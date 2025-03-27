package aba3.lucid.domain.board.repository;

import aba3.lucid.domain.board.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    /**
     * 특정 게시글에 작성된 모든 댓글을 작성 시간 순으로 조회
     *
     * - 부모 댓글과 대댓글을 구분하지 않고 모두 조회
     * - Controller/Service 단에서 계층 구조(부모-자식)를 조립할 때 사용
     *
     * @param postId 댓글이 달린 게시글의 ID
     * @return CommentEntity 리스트 (작성 시간 순 정렬)
     */
    List<CommentEntity> findByPost_PostIdOrderByCmtCreateAsc(Long postId);
}
