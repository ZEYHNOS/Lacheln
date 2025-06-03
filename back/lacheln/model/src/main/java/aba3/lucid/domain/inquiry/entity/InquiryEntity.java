package aba3.lucid.domain.inquiry.entity;

import aba3.lucid.domain.inquiry.enums.InquiryCategory;
import aba3.lucid.domain.inquiry.enums.InquiryStatus;
import aba3.lucid.domain.user.entity.UsersEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Table(name = "inquiry")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InquiryEntity {

    @Id
    @Column(name = "inquiry_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryId;

    @ManyToOne(fetch = FetchType.LAZY)
    private UsersEntity users;

    @Column(name = "inquiry_title", length = 100, nullable = false)
    private String inquiryTitle; //제목

    @Enumerated(EnumType.STRING)
    @Column(name = "inquiry_category", columnDefinition = "CHAR(30)", nullable = false)
    private InquiryCategory inquiryCategory;

    @Column(name = "inquiry_content", columnDefinition = "TEXT", nullable = false)
    private String inquiryContent; //내용

    @Enumerated(EnumType.STRING)
    @Column(name = "inquiry_status", columnDefinition = "CHAR(20)", nullable = false)
    private InquiryStatus inquiryStatus; //상태 접수, 진행중, 완료

    @Column(name = "inquiry_answer", length = 255)
    private String inquiryAnswer; //답변

    // 문의 작성일시를 저장하는 필드
    // - 사용자가 문의를 작성한 시간을 기록
    // - DB에 저장 시 자동으로 현재 시간이 입력됨 (@PrePersist 사용)
    @Column(name = "inquiry_created_at", nullable = false)
    private LocalDateTime inquiryCreatedAt;

    // 문의 생성 시 자동으로 inquiryCreatedAt에 현재 시간(LocalDateTime.now())을 저장하는 메서드
    // - Spring JPA의 @PrePersist 어노테이션을 사용하면
    //   엔티티가 저장되기 전에 이 메서드가 자동 호출됨
    @PrePersist
    protected void onCreate() {
        this.inquiryCreatedAt = LocalDateTime.now();
    }
}
