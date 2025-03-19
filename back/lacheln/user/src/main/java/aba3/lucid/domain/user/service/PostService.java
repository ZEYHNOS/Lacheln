package aba3.lucid.domain.user.service;

import aba3.lucid.domain.board.dto.PostRequest;
import aba3.lucid.domain.board.dto.PostResponse;
import aba3.lucid.domain.user.business.PostBusiness;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostBusiness postBusiness;

    /**
     * 게시글 생성
     */
    public PostResponse createPost(PostRequest postRequest, String userId) {
        return postBusiness.createPost(postRequest, userId);
    }
}
