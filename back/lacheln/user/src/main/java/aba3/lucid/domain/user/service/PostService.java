package aba3.lucid.domain.user.service;

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
     * 게시글 생성 - PostEntity 저장 + 이미지 저장
     *
     * 흐름:
     * 1. 게시판과 사용자 정보 조회 (존재 여부 검증)
     * 2. 전체/인기 게시판에는 글 작성 제한
     * 3. 게시글(PostEntity) 생성 및 저장
     * 4. 첨부 이미지(PostImageEntity) 함께 저장
     *
     * @return 저장된 PostEntity
     */
    @Transactional
    public PostEntity createPost(String userId, String title, String content, Long boardId, List<String> imageUrls) {
        // 게시판 존재 여부 확인
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "게시판이 존재하지 않습니다."));

        // 사용자 존재 여부 확인
        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "사용자가 존재하지 않습니다."));

        // 전체/인기 게시판은 글 작성 불가
        if (board.getBoardName().equals("전체") || board.getBoardName().equals("인기")) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "전체/인기 게시판에는 글을 작성할 수 없습니다.");
        }

        // 게시글 엔티티 생성
        PostEntity post = PostEntity.builder()
                .postTitle(title)
                .postContent(content)
                .board(board)
                .usersEntity(user)
                .postStatus(PostStatus.CREATED)
                .postCreate(LocalDateTime.now())
                .postUpdate(LocalDateTime.now())
                .build();

        // 게시글 저장
        PostEntity savedPost = postRepository.save(post);

        // 첨부 이미지가 있다면 PostImageEntity로 변환 후 저장
        List<PostImageEntity> images = imageUrls.stream()
                .map(url -> PostImageEntity.builder()
                        .post(savedPost)
                        .postImageUrl(url)
                        .build())
                .toList();
        postImageRepository.saveAll(images);

        return savedPost;
    }

    /**
     * 단일 게시글 조회 (삭제되지 않은 게시글만)
     *
     * @param postId 게시글 ID
     * @return 게시글 Entity
     * @throws ApiException 게시글이 없거나 삭제된 경우
     */
    public PostEntity getPostById(long postId) {
        return postRepository.findByPostIdAndDeletedFalse(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시글이 존재하지 않거나 삭제되었습니다."));
    }

    /**
     * 특정 게시판의 게시글 목록 조회 (삭제되지 않은 글만)
     *
     * @param boardId 게시판 ID
     * @return 게시글 목록
     * @throws ApiException 게시판이 존재하지 않을 경우
     */
    @Transactional(readOnly = true)
    public List<PostEntity> getPostListByBoardId(long boardId) {
        // 게시판 확인
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시판이 존재하지 않습니다."));
        // 해당 게시판의 삭제되지 않은 게시글 리스트 반환
        return postRepository.findAllByBoardAndDeletedFalse(board);
    }

    /**
     * 자유/질문/리뷰 게시판 통합 조회
     * - 삭제되지 않은 게시글만 조회
     *
     * @return 통합 게시글 목록
     */
    public List<PostEntity> getAllCategoryPosts() {
        List<String> boardNames = List.of("자유게시판", "질문게시판", "리뷰게시판");
        return postRepository.findByBoardBoardNameInAndDeletedFalse(boardNames);
    }

    /**
     * 게시글 수정
     *
     * 흐름:
     * 1. 게시글 조회 및 삭제 여부 확인
     * 2. 작성자 본인 여부 확인
     * 3. 삭제 요청된 이미지 삭제
     * 4. 제목/본문/수정일 업데이트
     * 5. 새 이미지 추가
     *
     * @return 수정 완료된 PostEntity
     */
    @Transactional
    public PostEntity updatePost(PostUpdateRequest request, String userId) {
        // 게시글 조회 (삭제된 경우 예외 발생)
        PostEntity post = postRepository.findByPostIdAndDeletedFalse(request.getPostId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시글이 존재하지 않거나 삭제되었습니다."));

        // 작성자 본인 확인
        if (!post.getUsersEntity().getUserId().equals(userId)) {
            throw new ApiException(ErrorCode.FORBIDDEN, "게시글 수정 권한이 없습니다.");
        }

        // 삭제 요청된 이미지가 있다면 삭제
        if (request.getDeleteImageUrls() != null && !request.getDeleteImageUrls().isEmpty()) {
            // 삭제 대상 이미지 목록 필터링
            List<PostImageEntity> toDelete = post.getPostImageList().stream()
                    .filter(img -> request.getDeleteImageUrls().contains(img.getPostImageUrl()))
                    .toList();

            // 실제 DB에서 이미지 삭제 처리 (배치 삭제로 성능 최적화)
            postImageRepository.deleteAllInBatch(toDelete);
        }

        // 제목, 내용, 수정일 갱신
        post.updatePost(
                request.getPostTitle(),
                request.getPostContent(),
                LocalDateTime.now()
        );

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
     * 게시글 삭제 (논리 삭제)
     *
     * 흐름:
     * 1. 게시글 존재 여부 및 삭제 상태 확인
     * 2. 작성자 본인 여부 확인 (관리자 판단은 Business 계층에서 수행)
     * 3. deleted 플래그 true로 설정하여 논리 삭제 처리
     */
    @Transactional
    public void deletePost(long postId, String userId) {
        // 삭제되지 않은 게시글 조회
        PostEntity post = postRepository.findByPostIdAndDeletedFalse(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시글이 존재하지 않거나 삭제되었습니다."));

        // 작성자 본인 확인
        if (!post.getUsersEntity().getUserId().equals(userId)) {
            throw new ApiException(ErrorCode.FORBIDDEN, "게시글 삭제 권한이 없습니다.");
        }

        // 논리 삭제 처리 (deleted = true)
        post.delete();
    }

    /**
     * 게시글 추천 처리
     * - 사용자가 특정 게시글에 추천을 1회 누르면, 추천 정보를 저장합니다.
     * - 이미 추천한 경우 예외를 발생시킵니다.
     *
     * @param postId 추천할 게시글 ID
     * @param userId 추천을 누른 사용자 ID
     */
    @Transactional
    public void likePost(Long postId, String userId) {
        // 1. 게시글 조회 (삭제된 글은 추천 불가)
        PostEntity post = postRepository.findByPostIdAndDeletedFalse(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "게시글이 존재하지 않거나 삭제되었습니다."));

        // 2. 사용자 조회
        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "사용자가 존재하지 않습니다."));

        // 3. 이미 추천한 경우 예외 발생
        boolean alreadyLiked = postLikeRepository.existsByPostPostIdAndUsersUserId(postId, userId);
        if (alreadyLiked) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "이미 추천한 게시글입니다.");
        }

        // 4. 추천 정보 저장
        PostLikeEntity like = PostLikeEntity.builder()
                .post(post)
                .users(user)
                .build();

        postLikeRepository.save(like);
    }

    /**
     * 게시글을 조회한 사용자의 조회 정보를 저장하는 메서드
     * - 게시글 상세 페이지 접속 시 호출됨
     * - 현재는 중복 조회 방지 없이 단순히 PostViewEntity를 저장함
     *
     * @param postId 조회 대상 게시글 ID
     * @param userId 조회한 사용자 ID
     */
    @Transactional
    public void addPostView(Long postId, String userId) {
        // 1. 게시글 존재 여부 확인 (삭제된 게시글은 예외 발생)
        PostEntity post = postRepository.findByPostIdAndDeletedFalse(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "게시글이 존재하지 않거나 삭제되었습니다."));

        // 2. 사용자 정보 조회
        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "사용자가 존재하지 않습니다."));

        // 3. PostViewEntity 생성 및 저장 (중복 여부는 일단 검사 안 함)
        PostViewEntity view = PostViewEntity.builder()
                .post(post)
                .users(user)
                .build();

        postViewRepository.save(view);
    }

    /**
     * 특정 게시판의 게시글을 페이징으로 조회
     * - 삭제되지 않은 게시글만 조회됨
     */
    @Transactional(readOnly = true)
    public Page<PostEntity> getPostPageByBoardId(Long boardId, Pageable pageable) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "게시판이 존재하지 않습니다."));
        return postRepository.findAllByBoardAndDeletedFalse(board, pageable);
    }

    /**
     * 전체 게시판(자유/질문/리뷰) 통합 페이징 조회
     */
    public Page<PostEntity> getAllCategoryPostPage(Pageable pageable) {
        List<String> boardNames = List.of("자유게시판", "질문게시판", "리뷰게시판");
        return postRepository.findByBoardBoardNameInAndDeletedFalse(boardNames, pageable);
    }

    /**
     * 특정 게시글의 조회수를 반환하는 메서드
     * - PostViewEntity 테이블에서 해당 게시글과 관련된 모든 레코드 수를 계산
     *
     * @param postId 게시글 ID
     * @return 게시글의 총 조회수
     */
    public long getViewCount(Long postId) {
        return postViewRepository.countByPostPostId(postId);
    }

    public long getLikeCount(Long postId) {
        return postLikeRepository.countByPostPostId(postId);
    }

    /**
     * 추천 수가 특정 수 이상인 인기 게시글을 페이징으로 조회
     *
     * @param minLike 최소 추천 수 (예: 15)
     * @param pageable 페이징 정보
     * @return 인기 게시글 Page
     */
    @Transactional(readOnly = true)
    public Page<PostEntity> getPopularPostPage(int minLike, Pageable pageable) {
        return postRepository.findPopularPosts(minLike, pageable);
    }
}