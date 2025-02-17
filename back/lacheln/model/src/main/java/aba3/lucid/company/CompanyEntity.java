package aba3.lucid.company;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "company")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpId;

    // 회사 이메일
    private String email;

    // 회사 비밀번호
    private String password;

    // 업체명
    private String cpName;

    // 대표자명
    private String repName;

    // 대표자 전화번호
    private String regNo;

    // 주소
    private String mainContact;

    // 우편번호
    private String postalCode;

    // 사업자등록번호
    private String bnRegNo;

    // 통신판매업신고번호
    private String mos;

    // 입점 상태
    // TODO ENUM 처리
    private String cpStatus;

    // 업체 소개 이미지(대표 이미지)
    private String profile;

    // 업체 설명
    private String cpExplain;

    // 카테고리
    // todo enum
    private String cpCategory;

    // 업체 전화번호
    private String cpContact;

    // 팩스 번호
    private String fax;
}
