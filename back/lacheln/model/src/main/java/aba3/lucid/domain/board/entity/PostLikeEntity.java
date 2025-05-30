package aba3.lucid.domain.board.entity;

import aba3.lucid.domain.user.entity.UsersEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "post_like")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostLikeEntity {

    @Id
    @Column(name = "post_like_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postLikeId; // 좋아요 PK

    @ManyToOne
    private PostEntity post; // 좋아요 누른 게시글 ID

    @ManyToOne
    private UsersEntity users; // 좋아요 누른 유저 ID
}
