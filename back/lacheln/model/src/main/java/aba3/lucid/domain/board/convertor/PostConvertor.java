package aba3.lucid.domain.board.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.board.dto.PostDetailResponse;
import aba3.lucid.domain.board.dto.PostListResponse;
import aba3.lucid.domain.board.dto.PostRequest;
import aba3.lucid.domain.board.entity.BoardEntity;
import aba3.lucid.domain.board.entity.PostEntity;
import aba3.lucid.domain.board.enums.PostStatus;
import aba3.lucid.domain.user.entity.UsersEntity;

import java.util.List;

/**
 * 게시글 관련 Entity <-> DTO 변환기
 * - Controller <-> Service/Business 계층 사이에서 데이터 변환 역할을 수행합니다.
 */
@Converter
public class PostConvertor {

    /**
     * 게시글 작성 요청 DTO → 게시글 Entity 변환
     *
     * @param request PostRequest 객체 (제목, 내용, 게시판 ID 포함)
     * @param board 게시판 Entity (작성 대상 게시판)
     * @param user 작성자 Entity (로그인된 사용자)
     * @return 저장 가능한 PostEntity
     */
    public PostEntity toEntity(PostRequest request, BoardEntity board, UsersEntity user) {
        return PostEntity.builder()
                .postTitle(request.getPostTitle())
                .postContent(request.getPostContent()) // HTML 포함 가능
                .board(board)
                .usersEntity(user)
                .postStatus(PostStatus.CREATED)
                .build();
    }

    /**
     * 게시글 목록 조회용 DTO 변환기
     *
     * @param post 게시글 Entity
     * @param likeCount 추천 수
     * @param viewCount 조회 수
     * @return 목록 응답용 DTO
     */
    public PostListResponse toListResponse(PostEntity post, int likeCount, int viewCount) {
        int commentCount = post.getCommentList() != null ? post.getCommentList().size() : 0;

        return PostListResponse.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .userNickName(post.getUsersEntity().getUserNickName())
                .category(post.getBoard().getBoardName())
                .postCreate(post.getPostCreate())
                .likeCount(likeCount)
                .viewCount(viewCount)
                .commentCount(commentCount)
                .build();
    }

    /**
     * 게시글 상세 조회 DTO 변환기
     * - 상세 페이지에서 모든 정보 출력용
     *
     * @param post 게시글 Entity
     * @param likeCount 추천 수
     * @param viewCount 조회 수
     * @return 상세 응답 DTO
     */
    public PostDetailResponse toDetailResponse(PostEntity post, int likeCount, int viewCount, boolean hasLiked) {
        return PostDetailResponse.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent()) // HTML 포함된 본문
                .postCreate(post.getPostCreate())
                .postUpdate(post.getPostUpdate())
                .postStatus(post.getPostStatus())
                .boardId(post.getBoard().getBoardId())
                .category(post.getBoard().getBoardName())
                .userNickName(post.getUsersEntity().getUserNickName())
                .userId(post.getUsersEntity().getUserId())
                .likeCount(likeCount)
                .viewCount(viewCount)
                .hasLiked(hasLiked)
                .build();
    }

    /**
     * 기존 방식 호환용: 추천수/조회수 제외 (기본값 0)
     */
    public PostDetailResponse toDetailResponse(PostEntity post) {
        return toDetailResponse(post, 0, 0, false);
    }

    public PostDetailResponse toDetailResponse(PostEntity post, int likeCount, int viewCount) {
        return toDetailResponse(post, likeCount, viewCount, false);
    }
}
