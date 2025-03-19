package aba3.lucid.domain.user.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.domain.board.dto.PostRequest;
import aba3.lucid.domain.board.dto.PostResponse;
import aba3.lucid.domain.user.business.PostBusiness;
import aba3.lucid.domain.user.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Post Controller", description = "게시글 관련 API")
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 게시글 생성
     * http://localhost:5052/post
     */
    @PostMapping("")
    @Operation(
            summary = "게시글 생성",
            description = "새로운 게시글을 생성합니다. (세미프로 이상부터 작성 가능)",
            responses = {
                    @ApiResponse(responseCode = "201", description = "게시글 생성 성공"),
                    @ApiResponse(responseCode = "400", description = "전체 & 인기 게시판 글 작성 불가"),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 게시판 또는 사용자")
            }
    )
    public API<PostResponse> createPost(
            @Valid @RequestBody PostRequest postRequest,
            @RequestParam String userId // TODO 임시로 userId 파라미터로 이용 (JWT 전)
    ) {
        PostResponse res = postService.createPost(postRequest, userId);
        return API.OK(res);
    }
}
