package aba3.lucid.board.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.domain.board.dto.BoardRequest;
import aba3.lucid.domain.board.dto.BoardResponse;
import aba3.lucid.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * BoardBusiness는 비즈니스 계층으로, 서비스 계층(BoardService)을 호출하여
 * 실제 로직을 수행하기 전/후로 필요한 추가 검증, 흐름 제어 등을 담당하는 중간 계층
 */
@Slf4j
@Business
@RequiredArgsConstructor
public class BoardBusiness {

    private final BoardService boardService;

    /**
     * 전체 게시판 목록 조회
     */
    public List<BoardResponse> getAllBoards() {
        return boardService.getAllBoards();
    }

    /**
     * 특정 게시판 조회
     */
    public BoardResponse getBoardById(long boardId) {
        return boardService.getBoardById(boardId);
    }

    /**
     * 게시판 생성
     */
    public BoardResponse createBoard(BoardRequest boardRequest) {
        return boardService.createBoard(boardRequest);
    }

    /**
     * 게시판 수정
     */
    public BoardResponse updateBoard(long boardId, BoardRequest boardRequest) {
        return boardService.updateBoard(boardId, boardRequest);
    }

    /**
     * 게시판 삭제
     */
    public void deleteBoard(long boardId) {
        boardService.deleteBoard(boardId);
    }
}
