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

    //소비자ID 이메일+비밀번호+비밀키(암호화시켜서저장)
    private String userId;

    //제목
    private String inquiryHead;

    //카테고리
    private String inquiryCategory;

    //내용
    private String inquiryContent;

    //파일
    private byte[] inquiryFile;

    //상태 접수, 진행중, 완료
    private String inquiryStatus;

    //답변
    private String Field;
}
