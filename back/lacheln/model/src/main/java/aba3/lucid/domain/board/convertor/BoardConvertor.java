package aba3.lucid.domain.board.convertor;

import aba3.lucid.domain.board.dto.BoardRequest;
import aba3.lucid.domain.board.dto.BoardResponse;
import aba3.lucid.domain.board.entity.BoardEntity;
import org.springframework.stereotype.Component;

@Component
public class BoardConvertor {

    // DTO -> Entity 변환
    public BoardEntity toEntity(BoardRequest request) {
        return BoardEntity.builder()
                .boardName(request.getBoardName())
                .build();
    }

    // Entity -> DTO 변환
    public BoardResponse toResponse(BoardEntity entity) {
        return new BoardResponse(entity.getBoardId(), entity.getBoardName());
    }
}
