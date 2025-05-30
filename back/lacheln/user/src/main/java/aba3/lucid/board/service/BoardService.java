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

/**
 * BoardService는 서비스 계층
 * 트랜잭션 처리 및 실제 비즈니스 로직을 수행
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardConvertor boardConvertor;

    /**
     * 전체 게시판 목록 조회
     */
    public List<BoardResponse> getAllBoards() {
        List<BoardEntity> boards = boardRepository.findAll();
        return boards.stream()
                .map(boardConvertor::toResponse)
                .toList();
    }

    /**
     * 특정 게시판 조회
     * @param boardId 조회할 게시판 ID
     */
    public BoardResponse getBoardById(Long boardId) {
        BoardEntity board = findBoardByIdWithThrow(boardId);
        return boardConvertor.toResponse(board);
    }

    /**
     * 게시판 생성
     * @param boardRequest 생성할 게시판 이름 DTO
     */
    public BoardResponse createBoard(BoardRequest boardRequest) {
        // 게시판 이름 중복 검사
        if (boardRepository.findByBoardName(boardRequest.getBoardName()).isPresent()) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "이미 존재하는 게시판 입니다: " + boardRequest.getBoardName());
        }

        // 생성 및 저장
        BoardEntity entity = boardConvertor.toEntity(boardRequest);
        BoardEntity saveEntity = boardRepository.save(entity);
        return boardConvertor.toResponse(saveEntity);
    }

    /**
     * 게시판 삭제
     * @param boardId 삭제할 게시판 ID
     */
    @Transactional
    public void deleteBoard(Long boardId) {
        BoardEntity board = findBoardByIdWithThrow(boardId);

        // 게시글이 있을 경우 예외 발생 또는 제거 처리
        if (!board.getPostList().isEmpty()) {
            board.getPostList().clear(); // 필요시 게시글 연쇄 삭제
        }

        boardRepository.delete(board);
    }

    /**
     * 게시판 수정
     * @param boardId 수정할 게시판 ID
     * @param boardRequest 새로운 게시판 이름
     */
    @Transactional
    public BoardResponse updateBoard(Long boardId, BoardRequest boardRequest) {
        BoardEntity board = findBoardByIdWithThrow(boardId);

        // 이름 중복 검사
        boardRepository.findByBoardName(boardRequest.getBoardName())
                .filter(existingBoard -> existingBoard.getBoardId() != boardId)
                .ifPresent(existingBoard -> {
                    throw new ApiException(ErrorCode.BAD_REQUEST, "이미 존재하는 게시판 입니다: " + boardRequest.getBoardName());
                });

        // 이름 변경 (변경 감지)
        board.changeBoardName(boardRequest.getBoardName());

        return boardConvertor.toResponse(board);
    }

    /**
     * 게시판 ID로 게시판 조회(게시판 존재 유무 확인)
     * @param boardId 찾을 게시판 ID
     * @return 존재하는 게시판 Entity
     */
    private BoardEntity findBoardByIdWithThrow(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시판이 존재하지 않습니다: " + boardId));
    }
}

