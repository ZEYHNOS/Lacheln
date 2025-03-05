package aba3.lucid.domain.board;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "post_view")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostViewEntity {

    @Id
    @Column(name = "post_view_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postViewId;

    @Column(name = "post_id", nullable = false)
    private long post; //게시글ID

    @Column(name = "user_id", columnDefinition = "CHAR(36)", nullable = false)
    private String user; //소비자ID
}
