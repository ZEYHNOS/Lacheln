package aba3.lucid.report.Business;


import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.image.ImageType;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.CompanyRepository;
import aba3.lucid.domain.inquiry.converter.ReportConverter;
import aba3.lucid.domain.inquiry.dto.AdminReportResponse;
import aba3.lucid.domain.inquiry.dto.ReportImageResponse;
import aba3.lucid.domain.inquiry.dto.ReportRequest;
import aba3.lucid.domain.inquiry.dto.ReportResponse;
import aba3.lucid.domain.inquiry.entity.ReportEntity;
import aba3.lucid.domain.inquiry.entity.ReportImageEntity;
import aba3.lucid.domain.inquiry.enums.ReportStatus;
import aba3.lucid.domain.inquiry.repository.ReportRepository;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.repository.UsersRepository;
import aba3.lucid.image.service.ImageService;
import aba3.lucid.report.Service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Business
@RequiredArgsConstructor
@Component
public class ReportBusiness {

    private final CompanyRepository companyRepository;
    private final UsersRepository usersRepository;
    private final ReportService reportService;
    private final ReportConverter reportConverter;
    private final ImageService imageService;
    private final CompanyService companyService;
    private final ReportRepository reportRepository;

    //신고 건수(알림 뱃지용)
    public int countUnreadReports() {
        return reportRepository.countByReportStatus(ReportStatus.NEW);
    }

    //신고 상세 리스트
    public List<ReportResponse> getUnreadReports() {
        List<ReportEntity> reports = reportRepository.findAll();
        return reports.stream()
                .map(reportConverter::toReportResponse)
                .collect(Collectors.toList());
        }



    //신고 상세 조회 (ID로)
    public ReportResponse getReportDetail(Long reportId) {
        ReportEntity report = reportRepository.findById(reportId).orElseThrow(()
                -> new ApiException(ErrorCode.NOT_FOUND));
        ReportResponse detailedReport = reportConverter.toReportResponse(report);
        return detailedReport;
    }

    //관리자가 신고 상세/내용을 확인하면 status를 ‘CHECKED’로 변경
    public void checkReport(Long reportId) {
        ReportEntity report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
        report.setReportStatus(ReportStatus.NEW);
        reportRepository.save(report);
    }

    public List<ReportResponse> getAllReports() {
        List<ReportEntity> reports = reportRepository.findAll();
        return reports.stream()
                .map(reportConverter::toReportResponse)
                .collect(Collectors.toList());
    }


    //업체 -->> 유저를 신고하기
    public ReportResponse reportUserByCompany(ReportRequest req, CustomUserDetails company) {
        Validator.throwIfNull(req);
        String userId = req.getUserId();

        log.info("cpId : {}", userId);
        //신고(회사) 조회
        CompanyEntity reporter = companyService.findByIdWithThrow(company.getCompanyId());

        //신고 대상(유저) 조회
        UsersEntity reported = usersRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));


        ReportEntity entity = reportConverter.toReportEntity(
                req, reporter, reported
        );

        ReportEntity savedEntity = reportService.submitReport(entity);
        return reportConverter.toReportResponse(savedEntity);
    }

    //유저가 ->>> 업체를 신고하기
    public ReportResponse reportCompanyByUser(ReportRequest req, CustomUserDetails user) {
        Validator.throwIfNull(req);
        Long cpId = req.getCpId();

        log.info("cpId : {}", cpId);
        //신고 유저 조회
        UsersEntity reporter = usersRepository.findById(user.getUserId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));

        //신고 (대상) 업체 조회
        CompanyEntity reported = companyRepository.findById(cpId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));


        //entity 조립
        ReportEntity entity = reportConverter.toReportEntity(
                req, reported, reporter
        );
        ReportEntity savedEntity = reportService.submitReport(entity);
        return reportConverter.toReportResponse(savedEntity);
    }


    public List<ReportImageResponse> uploadCompanyReportImages( Long cpId,Long reportId,
    List<MultipartFile> images)
            throws IOException {

        Validator.throwIfInvalidId(reportId);
        Validator.throwIfInvalidId(cpId);
        if(images == null || images.isEmpty()) {
            throw  new ApiException(ErrorCode.NULL_POINT);
        }
        // --- 파일 저장 & URL 획득
        CompanyEntity company = companyService.findByIdWithThrow(cpId);
        ReportEntity report = reportService.findByIdWithThrow(reportId);

        List<String> urls = imageService.imagesUpload(
                company, images, ImageType.REPORT
        );
        List<ReportImageEntity> entities = reportConverter.toImageEntities(urls, report);
        List<ReportImageEntity> saved = reportService.saveImages(entities);
        return saved.stream()
                .map(reportConverter:: toImageResponse)
                .collect(Collectors.toList());

    }

    public List<ReportImageResponse> uploadUserReportImages(
            String userId, Long reportId, List<MultipartFile> images
    ) throws IOException {
        Validator.throwIfInvalidId(reportId);
        Validator.throwIfInvalidId(userId);
        if(images == null || images.isEmpty()) {
            throw  new ApiException(ErrorCode.NULL_POINT, "이미지가 없습니다");
        }

        UsersEntity user = usersRepository.findById(userId).
                orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));

        ReportEntity report = reportService.findByIdWithThrow(reportId);

        List<String> urls = imageService.imagesUpload(
                user,
                images,
                ImageType.REPORT   // 또는 ImageType.USER_REPORT 등
        );

        // --- URL → Entity, 연관관계 설정 & 저장 ---
        List<ReportImageEntity> entities = reportConverter.toImageEntities(urls, report);
        List<ReportImageEntity> saved   = reportService.saveImages(entities);

        // --- DTO 반환 ---
        return saved.stream()
                .map(reportConverter::toImageResponse)
                .collect(Collectors.toList());

    }

    public API<Long> getTodayNewReport(){
        long count = reportService.getTodayReportCount();
        return API.OK(count);
    }





}


