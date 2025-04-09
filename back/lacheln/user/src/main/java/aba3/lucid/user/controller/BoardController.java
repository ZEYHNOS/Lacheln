package aba3.lucid.user.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.domain.board.dto.BoardRequest;
import aba3.lucid.domain.board.dto.BoardResponse;
import aba3.lucid.user.business.BoardBusiness;
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

    /**
     * 전체 게시판 목록 조회
     * http://localhost:5052/board
     */
    @GetMapping("")
    @Operation(
            summary = "전체 게시판 조회",
            description = "전체 게시판을 조회",
            responses = {
                @ApiResponse(responseCode = "200", description = "성공")
            }
    )
    public API<List<BoardResponse>> getAllBoards() {
        return API.OK(boardBusiness.getAllBoards());
    }

    /**
     * 특정 게시판 조회
     * http://localhost:5052/board/{boardId}
     */
    @GetMapping("/{boardId}")
    @Operation(
            summary = "특정 게시판 조회",
            description = "게시판 ID를 이용하여 특정 게시판을 조회",
            responses = {
                @ApiResponse(responseCode = "200", description = "성공"),
                @ApiResponse(responseCode = "404", description = "해당 게시판이 존재하지 않습니다")
            }
    )
    public API<BoardResponse> getBoardById(
            @PathVariable @Min(1) long boardId
    ) {
        return API.OK(boardBusiness.getBoardById(boardId));
    }

    /**
     * 게시판 (생성, 삭제, 수정) ADMIN 등급만 할 수 있고 이외의 등급은 접근 못 하도록 해야함
     * 프론트 역시 ADMIN이 아니라면 버튼 자체가 화면에 생성되면 안 됨
     */

    /**
     * 게시판 생성
     * http://localhost:5052/board
     * TODO ADMIN 권한 확인 후 게시판 생성
     */
    @PostMapping("")
    @Operation(
            summary = "게시판 생성",
            description = "새로운 게시판을 생성합니다. (ADMIN 권한 필요)",
            responses = {
                    @ApiResponse(responseCode = "201", description = "게시판 생성 성공"),
                    @ApiResponse(responseCode = "400", description = "이미 존재하는 게시판 이름")
            }
    )
    public API<BoardResponse> create(
        @Valid
        @RequestBody BoardRequest boardRequest
    ){
        BoardResponse res = boardBusiness.createBoard(boardRequest);
        return API.OK(res);
    }

    /**
     * 게시판 삭제
     * http://localhost:5052/board/{boardId}
     * TODO ADMIN 권한 확인 후 게시판 삭제
     */
    @DeleteMapping("/{boardId}")
    @Operation(
            summary = "게시판 삭제",
            description = "특정 게시판을 삭제합니다. (ADMIN 권한 필요)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시판 삭제 성공"),
                    @ApiResponse(responseCode = "404", description = "해당 게시판이 존재하지 않습니다")
            }
    )
    public API<String> deleteBoard(@PathVariable long boardId) {
        boardBusiness.deleteBoard(boardId);
        return API.OK("게시판이 삭제되었습니다: " + boardId);
    }

    /**
     * 게시판 수정
     * http://localhost:5052/board/{boardId}
     * TODO ADMIN 권한 확인 후 게시판 수정
     */
    @PutMapping("/{boardId}")
    @Operation(
            summary = "게시판 수정",
            description = "특정 게시판의 이름을 수정합니다. (ADMIN 권한 필요)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시판 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "이미 존재하는 게시판 이름"),
                    @ApiResponse(responseCode = "404", description = "해당 게시판이 존재하지 않습니다")
            }
    )
    public API<BoardResponse> updateBoard(
            @PathVariable long boardId,
            @Valid @RequestBody BoardRequest boardRequest
    ) {
        return API.OK(boardBusiness.updateBoard(boardId, boardRequest));
    }
}

