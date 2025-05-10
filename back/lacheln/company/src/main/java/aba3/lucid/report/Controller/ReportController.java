package aba3.lucid.report.Controller;


import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.domain.inquiry.dto.ReportRequest;
import aba3.lucid.domain.inquiry.dto.ReportResponse;
import aba3.lucid.report.Business.ReportBusiness;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportBusiness reportBusiness;

    @PostMapping("/company/{companyId}")
    public API<ReportResponse> reportUser(
            @PathVariable Long companyId,
            @RequestBody ReportRequest reportRequest
    ) {
        ReportResponse response = reportBusiness.reportUserByCompany(reportRequest, AuthUtil.getCompanyId());
        return API.OK();
    }

    @PostMapping("/user/{userId}")
    public API<ReportResponse> reportCompany(
            @PathVariable String userId,
            @RequestBody ReportRequest reportRequest
    ) {
        ReportResponse response = reportBusiness.reportCompanyByUser(reportRequest,userId);
        return API.OK();
    }

}
