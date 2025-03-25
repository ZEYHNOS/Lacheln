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
     * 게시글 작성 요청 → Entity 변환
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
     * 게시글 목록 응답 DTO 변환
     * UUID는 제외하고 닉네임만 포함
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
     * 게시글 상세 응답 DTO 변환
     * UUID는 제외하고 닉네임만 포함
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
                .userNickName(post.getUsersEntity().getUserNickName())
                .imageUrls(imageUrls)
                .build();
    }
}