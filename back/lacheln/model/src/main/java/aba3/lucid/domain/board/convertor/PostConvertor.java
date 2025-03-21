package aba3.lucid.domain.board.convertor;

import aba3.lucid.domain.board.dto.PostRequest;
import aba3.lucid.domain.board.dto.PostResponse;
import aba3.lucid.domain.board.entity.BoardEntity;
import aba3.lucid.domain.board.entity.PostEntity;
import aba3.lucid.domain.board.enums.PostStatus;
import aba3.lucid.domain.user.entity.UsersEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostConvertor {

    /**
     * PostRequest -> PostEntity 변환
     * 사용자가 보낸 게시글 작성 요청 데이터를 DB 저장용 Entity로 변환
     * @param request 사용자의 게시글 작성 요청 DTO
     * @param board 게시판 엔티티(어떤 게시판에 글 쓰는지)
     * @param user 사용자 엔티티 (글 작성자 번호)
     * @return 저장 가능한 PostEntity
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
     * PostEntity -> PostResponse 변환
     * DB에서 조회한 게시글 엔티티를 클라이언트에 응답할 DTO로 변환
     * @param entity 게시글 엔티티
     * @param imageUrls 게시글에 첨부된 이미지 URL 리스트
     * @return 클라이언트에 응답할 DTO
     */
    public PostResponse toResponse(PostEntity entity, List<String> imageUrls) {
        return PostResponse.builder()
                .postId(entity.getPostId())
                .postTitle(entity.getPostTitle())
                .postContent(entity.getPostContent())
                .postCreate(entity.getPostCreate())
                .postUpdate(entity.getPostUpdate())
                .postStatus(entity.getPostStatus())
                .BoardId(entity.getBoard().getBoardId())
                .category(entity.getBoard().getBoardName())
                .userId(entity.getUsersEntity().getUserId())
                .imageUrls(imageUrls)
                .build();
    }
}
