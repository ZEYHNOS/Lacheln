package aba3.lucid.domain.user.service;

import aba3.lucid.domain.board.dto.BoardRequest;
import aba3.lucid.domain.board.dto.BoardResponse;
import aba3.lucid.domain.board.entity.BoardEntity;
import aba3.lucid.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

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
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시판이 존재하지 않습니다: " + boardId));

        return new BoardResponse(board.getBoardId(), board.getBoardName());
    }

    /**
     *
     * @param boardRequest 클라이언트(ADMIN)가 전달한 게시판 생성 요청 데이터(게시판 이름)
     * @return 생성된 게시판 엔티티(데이터베이스에 저장된 상태)
     */
    public BoardResponse create(
            BoardRequest boardRequest
    ){
        //게시판 이름 중복 검사
        Optional<BoardEntity> duplicationBoard = boardRepository.findByBoardName(boardRequest.getBoardName());
        if (duplicationBoard.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 게시판 입니다.");
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
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시판이 존재하지 않습니다: " + boardId));

        //게시판 삭제
        boardRepository.delete(board);
    }
}

