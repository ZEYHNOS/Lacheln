package aba3.lucid.report.Controller;


import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.inquiry.dto.ReportImageRequest;
import aba3.lucid.domain.inquiry.dto.ReportImageResponse;
import aba3.lucid.domain.inquiry.dto.ReportRequest;
import aba3.lucid.domain.inquiry.dto.ReportResponse;
import aba3.lucid.report.Business.ReportBusiness;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportBusiness reportBusiness;


    @PostMapping("/company")
    public API<ReportResponse> reportUser(
            @AuthenticationPrincipal CustomUserDetails company,
            @RequestBody ReportRequest reportRequest
    ) {
        ReportResponse response = reportBusiness.reportUserByCompany(reportRequest,company);
        return API.OK(response);
    }

    @PostMapping("/user")
    public API<ReportResponse> reportCompany(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody ReportRequest reportRequest
    ) {
        log.info("reportTarget {}", reportRequest.getReportTarget());
        log.info("reportTitle {}", reportRequest.getReportTitle());
        ReportResponse response = reportBusiness.reportCompanyByUser(reportRequest,user);
        return API.OK(response);
    }


    @PostMapping("/user/image/upload")
    @Operation(summary = "유저 신고 이미지 업로드")
    public API<List<ReportImageResponse>> uploadUserImages(
            @RequestPart("images")  List<MultipartFile> images,
            @RequestPart("request") ReportImageRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws IOException {
        // userId, reportId 는 DTO 에 담겨있다고 가정
        String userId   = userDetails.getUserId();
        Long   reportId = request.getReportId();

        List<ReportImageResponse> url = reportBusiness.uploadUserReportImages(
                userId,
                reportId,
                images
        );
        return API.OK(url);
    }

    @PostMapping("/company/image/upload")
    @Operation(summary = "업체 신고 이미지 업로드")
    public API<List<ReportImageResponse>> uploadCompanyImages(
            @RequestPart("images")  List<MultipartFile> images,
            @RequestPart("request") ReportImageRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws IOException {
        Long   cpId     = userDetails.getCompanyId();
        Long   reportId = request.getReportId();

        List<ReportImageResponse> urls = reportBusiness.uploadCompanyReportImages(
                cpId,
                reportId,
                images
        );
        return API.OK(urls);
    }

    @GetMapping("/admin/unread/count")
    public API<Integer> countUnreadReports() {
        int count = reportBusiness.countUnreadReports();
        return API.OK(count);
    }

    @GetMapping("/admin/{reportId}")
    public API<ReportResponse> getReportDetail(@PathVariable Long reportId) {
        ReportResponse reportDetail = reportBusiness.getReportDetail(reportId);
        return API.OK(reportDetail);
    }

    @GetMapping("/admin")
    public API<List<ReportResponse>> getAllReports() {
        List<ReportResponse> responses = reportBusiness.getAllReports();
        return API.OK(responses);
    }




}
