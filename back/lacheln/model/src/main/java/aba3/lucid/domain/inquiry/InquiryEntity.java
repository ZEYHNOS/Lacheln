package aba3.lucid.domain.inquiry;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "user_id", columnDefinition = "CHAR(36)", nullable = false)
    private String user; //소비자ID 이메일+비밀번호+비밀키(암호화시켜서저장)

    @Column(name = "inquiry_title", length = 100, nullable = false)
    private String inquiryTitle; //제목

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
}
