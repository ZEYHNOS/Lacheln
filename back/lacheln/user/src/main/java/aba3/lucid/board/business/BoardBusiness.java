package aba3.lucid.board.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.board.convertor.BoardConvertor;
import aba3.lucid.domain.board.dto.BoardRequest;
import aba3.lucid.domain.board.dto.BoardResponse;
import aba3.lucid.board.service.BoardService;
import aba3.lucid.domain.board.entity.BoardEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.enums.TierEnum;
import aba3.lucid.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class BoardBusiness {

    private final BoardService boardService;
    private final BoardConvertor boardConvertor;
    private final UserService userService;

    // 전체 게시판 목록 조회
    public List<BoardResponse> getAllBoards() {
        return boardService.getAllBoards();
    }

    // 특정 게시판 단건 조회
    public BoardResponse getBoardById(Long boardId) {
        BoardEntity entity = boardService.getBoardByIdWithThrow(boardId);
        return boardConvertor.toResponse(entity);
    }

    // 게시판 생성 (ADMIN 전용)
    public BoardResponse createBoard(BoardRequest boardRequest) {
        validateAdminRole(); // ✅ 관리자 권한 검증

        if (boardService.existsByBoardName(boardRequest.getBoardName())) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "이미 존재하는 게시판입니다: " + boardRequest.getBoardName());
        }

        return boardService.createBoard(boardRequest);
    }

    // 게시판 수정 (ADMIN 전용)
    public BoardResponse updateBoard(Long boardId, BoardRequest boardRequest) {
        validateAdminRole(); // ✅ 관리자 권한 검증

        if (boardService.isBoardNameDuplicated(boardId, boardRequest.getBoardName())) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "이미 존재하는 게시판 이름입니다: " + boardRequest.getBoardName());
        }

        return boardService.updateBoard(boardId, boardRequest);
    }

    // 게시판 삭제 (ADMIN 전용)
    public void deleteBoard(Long boardId) {
        validateAdminRole(); // ✅ 관리자 권한 검증
        boardService.deleteBoard(boardId);
    }

    // ✅ 관리자 권한 검증 메서드
    private void validateAdminRole() {
        String userId = AuthUtil.getUserId(); // 인증된 사용자 ID
        UsersEntity user = userService.findByIdWithThrow(userId); // 사용자 조회

        if (!TierEnum.ADMIN.equals(user.getUserTier())) {
            throw new ApiException(ErrorCode.FORBIDDEN, "관리자 권한이 필요합니다.");
        }
    }
}
