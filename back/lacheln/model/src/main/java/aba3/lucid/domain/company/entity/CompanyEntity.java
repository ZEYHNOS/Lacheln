package aba3.lucid.domain.company.entity;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.dto.CompanyUpdateRequest;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.company.enums.CompanyStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "company")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class CompanyEntity {

    @Id
    @Column(name = "cp_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpId;


    // 회사 이메일
    @Column(name = "cp_email", columnDefinition = "VARCHAR(255)", nullable = false)
    private String cpEmail;

    // 회사 비밀번호
    @Column(name = "cp_password", columnDefinition = "CHAR(255)", nullable = false)
    private String cpPassword;

    // 업체명
    @Column(name = "cp_name", columnDefinition = "VARCHAR(100)")
    private String cpName;

    // 대표자명
    @Column(name = "cp_rep_name", columnDefinition = "VARCHAR(20)", nullable = false)
    private String cpRepName;

    // 대표자 전화번호
    @Column(name = "cp_main_contact", columnDefinition = "CHAR(11)", nullable = false)
    private String cpMainContact;

    // 주소
    @Column(name = "cp_address", columnDefinition = "CHAR(100)", nullable = false)
    private String cpAddress;

    // 우편번호
    @Column(name = "cp_postal_code", columnDefinition = "CHAR(5)", nullable = false)
    private String cpPostalCode;

//     사업자등록번호
    @Column(name = "cp_bn_reg_no", columnDefinition = "CHAR(15)", nullable = false)
    @Pattern( regexp = "^\\d{3}-\\d{2}-\\d{5}$",
            message = "사업자등록번호 현식은 'xxx-xx-xxxx'로 입력해야 합니다.")
    private String cpBnRegNo;

    // 통신판매업신고번호
    @Column(name = "cp_mos", columnDefinition = "VARCHAR(20)", nullable = false)
    @Pattern(
            regexp = "^\\d{4}-[\\uAC00-\\uD7A3]+-\\d{5}$",
            message = "통신판매업신고번호 형식은 '2019-서울강남-01234'로 입력해야 합니다."
    )
    private String cpMos;

    // 입점 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "cp_status", columnDefinition = "CHAR(20)", nullable = false)
    private CompanyStatus cpStatus;

    // 업체 소개 이미지(대표 이미지)
    // TODO 기본값 설정

    @Column(name = "cp_profile", columnDefinition = "CHAR(255)", nullable = false)
    private String cpProfile;

    // 업체 설명
    @Column(name = "cp_explain", columnDefinition = "TEXT")
    private String cpExplain;

    // 카테고리
    @Enumerated(EnumType.STRING)
    @Column(name = "cp_category", columnDefinition = "CHAR(1)", nullable = true)
    private CompanyCategory cpCategory;

//     업체 전화번호
    @Column(name = "cp_contact", columnDefinition = "CHAR(11)")
    private String cpContact;

    // 팩스 번호
    @Column(name = "cp_fax", columnDefinition = "CHAR(20)")
    private String cpFax;

    @CreationTimestamp
    @Column(name = "company_join_date", nullable = false)
    private LocalDateTime companyJoinDate; //가입일

    // 업체 권한
    @Column(name = "company_role", nullable = false, columnDefinition = "CHAR(7)")
    private String cpRole = "COMPANY";

    public void updateCompany(CompanyUpdateRequest companyUpdateRequest) {
        updateCpAddress(companyUpdateRequest.getAddress());
        
    }

    public void updateCpAddress(String cpAddress) {
        if(cpAddress == null || cpAddress.trim().isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_PARAMETER, "주소는 필수 입력값입니다.");
        }
        this.cpAddress = cpAddress;


    }




//    public  void updateCpPassword(@Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해야 합니다.")
//                                  String cpPassword) {
//        if(cpPassword == null || cpPassword.trim().isEmpty()) {
//            throw new ApiException(ErrorCode.INVALID_PARAMETER, "비밀번호는 필수 입력값입니다.");
//        }
//        this.cpPassword = cpPassword;
//        ///확실하게 안 했음
//
//    }

//    private String hashPassword(String password) {
//        return password;
//    }


    // TODO 나중에 필요하면 하나씩 사용하기
//    // 리뷰 Mapping Table
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "review_id")
//    private ReviewCommentEntity reviewComment;
//
//    // 캘린더 Mapping Table
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "cal_id")
//    private CalendarEntity calendarEntity;
//
//    // 업체 SNS Mapping Table
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "sns_id")
//    private SocialEntity socialEntity;
//
//    // 업체 알림 Mapping table
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "cp_country_id")
//    private CompanyAlertEntity companyAlertEntity;
//
//    // 정규 휴무일 Mapping Table
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "rh_id")
//    private RegularHolidayEntity regularHolidayEntity;
//
//    // 임시 휴무일 Mapping Table
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "th_id")
//    private TemporaryHolidayEntity temporaryHolidayEntity;
//
//    // 요일별 일정 Mapping Table
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ws_id")
//    private WeekdaysScheduleEntity weekdaysScheduleEntity;
}
