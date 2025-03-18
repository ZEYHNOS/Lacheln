package aba3.lucid.domain.board.entity;

import aba3.lucid.domain.board.enums.PostStatus;
import aba3.lucid.domain.user.entity.UsersEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Table(name = "post")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostEntity {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postId; //게시글ID

    // FETCH LAZY 달아야할려나 애매하네
    @ManyToOne(fetch = FetchType.LAZY)
    private UsersEntity usersEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    private BoardEntity board;

    @Column(name = "post_title", columnDefinition = "CHAR(100)", nullable = false)
    private String postTitle; //제목

    @Column(name = "post_content", columnDefinition = "LONGTEXT", nullable = false)
    private String postContent; // 내용

    // 최초 저장시 현재 시간으로 저장
    @CreationTimestamp
    @Column(name = "post_create", nullable = false)
    private LocalDateTime postCreate; //작성일

    @CreationTimestamp
    @Column(name = "post_update", nullable = false)
    private LocalDateTime postUpdate; //수정일

    @Enumerated(EnumType.STRING)
    @Column(name = "post_status", columnDefinition = "CHAR(20)", nullable = false)
    private PostStatus postStatus; //상태 enum 참고

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostViewEntity> postViewList;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostLikeEntity> postLikeList;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<CommentEntity> commentList;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImageEntity> postImageList;
}
