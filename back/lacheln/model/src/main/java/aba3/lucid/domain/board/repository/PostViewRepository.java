package aba3.lucid.domain.board.repository;

import aba3.lucid.domain.board.entity.PostViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostViewRepository extends JpaRepository<PostViewEntity, Long> {

    /**
     * 특정 게시글의 전체 조회수를 계산하는 메서드
     * - 게시글 ID를 기준으로 PostViewEntity 개수를 세서 조회수를 반환
     *
     * @param postId 조회수를 알고 싶은 게시글 ID
     * @return 해당 게시글의 조회수 (PostViewEntity 개수)
     */
    long countByPostPostId(Long postId);

    /**
     * 특정 사용자가 해당 게시글을 이미 조회했는지 확인하는 메서드
     * - 중복 조회 방지 로직에 활용 가능 (현재는 사용 안 함)
     *
     * @param postId 게시글 ID
     * @param userId 사용자 ID
     * @return 이미 조회했다면 true, 아니면 false
     */
    boolean existsByPostPostIdAndUsersUserId(Long postId, String userId);
}
