package aba3.lucid.domain.board.entity;

import aba3.lucid.domain.board.enums.CommentStatus;
import aba3.lucid.domain.user.entity.UsersEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "comment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cmt_id")
    private Long cmtId;

    /**
     * 부모 댓글 (null이면 일반 댓글)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_cmt_id")
    private CommentEntity parent;

    /**
     * 자식 댓글들 (대댓글)
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CommentEntity> children = new ArrayList<>();

    /**
     * 댓글이 속한 게시글
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    /**
     * 댓글 작성자
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UsersEntity users;

    /**
     * 댓글 내용
     */
    @Column(name = "cmt_content", columnDefinition = "VARCHAR(100)", nullable = false)
    private String cmtContent;

    /**
     * 작성 시간
     */
    @CreationTimestamp
    @Column(name = "cmt_create", nullable = false)
    private LocalDateTime cmtCreate;

    /**
     * 마지막 수정 시간
     */
    @UpdateTimestamp
    @Column(name = "cmt_update", nullable = false)
    private LocalDateTime cmtUpdate;

    /**
     * 댓글 상태 (CREATED, DELETED 등)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "cmt_status", length = 20, nullable = false)
    private CommentStatus cmtStatus;

    /**
     * 댓글 차수 (댓글: 1, 답글: 2~4)
     */
    @Range(min = 1, max = 4)
    @Column(name = "cmt_degree", nullable = false)
    private int cmtDegree;

    /**
     * Soft Delete 처리 (본인 + 모든 자식 댓글까지 재귀 삭제)
     */
    public void deleteWithChildren() {
        this.cmtStatus = CommentStatus.DELETED;

        if (children != null) {
            for (CommentEntity child : children) {
                child.deleteWithChildren();
            }
        }
    }

    /**
     * 본인만 Soft Delete 처리
     */
    public void deleteOnlySelf() {
        this.cmtStatus = CommentStatus.DELETED;
    }
}
