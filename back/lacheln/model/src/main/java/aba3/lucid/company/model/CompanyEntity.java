package aba3.lucid.company.model;

import aba3.lucid.enums.CompanyStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cpId;

    private String email;

    private String password;

    private String name;

    private String repName;     // 대표자명

    private String regNo;       // 주민번호

    private String mainContact;     // 대표자 전화번호

    private String contack;     //업체 전화번호

    private String profile;

    private LocalDate expDate;     // 계약 만료일

    private String address;

    private String postalCode;  // 우편번호

    private String fax;

    private String bnRegNo;     // 사업자 등록 번호

    private String explain;     // 업체설명

    private String mos;     // 통신 판매업 신고 번호

    private LocalTime openTime;     // 영업 시작 시간

    private LocalTime closeTime;    // 영업 종료 시간

    private LocalDate joinAt;       // 가입 일시

    @Enumerated(EnumType.STRING)
    private CompanyStatus cpStatus;     // 상태

}
