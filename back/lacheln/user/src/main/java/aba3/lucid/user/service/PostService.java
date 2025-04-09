package aba3.lucid.user.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.board.dto.PostUpdateRequest;
import aba3.lucid.domain.board.entity.*;
import aba3.lucid.domain.board.enums.PostStatus;
import aba3.lucid.domain.board.repository.*;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final BoardRepository boardRepository;
    private final UsersRepository usersRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostViewRepository postViewRepository;

    /**
     * 게시글 생성 서비스
     * - 사용자 및 게시판 존재 여부 검증
     * - 전체/인기 게시판 글 작성 금지
     * - 게시글 및 첨부 이미지 저장
     *
     * @param userId    작성자 ID
     * @param title     게시글 제목
     * @param content   게시글 본문
     * @param boardId   게시판 ID
     * @param imageUrls 첨부 이미지 리스트
     * @return 저장된 PostEntity
     */
    @Transactional
    public PostEntity createPost(String userId, String title, String content, Long boardId, List<String> imageUrls) {
        // 1. 게시판 존재 여부 확인
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "게시판이 존재하지 않습니다."));

        // 2. 사용자 존재 여부 확인
        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "사용자가 존재하지 않습니다."));

        // 3. 전체/인기 게시판은 직접 글 작성 불가
        if (board.getBoardName().equals("전체") || board.getBoardName().equals("인기")) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "전체/인기 게시판에는 글을 작성할 수 없습니다.");
        }

        // 4. 게시글 생성
        PostEntity post = PostEntity.builder()
                .postTitle(title)
                .postContent(content)
                .board(board)
                .usersEntity(user)
                .postStatus(PostStatus.CREATED)
                .postCreate(LocalDateTime.now())
                .postUpdate(LocalDateTime.now())
                .build();

        // 5. 게시글 저장
        PostEntity savedPost = postRepository.save(post);

        // 6. 첨부 이미지가 있을 경우 저장
        if (imageUrls != null && !imageUrls.isEmpty()) {
            List<PostImageEntity> images = imageUrls.stream()
                    .map(url -> PostImageEntity.builder()
                            .post(savedPost)
                            .postImageUrl(url)
                            .build())
                    .toList();
            postImageRepository.saveAll(images);
        }

        return savedPost;
    }

    /**
     * 게시글 단건 조회 (삭제되지 않은 글만)
     *
     * @param postId 게시글 ID
     * @return PostEntity
     */
    @Transactional(readOnly = true)
    public PostEntity getPostById(long postId) {
        return postRepository.findByPostIdAndDeletedFalse(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시글이 존재하지 않거나 삭제되었습니다."));
    }

    /**
     * 개별 게시판 글 목록 조회 (삭제되지 않은 글만)
     *
     * @param boardId 게시판 ID
     * @return 게시글 리스트
     */
    @Transactional(readOnly = true)
    public List<PostEntity> getPostListByBoardId(long boardId) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시판이 존재하지 않습니다."));
        return postRepository.findAllByBoardAndDeletedFalse(board);
    }

    /**
     * 전체 게시판(자유/질문/리뷰) 통합 글 목록 조회
     * - 삭제되지 않은 글만 반환
     *
     * @return 게시글 리스트
     */
    @Transactional(readOnly = true)
    public List<PostEntity> getAllCategoryPosts() {
        List<String> boardNames = List.of("자유게시판", "질문게시판", "리뷰게시판");
        return postRepository.findByBoardBoardNameInAndDeletedFalse(boardNames);
    }

    /**
     * 게시글 수정
     * - 작성자 본인만 가능
     * - 이미지 삭제 및 추가, 제목/내용 수정
     *
     * @param request 수정 요청 DTO
     * @param userId  요청한 사용자 ID
     * @return 수정된 PostEntity
     */
    @Transactional
    public PostEntity updatePost(PostUpdateRequest request, String userId) {
        PostEntity post = postRepository.findByPostIdAndDeletedFalse(request.getPostId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시글이 존재하지 않거나 삭제되었습니다."));

        if (!post.getUsersEntity().getUserId().equals(userId)) {
            throw new ApiException(ErrorCode.FORBIDDEN, "게시글 수정 권한이 없습니다.");
        }

        // 이미지 삭제 처리
        if (request.getDeleteImageUrls() != null && !request.getDeleteImageUrls().isEmpty()) {
            List<PostImageEntity> toDelete = post.getPostImageList().stream()
                    .filter(img -> request.getDeleteImageUrls().contains(img.getPostImageUrl()))
                    .toList();
            postImageRepository.deleteAllInBatch(toDelete);
        }

        // 제목/내용/수정일 갱신
        post.updatePost(request.getPostTitle(), request.getPostContent(), LocalDateTime.now());

        // 새 이미지 추가
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            List<PostImageEntity> newImages = request.getImageUrls().stream()
                    .map(url -> PostImageEntity.builder()
                            .post(post)
                            .postImageUrl(url)
                            .build())
                    .toList();
            postImageRepository.saveAll(newImages);
        }

        return post;
    }

    /**
     * 게시글 삭제 (논리 삭제 처리)
     * - 작성자 본인만 삭제 가능
     */
    @Transactional
    public void deletePost(long postId, String userId) {
        PostEntity post = postRepository.findByPostIdAndDeletedFalse(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시글이 존재하지 않거나 삭제되었습니다."));

        if (!post.getUsersEntity().getUserId().equals(userId)) {
            throw new ApiException(ErrorCode.FORBIDDEN, "게시글 삭제 권한이 없습니다.");
        }

        post.delete(); // 논리 삭제 (deleted = true)
    }

    /**
     * 게시글 추천 기능
     * - 중복 추천 방지
     */
    @Transactional
    public void likePost(Long postId, String userId) {
        PostEntity post = postRepository.findByPostIdAndDeletedFalse(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "게시글이 존재하지 않거나 삭제되었습니다."));

        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "사용자가 존재하지 않습니다."));

        if (postLikeRepository.existsByPostPostIdAndUsersUserId(postId, userId)) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "이미 추천한 게시글입니다.");
        }

        PostLikeEntity like = PostLikeEntity.builder()
                .post(post)
                .users(user)
                .build();
        postLikeRepository.save(like);
    }

    /**
     * 게시글 조회 기록 저장
     * - 단순 조회수 증가 (중복 방지 X)
     */
    @Transactional
    public void addPostView(Long postId, String userId) {
        PostEntity post = postRepository.findByPostIdAndDeletedFalse(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "게시글이 존재하지 않거나 삭제되었습니다."));

        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "사용자가 존재하지 않습니다."));

        PostViewEntity view = PostViewEntity.builder()
                .post(post)
                .users(user)
                .build();
        postViewRepository.save(view);
    }

    /**
     * 개별 게시판 페이징 조회
     * - 정렬 기준: postCreate (최신순)
     */
    @Transactional(readOnly = true)
    public Page<PostEntity> getPostPageByBoardId(Long boardId, Pageable pageable) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "게시판이 존재하지 않습니다."));
        return postRepository.findAllByBoardAndDeletedFalse(board, pageable);
    }

    /**
     * 전체 게시판(자유/질문/리뷰) 페이징 조회
     * - 정렬 기준: postCreate (최신순)
     */
    @Transactional(readOnly = true)
    public Page<PostEntity> getAllCategoryPostPage(Pageable pageable) {
        List<String> boardNames = List.of("자유게시판", "질문게시판", "리뷰게시판");
        return postRepository.findByBoardBoardNameInAndDeletedFalse(boardNames, pageable);
    }

    /**
     * 인기 게시판 페이징 조회
     * - 조건: 추천 수 15 이상
     * - 정렬 기준: popularRegisteredAt (인기 등록 시점)
     */
    @Transactional(readOnly = true)
    public Page<PostEntity> getPopularPostPage(Pageable pageable) {
        List<String> boardNames = List.of("자유게시판", "질문게시판", "리뷰게시판");
        return postRepository.findPopularPosts(boardNames, pageable);
    }

    /**
     * 게시글 조회수 반환
     *
     * @param postId 게시글 ID
     * @return 조회수
     */
    public long getViewCount(Long postId) {
        return postViewRepository.countByPostPostId(postId);
    }

    /**
     * 게시글 추천 수 반환
     *
     * @param postId 게시글 ID
     * @return 추천 수
     */
    public long getLikeCount(Long postId) {
        return postLikeRepository.countByPostPostId(postId);
    }
}