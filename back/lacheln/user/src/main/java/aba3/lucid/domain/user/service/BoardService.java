package aba3.lucid.domain.user.service;

import aba3.lucid.domain.board.dto.BoardRequest;
import aba3.lucid.domain.board.dto.BoardResponse;
import aba3.lucid.domain.user.business.BoardBusiness;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardBusiness boardBusiness;

    public List<BoardResponse> getAllBoards() {
        return boardBusiness.getAllBoards();
    }

    public BoardResponse getBoardById(long boardId) {
        return boardBusiness.getBoardById(boardId);
    }

    public BoardResponse createBoard(BoardRequest boardRequest) {
        return boardBusiness.createBoard(boardRequest);
    }

    public BoardResponse updateBoard(long boardId, BoardRequest boardRequest) {
        return boardBusiness.updateBoard(boardId, boardRequest);
    }

    public void deleteBoard(long boardId) {
        boardBusiness.deleteBoard(boardId);
    }
}

