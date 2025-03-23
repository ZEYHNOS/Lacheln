package aba3.lucid.domain.user.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.domain.board.dto.PostRequest;
import aba3.lucid.domain.board.dto.PostResponse;
import aba3.lucid.domain.user.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class PostBusiness {

    private final PostService postService;

    /**
     * 게시글 생성
     */
    public PostResponse createPost(PostRequest postRequest, String userId) {
        return postService.createPost(postRequest, userId);
    }

    /**
     * 게시글 조회(단건)
     * @param postId 조회할 게시글 ID
     * @return 조회된 게시글 응답 DTO
     */
    public PostResponse getPostById(long postId) {
        return postService.getPostById(postId);
    }

    /**
     * 게시판 ID를 기반으로 게시글 목록을 조회하는 비즈니스 계층
     * - 게시판이 존재하는지 확인 후 서비스 계층에 위임
     */
    public List<PostResponse> getPostListByBoardId(long boardId) {
        return postService.getPostListByBoardId(boardId);
    }

    /**
     * 전체 게시판 조회 (자유/질문/리뷰)
     */
    public List<PostResponse> getAllCategoryPosts() {
        return postService.getAllCategoryPosts();
    }
}
