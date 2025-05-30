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

@Converter
public class PostConvertor {

    /**
     * 게시글 작성 요청 DTO → 게시글 Entity 변환
     *
     * - 사용자가 게시글을 작성할 때 들어오는 PostRequest 객체를
     *   실제 DB에 저장할 수 있는 PostEntity로 변환한다.
     * - 게시판(BoardEntity)와 사용자(UsersEntity) 객체도 함께 주입받아 연관관계를 설정한다.
     *
     * @param request 게시글 작성 요청 DTO
     * @param board 게시판 Entity (게시글이 속한 게시판)
     * @param user 작성자 Entity
     * @return 변환된 게시글 Entity
     */
    public PostEntity toEntity(PostRequest request, BoardEntity board, UsersEntity user) {
        return PostEntity.builder()
                .postTitle(request.getPostTitle())
                .postContent(request.getPostContent())
                .board(board)
                .usersEntity(user)
                .postStatus(PostStatus.CREATED)
                .build();
    }

    /**
     * 게시글 목록 조회용 DTO 변환기
     *
     * - 게시글 목록에서는 간략한 정보(제목, 닉네임, 카테고리, 작성일 등)만 제공된다.
     * - 추천수(likeCount), 조회수(viewCount)도 포함되어야 하기 때문에 함께 전달받는다.
     *
     * @param post 게시글 Entity
     * @param likeCount 해당 게시글의 추천 수
     * @param viewCount 해당 게시글의 조회 수
     * @return 게시글 목록 응답 DTO
     */
    public PostListResponse toListResponse(PostEntity post, int likeCount, int viewCount) {
        return PostListResponse.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .userNickName(post.getUsersEntity().getUserNickName())
                .category(post.getBoard().getBoardName())
                .postCreate(post.getPostCreate())
                .likeCount(likeCount)
                .viewCount(viewCount)
                .build();
    }

    /**
     * 게시글 상세 조회 DTO 변환기 (추천수, 조회수 포함)
     *
     * - 게시글 상세 페이지에서는 제목, 내용, 작성자, 게시판, 이미지 리스트 등
     *   모든 정보를 클라이언트에 전달해야 한다.
     * - 추가로 추천수(likeCount)와 조회수(viewCount)도 포함된다.
     *
     * @param post 게시글 Entity
     * @param imageUrls 게시글에 첨부된 이미지 URL 리스트
     * @param likeCount 게시글 추천 수
     * @param viewCount 게시글 조회 수
     * @return 게시글 상세 응답 DTO
     */
    public PostDetailResponse toDetailResponse(PostEntity post, List<String> imageUrls, int likeCount, int viewCount) {
        return PostDetailResponse.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .postCreate(post.getPostCreate())
                .postUpdate(post.getPostUpdate())
                .postStatus(post.getPostStatus())
                .boardId(post.getBoard().getBoardId())
                .category(post.getBoard().getBoardName())
                .userNickName(post.getUsersEntity().getUserNickName())
                .imageUrls(imageUrls)
                .likeCount(likeCount)
                .viewCount(viewCount)
                .build();
    }

    /**
     * 게시글 상세 조회용 (기존 방식 호환용) 변환기 - 추천수/조회수 제외
     *
     * - 기존에 사용하던 2개 인자 기반 변환 메서드 유지용
     * - 추천수, 조회수는 기본값 0으로 처리
     *
     * @param post 게시글 Entity
     * @param imageUrls 이미지 URL 리스트
     * @return 게시글 상세 응답 DTO
     */
    public PostDetailResponse toDetailResponse(PostEntity post, List<String> imageUrls) {
        return toDetailResponse(post, imageUrls, 0, 0);
    }
}
