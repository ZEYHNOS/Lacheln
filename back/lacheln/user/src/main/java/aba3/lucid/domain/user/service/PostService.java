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

    /**
     * 게시글 생성 - PostEntity 저장 + 이미지 저장
     * @return 저장된 PostEntity
     */
    @Transactional
    public PostEntity createPost(String userId, String title, String content, Long boardId, List<String> imageUrls) {
        // 1. 게시판 확인
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "게시판이 존재하지 않습니다."));

        // 2. 사용자 확인
        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "사용자가 존재하지 않습니다."));

        // 3. 전체/인기 게시판 작성 제한
        if (board.getBoardName().equals("전체") || board.getBoardName().equals("인기")) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "전체/인기 게시판에는 글을 작성할 수 없습니다.");
        }

        // 4. 게시글 저장
        PostEntity post = PostEntity.builder()
                .postTitle(title)
                .postContent(content)
                .board(board)
                .usersEntity(user)
                .postStatus(PostStatus.CREATED)
                .postCreate(LocalDateTime.now())
                .postUpdate(LocalDateTime.now())
                .build();
        PostEntity savedPost = postRepository.save(post);

        // 5. 이미지 저장
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
     * 단일 게시글 조회
     */
    public PostEntity getPostById(long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시글이 존재하지 않습니다."));
    }

    /**
     * 특정 게시판의 게시글 목록 조회
     */
    @Transactional(readOnly = true)
    public List<PostEntity> getPostListByBoardId(long boardId) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시판이 존재하지 않습니다."));
        return postRepository.findAllByBoard(board);
    }

    /**
     * 자유/질문/리뷰 전체 게시판 목록 조회
     */
    public List<PostEntity> getAllCategoryPosts() {
        List<String> boardNames = List.of("자유게시판", "질문게시판", "리뷰게시판");
        return postRepository.findByBoard_BoardNameIn(boardNames);
    }

    /**
     * 추천수 15 이상 인기 게시글 조회
     */
    public List<PostEntity> getPopularPosts() {
        List<String> boardNames = List.of("자유게시판", "질문게시판", "리뷰게시판");
        return postRepository.findPopularPosts(boardNames);
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public PostEntity updatePost(PostUpdateRequest request, String userId) {
        PostEntity post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시글이 존재하지 않습니다."));

        if (!post.getUsersEntity().getUserId().equals(userId)) {
            throw new ApiException(ErrorCode.FORBIDDEN, "게시글 수정 권한이 없습니다.");
        }

        // 기존 이미지 삭제
        postImageRepository.deleteAll(post.getPostImageList());

        // 도메인 메서드 방식으로 수정
        post.updatePost(
                request.getPostTitle(),
                request.getPostContent(),
                LocalDateTime.now()
        );

        // 이미지 다시 저장
        List<PostImageEntity> newImages = request.getImageUrls().stream()
                .map(url -> PostImageEntity.builder()
                        .post(post)
                        .postImageUrl(url)
                        .build())
                .toList();
        postImageRepository.saveAll(newImages);

        return post;
    }
}
