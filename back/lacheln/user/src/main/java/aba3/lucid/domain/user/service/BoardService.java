package aba3.lucid.domain.user.service;

import aba3.lucid.domain.board.dto.BoardRequest;
import aba3.lucid.domain.board.entity.BoardEntity;
import aba3.lucid.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     *
     * @param boardRequest 클라이언트(ADMIN)가 전달한 게시판 생성 요청 데이터(게시판 이름)
     * @return 생성된 게시판 엔티티(데이터베이스에 저장된 상태)
     */
    public BoardEntity create(
            BoardRequest boardRequest
    ){
        BoardEntity entity = BoardEntity.builder()
                .boardName(boardRequest.getBoardName())
                .build();

        return boardRepository.save(entity);
    }
}

