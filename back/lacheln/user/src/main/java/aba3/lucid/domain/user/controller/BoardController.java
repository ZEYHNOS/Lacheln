package aba3.lucid.domain.user.controller;

import aba3.lucid.domain.board.dto.BoardRequest;
import aba3.lucid.domain.board.dto.BoardResponse;
import aba3.lucid.domain.board.entity.BoardEntity;
import aba3.lucid.domain.user.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /**
     * 전체 게시판 목록 조회
     * http://localhost:5052/board
     */
    @GetMapping("")
    public ResponseEntity<List<BoardResponse>> getAllBoards() {
        return ResponseEntity.ok(boardService.getAllBoards());
    }

    /**
     * 특정 게시판 조회
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponse> getBoardById(@PathVariable int boardId) {
        return ResponseEntity.ok(boardService.getBoardById(boardId));
    }

    /**
     * 게시판 생성, 삭제, 수정 ADMIN 등급만 할 수 있고 이외의 등급은 접근 못 하도록 해야함
     * 프론트 역시 ADMIN이 아니라면 버튼 자체가 화면에 생성되면 안 됨
     */

    /**
     * 게시판 생성
     */
    @PostMapping("")
    public BoardResponse create(
        @Valid
        @RequestBody BoardRequest boardRequest
    ){
        return boardService.create(boardRequest);
    }

    /**
     * 게시판 삭제
     */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok("게시판이 삭제되었습니다: " + boardId);
    }
}

