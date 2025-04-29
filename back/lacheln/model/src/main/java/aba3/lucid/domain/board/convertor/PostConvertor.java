package aba3.lucid.domain.board.convertor;

import aba3.lucid.domain.board.dto.PostDetailResponse;
import aba3.lucid.domain.board.dto.PostListResponse;
import aba3.lucid.domain.board.dto.PostRequest;
import aba3.lucid.domain.board.entity.BoardEntity;
import aba3.lucid.domain.board.entity.PostEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.board.enums.PostStatus;
import org.springframework.stereotype.Component;

@Component
public class PostConvertor {

    public PostEntity toEntity(PostRequest request, BoardEntity board, UsersEntity user) {
        return PostEntity.builder()
                .postTitle(request.getPostTitle())
                .postContent(request.getPostContent())
                .board(board)
                .usersEntity(user)
                .postStatus(PostStatus.CREATED)
                .build();
    }

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

    public PostDetailResponse toDetailResponse(PostEntity post, int likeCount, int viewCount) {
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
                .likeCount(likeCount)
                .viewCount(viewCount)
                .build();
    }
}