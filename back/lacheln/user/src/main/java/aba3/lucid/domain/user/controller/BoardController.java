package aba3.lucid.domain.user.controller;

import aba3.lucid.domain.board.dto.BoardRequest;
import aba3.lucid.domain.board.entity.BoardEntity;
import aba3.lucid.domain.user.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시판 생성 ADMIN 등급만 할 수 있고 이외의 등급은 접근 못 하도록 해야함
     */
    @PostMapping("")
    public BoardEntity create(
        @Valid
        @RequestBody BoardRequest boardRequest
    ){
        return boardService.create(boardRequest);
    }
}

