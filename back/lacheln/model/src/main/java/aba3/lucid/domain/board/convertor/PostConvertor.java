package aba3.lucid.domain.board.convertor;

import aba3.lucid.domain.board.dto.*;
import aba3.lucid.domain.board.entity.BoardEntity;
import aba3.lucid.domain.board.entity.PostEntity;
import aba3.lucid.domain.board.enums.PostStatus;
import aba3.lucid.domain.user.entity.UsersEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostConvertor {

    /**
     * 게시글 작성 요청(PostRequest) → DB 저장용 Entity로 변환
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
     * 게시글 목록 조회용 변환 (이미지 제외)
     */
    public PostListResponse toListResponse(PostEntity post, int likeCount, int viewCount) {
        return PostListResponse.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .userId(post.getUsersEntity().getUserId())
                .category(post.getBoard().getBoardName())
                .postCreate(post.getPostCreate())
                .likeCount(likeCount)
                .viewCount(viewCount)
                .build();
    }

    /**
     * 게시글 상세 조회용 변환 (이미지 포함)
     */
    public PostDetailResponse toDetailResponse(PostEntity post, List<String> imageUrls) {
        return PostDetailResponse.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .postCreate(post.getPostCreate())
                .postUpdate(post.getPostUpdate())
                .postStatus(post.getPostStatus())
                .boardId(post.getBoard().getBoardId())
                .category(post.getBoard().getBoardName())
                .userId(post.getUsersEntity().getUserId())
                .imageUrls(imageUrls)
                .build();
    }
}