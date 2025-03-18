package aba3.lucid.domain.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "post_image")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PostImageEntity {

    @Id
    @Column(name = "post_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postImageId; //게시글 이미지 PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post; //게시글 ID

    @Column(name = "post_image_url", length = 255, nullable = false)
    private String postImageUrl; //게시글 첨부 이미지 URL
}
