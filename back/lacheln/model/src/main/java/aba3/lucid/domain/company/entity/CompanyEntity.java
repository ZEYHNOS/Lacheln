package aba3.lucid.domain.company.entity;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.password.PasswordEncoder;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.dto.CompanyRequest;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.company.enums.CompanyStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Getter
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

    // 사업자등록번호
    @Column(name = "cp_bn_reg_no", columnDefinition = "CHAR(10)", nullable = false)
    private String cpBnRegNo;

    // 통신판매업신고번호
    @Column(name = "cp_mos", columnDefinition = "VARCHAR(20)", nullable = false)
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
    @Column(name = "cp_category", columnDefinition = "CHAR(1)", nullable = false)
    private CompanyCategory cpCategory;

    // 업체 전화번호
    @Column(name = "cp_contact", columnDefinition = "CHAR(11)")
    private String cpContact;

    // 팩스 번호
    @Column(name = "cp_fax", columnDefinition = "CHAR(20)")
    private String cpFax;

    // 업체 권한
    @Column(name = "company_role", nullable = false, columnDefinition = "CHAR(7)")
    private String cpRole = "COMPANY";

    public void updateCompanyRequest(CompanyRequest request) {
        updateCpPassword(request.getCpPassword());
        updateCpMainContact(request.getCpMainContact());
        updateCpAddress(request.getCpAddress());
        
    }

    public void updateCpAddress(String cpAddress) {
        if(cpAddress == null || cpAddress.trim().isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_PARAMETER, "주소는 필수 입력값입니다.");
        }
        this.cpAddress = cpAddress;


    }

    public void updateCpMainContact(@NotBlank(message = "대표자 전화번호는 필수 입력값입니다.") String cpMainContact) {
        if(!cpMainContact.matches("^\\d{10,11}$")) {
            throw new ApiException(ErrorCode.INVALID_PARAMETER, " 대표자 전화번호는 10~11자리 숫자만 가능합니다.");
        }
        this.cpMainContact = cpMainContact;
        
    }



    public  void updateCpPassword(@Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해야 합니다.")
                                  String cpPassword) {
        if(cpPassword == null || cpPassword.trim().isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_PARAMETER, "비밀번호는 필수 입력값입니다.");
        }
        String hashedPassword = hashPassword(cpPassword);
        this.cpPassword = hashedPassword;
        ///확실하게 안 했음
        
    }

    private String hashPassword(String password) {
        return password;
    }


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
