package aba3.lucid.domain.board;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "postlike")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostLikeEntity {

    @Id
    @Column(name = "like_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long likeId;

    @Column(name = "post_id", nullable = false)
    private long postId; //게시글ID

    @Column(name = "user_id", columnDefinition = "CHAR(36)", nullable = false)
    private String userId; //유저ID
}
