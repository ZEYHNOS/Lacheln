package aba3.lucid.domain.user.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.domain.board.dto.PostRequest;
import aba3.lucid.domain.board.dto.PostResponse;
import aba3.lucid.domain.user.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
}
