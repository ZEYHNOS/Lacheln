package aba3.lucid.adjustment.Controller;


import aba3.lucid.adjustment.Business.AdjustmentBusiness;
import aba3.lucid.adjustment.Service.AdjustmentService;
import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.company.dto.AdjustmentRequest;
import aba3.lucid.domain.company.dto.AdjustmentResponse;
import aba3.lucid.domain.company.repository.AdjustmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class AdjustmentController {

    private final AdjustmentRepository adjustmentRepository;
    private final AdjustmentBusiness adjustmentBusiness;
    private final AdjustmentService adjustmentService;

    @PostMapping("/create")
    public API<AdjustmentResponse> createAdjustment(
            @AuthenticationPrincipal CustomUserDetails company,
            @RequestBody AdjustmentRequest request
   ){
       AdjustmentResponse response =adjustmentBusiness.createAdjustmentEntity(request, company.getCompanyId());
       return API.OK(response);

   }

   @PutMapping("/update")
    public API<AdjustmentResponse> updateAdjustment(
           @AuthenticationPrincipal CustomUserDetails company,
           @RequestBody AdjustmentRequest request

   ) {
        AdjustmentResponse response = adjustmentBusiness.updateAdjustment(request, company.getCompanyId());
        return API.OK(response);
   }


}
