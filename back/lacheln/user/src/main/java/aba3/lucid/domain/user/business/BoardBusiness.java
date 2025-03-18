package aba3.lucid.domain.user.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.board.dto.BoardRequest;
import aba3.lucid.domain.board.dto.BoardResponse;
import aba3.lucid.domain.board.entity.BoardEntity;
import aba3.lucid.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@Business
@RequiredArgsConstructor
public class BoardBusiness {

    private final BoardRepository boardRepository;

    /**
     * 모든 게시판 목록 조회
     */
    public List<BoardResponse> getAllBoards() {
        List<BoardEntity> boards = boardRepository.findAll();
        return boards.stream()
                .map(board -> new BoardResponse(board.getBoardId(), board.getBoardName()))
                .toList();
    }

    /**
     * 특정 게시판 조회
     */
    public BoardResponse getBoardById(long boardId) {
        BoardEntity board = findBoardByIdWithThrow(boardId);

        return new BoardResponse(board.getBoardId(), board.getBoardName());
    }

    /**
     *
     * @param boardRequest 클라이언트(ADMIN)가 전달한 게시판 생성 요청 데이터(게시판 이름)
     * @return 생성된 게시판 엔티티(데이터베이스에 저장된 상태)
     */
    public BoardResponse createBoard(
            BoardRequest boardRequest
    ){
        //게시판 이름 중복 검사
        Optional<BoardEntity> duplicationBoard = boardRepository.findByBoardName(boardRequest.getBoardName());
        if (duplicationBoard.isPresent()) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "이미 존재하는 게시판 입니다: " + boardRequest.getBoardName());
        }

        //새 게시판 생성
        BoardEntity entity = BoardEntity.builder()
                .boardName(boardRequest.getBoardName())
                .build();

        BoardEntity saveEntity = boardRepository.save(entity);

        //BoardEntity -> BoardResponse 변환
        return new BoardResponse(saveEntity.getBoardId(), saveEntity.getBoardName());
    }

    /**
     * 게시판 삭제 기능
     * @param boardId 삭제할 게시판의 ID
     */
    public void deleteBoard(long boardId) {
        //게시판 존재 여부 확인
        BoardEntity board = findBoardByIdWithThrow(boardId);

        //게시판 삭제
        boardRepository.delete(board);
    }

    /**
     * 게시판 수정 기능
     * @param boardId 수정할 게시판 ID
     * @param boardRequest 새로운 게시판 정보 (boardName)
     * @return 수정된 게시판 정보
     */
    public BoardResponse updateBoard(long boardId, BoardRequest boardRequest) {
        //해당 ID의 게시판 존재유무 확인
        BoardEntity board = findBoardByIdWithThrow(boardId);

        //(수정 시 게시판 중복 방지)
        Optional<BoardEntity> duplicationBoard = boardRepository.findByBoardName(boardRequest.getBoardName());
        if (duplicationBoard.isPresent() && duplicationBoard.get().getBoardId() != boardId) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "이미 존재하는 게시판 이름입니다: " + boardRequest.getBoardName());
        }

        //게시판 이름 수정
        board = BoardEntity.builder()
                .boardId(board.getBoardId()) //ID 유지
                .boardName(boardRequest.getBoardName())
                .postList(board.getPostList()) //해당 게시판 게시글들 유지
                .build();

        //수정된 게시판 저장
        BoardEntity updatedBoard = boardRepository.save(board);

        //응답 DTO로 변환 후 반환
        return new BoardResponse(updatedBoard.getBoardId(), updatedBoard.getBoardName());
    }

    //게시판 존재 확인
    private BoardEntity findBoardByIdWithThrow(long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시판이 존재하지 않습니다: " + boardId));
    }
}
