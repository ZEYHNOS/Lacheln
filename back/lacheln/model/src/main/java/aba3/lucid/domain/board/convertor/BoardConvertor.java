package aba3.lucid.domain.board.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.board.dto.BoardRequest;
import aba3.lucid.domain.board.dto.BoardResponse;
import aba3.lucid.domain.board.entity.BoardEntity;

@Converter
public class BoardConvertor {

    // DTO -> Entity 변환
    public BoardEntity toEntity(BoardRequest request) {
        return BoardEntity.builder()
                .boardName(request.getBoardName())
                .build();
    }

    // Entity -> DTO 변환
    public BoardResponse toResponse(BoardEntity entity) {
        return BoardResponse.builder()
                .boardId(entity.getBoardId())
                .boardName(entity.getBoardName())
                .build();
    }
}
