package aba3.lucid.domain.user.controller;

import aba3.lucid.dto.board.BoardRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    @PostMapping("")
    public void create(
        @Valid
        BoardRequest boardRequest
    ){

    }
}
