package aba3.lucid.domain.company;

import aba3.lucid.domain.calendar.CalendarEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.company.enums.CompanyStatus;
import aba3.lucid.domain.product.ReviewCommentEntity;
import aba3.lucid.domain.schedule.RegularHolidayEntity;
import aba3.lucid.domain.schedule.TemporaryHolidayEntity;
import aba3.lucid.domain.schedule.WeekdaysScheduleEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Calendar;

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
    @Column(name = "cp_reg_no", columnDefinition = "CHAR(11)", nullable = false)
    private String cpRegNo;

    // 주소
    @Column(name = "cp_main_contact", columnDefinition = "CHAR(100)", nullable = false)
    private String cpMainContact;

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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private ReviewCommentEntity reviewComment;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "cal_id")
    private CalendarEntity calendarEntity;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "sns_id")
    private SocialEntity socialEntity;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "cp_country_id")
    private CompanyAlertEntity companyAlertEntity;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "rh_id")
    private RegularHolidayEntity regularHolidayEntity;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "th_id")h
    private TemporaryHolidayEntity temporaryHolidayEntity;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "ws_id")
    private WeekdaysScheduleEntity weekdaysScheduleEntity;



}
