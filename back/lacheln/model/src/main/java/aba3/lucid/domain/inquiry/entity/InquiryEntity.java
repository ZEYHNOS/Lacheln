package aba3.lucid.domain.inquiry.entity;

import aba3.lucid.domain.user.entity.UsersEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@Table(name = "inquiry")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InquiryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryId;

    @ManyToOne(fetch = FetchType.LAZY)
    private UsersEntity users;

    @Column(name = "inquiry_title", length = 100, nullable = false)
    private String inquiryTitle; //제목

    // TODO 문의 카테고리 나중에 정해야 할 듯
    @Column(name = "inquiry_category", columnDefinition = "CHAR(50)", nullable = false)
    private String inquiryCategory; //카테고리

    @Column(name = "inquiry_content", columnDefinition = "TEXT", nullable = false)
    private String inquiryContent; //내용

    @Lob
    @Column(name = "inquiry_file", columnDefinition = "LONGBLOB")
    private byte[] inquiryFile; //파일

    @Enumerated(EnumType.STRING)
    @Column(name = "inquiry_status", columnDefinition = "CHAR(20)", nullable = false)
    private String inquiryStatus; //상태 접수, 진행중, 완료

    @Column(name = "inquiry_answer", length = 255)
    private String inquiryAnswer; //답변

    @JsonIgnore
    @OneToMany(mappedBy = "inquiry", cascade = CascadeType.REMOVE)
    private List<InquiryImageEntity> inquiryImageList;
}
