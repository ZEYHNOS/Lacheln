package aba3.lucid.board.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.domain.board.dto.BoardRequest;
import aba3.lucid.domain.board.dto.BoardResponse;
import aba3.lucid.board.business.BoardBusiness;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Board Controller", description = "게시판 관련 API")
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardBusiness boardBusiness;

    @GetMapping("")
    @Operation(summary = "전체 게시판 목록 조회", description = "전체 게시판을 조회합니다.",
            responses = {@ApiResponse(responseCode = "200", description = "성공")})
    public API<List<BoardResponse>> getAllBoards() {
        return API.OK(boardBusiness.getAllBoards());
    }

    @GetMapping("/{boardId}")
    @Operation(summary = "특정 게시판 조회", description = "게시판 ID를 통해 게시판을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "404", description = "해당 게시판이 존재하지 않습니다")
            })
    public API<BoardResponse> getBoardById(@PathVariable @Min(1) Long boardId) {
        return API.OK(boardBusiness.getBoardById(boardId));
    }

    @PostMapping("")
    @Operation(summary = "게시판 생성", description = "새 게시판을 생성합니다. (ADMIN 권한 필요)",
            responses = {
                    @ApiResponse(responseCode = "201", description = "생성 성공"),
                    @ApiResponse(responseCode = "400", description = "이미 존재하는 게시판 이름")
            })
    public API<BoardResponse> createBoard(@Valid @RequestBody BoardRequest boardRequest) {
        return API.OK(boardBusiness.createBoard(boardRequest));
    }

    @PutMapping("/{boardId}")
    @Operation(summary = "게시판 수정", description = "게시판 이름을 수정합니다. (ADMIN 권한 필요)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "수정 성공"),
                    @ApiResponse(responseCode = "400", description = "이름 중복"),
                    @ApiResponse(responseCode = "404", description = "게시판 없음")
            })
    public API<BoardResponse> updateBoard(
            @PathVariable Long boardId,
            @Valid @RequestBody BoardRequest boardRequest
    ) {
        return API.OK(boardBusiness.updateBoard(boardId, boardRequest));
    }

    @DeleteMapping("/{boardId}")
    @Operation(summary = "게시판 삭제", description = "게시판을 삭제합니다. (ADMIN 권한 필요)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "삭제 성공"),
                    @ApiResponse(responseCode = "404", description = "게시판 없음")
            })
    public API<String> deleteBoard(@PathVariable Long boardId) {
        boardBusiness.deleteBoard(boardId);
        return API.OK("게시판이 삭제되었습니다: " + boardId);
    }
}
