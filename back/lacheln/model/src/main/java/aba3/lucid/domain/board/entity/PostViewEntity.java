package aba3.lucid.domain.board.entity;

import aba3.lucid.domain.user.entity.UsersEntity;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    private UsersEntity users;
}
