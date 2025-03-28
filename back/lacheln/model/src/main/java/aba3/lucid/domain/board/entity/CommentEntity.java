package aba3.lucid.domain.board.entity;

import aba3.lucid.domain.board.enums.CommentStatus;
import aba3.lucid.domain.user.entity.UsersEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Table(name = "comment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentEntity {

    @Id
    @Column(name = "cmt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cmtId;

    // 부모 댓글을 가리키는 자기참조 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_cmt_id") // 자식 댓글에서 부모 댓글을 가리키는 외래 키
    private CommentEntity parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<CommentEntity> children; // 자식 댓글들 (부모 댓글에 속하는 댓글들)

    @ManyToOne(fetch = FetchType.LAZY)
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    private UsersEntity users;

    @Column(name = "cmt_content", columnDefinition = "CHAR(100)", nullable = false)
    private String cmtContent; //내용

    @CreationTimestamp
    @Column(name = "cmt_create", nullable = false)
    private LocalDateTime cmtCreate; //작성일

    @CreationTimestamp
    @Column(name = "cmt_update", nullable = false)
    private LocalDateTime cmtUpdate; //수정일

    @Enumerated(EnumType.STRING)
    @Column(name = "cmt_status", columnDefinition = "CHAR(20)", nullable = false)
    private CommentStatus cmtStatus; //CommentStatus 참고

    @Range(min = 1, max = 4)
    @Column(name = "cmt_degree", nullable = false)
    private int cmtDegree; //차수 1~4

    // 댓글 삭제
    public void delete() {
        this.cmtStatus = CommentStatus.DELETED;
    }
}
