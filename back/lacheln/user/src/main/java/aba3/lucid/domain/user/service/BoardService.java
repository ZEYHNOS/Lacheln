package aba3.lucid.domain.user.service;

import aba3.lucid.domain.board.BoardEntity;
import aba3.lucid.dto.board.BoardRequest;
import aba3.lucid.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    /*public BoardEntity create(BoardRequest boardRequest) {
        var entity = BoardEntity.builder()
                .boardName(boardRequest.getBoardName()) // boardName 설정
                .build();

        //TODO save 구현
        //return boardRepository.save(entity); // 저장 처리
    }*/

}
