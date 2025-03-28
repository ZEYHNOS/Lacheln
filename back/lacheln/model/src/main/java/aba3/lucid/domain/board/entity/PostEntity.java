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
    private Long postId; // 게시글 ID

    @ManyToOne(fetch = FetchType.LAZY)
    private UsersEntity usersEntity; // 작성자 정보 (회원)

    @ManyToOne(fetch = FetchType.LAZY)
    private BoardEntity board; // 게시판 정보

    @Column(name = "post_title", columnDefinition = "CHAR(100)", nullable = false)
    private String postTitle; // 게시글 제목

    @Column(name = "post_content", columnDefinition = "LONGTEXT", nullable = false)
    private String postContent; // 게시글 내용

    @CreationTimestamp
    @Column(name = "post_create", nullable = false)
    private LocalDateTime postCreate; // 작성일 (최초 저장 시간)

    @CreationTimestamp
    @Column(name = "post_update", nullable = false)
    private LocalDateTime postUpdate; // 수정일 (갱신 시간)

    @Enumerated(EnumType.STRING)
    @Column(name = "post_status", columnDefinition = "CHAR(20)", nullable = false)
    private PostStatus postStatus; // 게시글 상태 (예: CREATED, DELETED 등)

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostViewEntity> postViewList; // 게시글 조회 기록 연관관계

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLikeEntity> postLikeList; // 게시글 좋아요 연관관계

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> commentList; // 게시글 댓글 연관관계

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImageEntity> postImageList; // 게시글 이미지 연관관계

    // 게시글 수정 메서드 (제목, 내용, 수정일 변경)
    public void updatePost(String newTitle, String newContent, LocalDateTime updateTime) {
        this.postTitle = newTitle;
        this.postContent = newContent;
        this.postUpdate = updateTime;
    }

    // 논리 삭제를 위한 플래그
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false; // true면 삭제된 게시글로 간주

    // 논리 삭제 처리 메서드 (deleted = true로 변경)
    public void delete() {
        this.deleted = true;
    }

    // 외부에서 삭제 여부 확인용 getter
    public boolean isDeleted() {
        return this.deleted;
    }
}