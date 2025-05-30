package aba3.lucid.board.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.board.convertor.BoardConvertor;
import aba3.lucid.domain.board.dto.BoardRequest;
import aba3.lucid.domain.board.dto.BoardResponse;
import aba3.lucid.domain.board.entity.BoardEntity;
import aba3.lucid.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardConvertor boardConvertor;

    // 전체 게시판 조회
    public List<BoardResponse> getAllBoards() {
        return boardRepository.findAll().stream()
                .map(boardConvertor::toResponse)
                .toList();
    }

    // 게시판 생성
    @Transactional
    public BoardResponse createBoard(BoardRequest request) {
        BoardEntity newEntity = boardConvertor.toEntity(request);
        BoardEntity saved = boardRepository.save(newEntity);
        return boardConvertor.toResponse(saved);
    }

    // 게시판 이름 중복 확인
    public boolean existsByBoardName(String name) {
        return boardRepository.findByBoardName(name).isPresent();
    }

    // 게시판 이름이 본인 외에 중복인지 확인
    public boolean isBoardNameDuplicated(Long boardId, String name) {
        Optional<BoardEntity> existing = boardRepository.findByBoardName(name);
        return existing.isPresent() && !existing.get().getBoardId().equals(boardId);
    }

    // 게시판 수정
    @Transactional
    public BoardResponse updateBoard(Long boardId, BoardRequest request) {
        BoardEntity board = getBoardByIdWithThrow(boardId);
        board.changeBoardName(request.getBoardName());
        return boardConvertor.toResponse(board);
    }

    // 게시판 삭제
    @Transactional
    public void deleteBoard(Long boardId) {
        BoardEntity board = getBoardByIdWithThrow(boardId);
        board.getPostList().clear(); // 연관 게시글 클리어 후 삭제
        boardRepository.delete(board);
    }

    // 게시판 ID로 조회 + 예외처리 포함
    public BoardEntity getBoardByIdWithThrow(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시판이 존재하지 않습니다: " + boardId));
    }
}
