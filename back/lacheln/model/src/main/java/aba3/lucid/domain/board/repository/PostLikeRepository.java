package aba3.lucid.domain.board.repository;

import aba3.lucid.domain.board.entity.PostLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLikeEntity, Long> {

    /**
     * 사용자가 특정 게시글에 대해 이미 추천했는지 확인하는 메서드
     * - 중복 추천을 방지하기 위해 사용
     *
     * @param postId 게시글 ID
     * @param userId 사용자 ID
     * @return 이미 추천한 경우 true, 아니면 false
     */
    boolean existsByPostPostIdAndUsersUserId(Long postId, String userId);

    /**
     * 게시글의 총 추천 수를 계산하는 메서드
     *
     * @param postId 게시글 ID
     * @return 해당 게시글의 추천 수
     */
    long countByPostPostId(Long postId);
}


