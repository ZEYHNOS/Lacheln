package aba3.lucid.report.Business;


import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.CompanyRepository;
import aba3.lucid.domain.inquiry.convertor.ReportConvertor;
import aba3.lucid.domain.inquiry.dto.ReportRequest;
import aba3.lucid.domain.inquiry.dto.ReportResponse;
import aba3.lucid.domain.inquiry.entity.ReportEntity;
import aba3.lucid.domain.inquiry.enums.ReportCategory;
import aba3.lucid.domain.inquiry.repository.ReportRepository;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.repository.UsersRepository;
import aba3.lucid.report.Service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Business
@RequiredArgsConstructor
@Component
public class ReportBusiness {

    private final CompanyRepository companyRepository;
    private final UsersRepository usersRepository;
    private final ReportRepository reportRepository;
    private final ReportService reportService;
    private final ReportConvertor reportConvertor;


    public ReportResponse report(ReportRequest request, AuthUtil authUtil) {
        Validator.throwIfNull(request);
        switch (request.getReportTarget()) {
            case "USER":
                return reportUserByCompany(request, AuthUtil.getCompanyId());
            case "COMPANY":
                return  reportCompanyByUser(request,AuthUtil.getUserId());
            default:
                throw new ApiException(ErrorCode.INVALID_PARAMETER);
        }
    }

    //업체 유저를 신고하기
    public ReportResponse reportUserByCompany(ReportRequest req, Long cpId) {
        Validator.throwIfNull(req);
        Validator.throwIfInvalidId(cpId);
        String userId = req.getUserId();
        //신고(회사) 조회
        CompanyEntity reporter = companyRepository.findById(cpId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));

        //신고 대상(유저) 조회
        UsersEntity reported = usersRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));

        //제한 카테고리 검사
        if(req.getReportCategory() == ReportCategory.RESTRICTED) {
            throw  new ApiException(ErrorCode.RESTRICTED_CONTENT);
        }

        //자기 자신을 신고할 수 없다
        reportService.validateSelfReport(String.valueOf(cpId),userId);

        ReportEntity entity = reportConvertor.toReportEntity(
                req, reporter, reported
        );

        ReportEntity savedEntity = reportService.submitReport(entity);
        return reportConvertor.toReportResponse(savedEntity);
    }

    //유저가 업체를 신고하기
    public ReportResponse reportCompanyByUser(ReportRequest req, String userId) {
        Validator.throwIfNull(req);
        Validator.throwIfInvalidId(Long.valueOf(userId));
        Long cpId = req.getCpId();


        //신고 유저 조회
        UsersEntity reporter = usersRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));

        //신고 (대상) 업체 조회
        CompanyEntity reported = companyRepository.findById(cpId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));

        //자기 자신을 신고할 수 없다
        reportService.validateSelfReport(String.valueOf(cpId),userId);


        //제한 카테고리 검사
        if(req.getReportCategory() == ReportCategory.RESTRICTED) {
            throw  new ApiException(ErrorCode.RESTRICTED_CONTENT);
        }

        //entity 조립
        ReportEntity entity = reportConvertor.toReportEntity(
                req, reported, reporter
        );
        ReportEntity savedEntity = reportService.submitReport(entity);
        return reportConvertor.toReportResponse(savedEntity);
    }



}


