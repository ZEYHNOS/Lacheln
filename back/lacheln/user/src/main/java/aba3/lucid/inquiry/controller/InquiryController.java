package aba3.lucid.inquiry.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.domain.inquiry.dto.InquiryCreateRequest;
import aba3.lucid.domain.inquiry.dto.InquiryResponse;
import aba3.lucid.inquiry.business.InquiryBusiness;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * InquiryController
 * - 문의 작성 및 조회 API를 처리하는 진입 지점
 */
@Tag(name = "Inquiry Controller", description = "문의 작성 및 조회 API")
@RestController
@RequestMapping("/inquiry")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryBusiness inquiryBusiness;

    /**
     * 문의 작성 API
     * - JSON 요청을 통해 제목, 카테고리, 내용을 작성
     */
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "문의 작성",
            description = "로그인한 사용자가 제목, 카테고리, 내용을 포함하여 문의를 작성합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "문의 작성 성공"),
                    @ApiResponse(responseCode = "400", description = "입력값 오류"),
                    @ApiResponse(responseCode = "404", description = "사용자 없음")
            }
    )
    public API<InquiryResponse> createInquiry(@Valid @RequestBody InquiryCreateRequest request) {
        return API.OK(inquiryBusiness.createInquiry(request));
    }

    /**
     * 내 문의 목록 조회 API
     * - 로그인한 사용자가 작성한 모든 문의 내역을 조회합니다.
     * TODO 지금 스웨거 조회시 목록 조회가 아닌 상세 조회로 뜸 수정해야함
     */
    @GetMapping("")
    @Operation(
            summary = "내 문의 목록 조회",
            description = "로그인한 사용자가 작성한 모든 문의 내역을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공")
            }
    )
    public API<List<InquiryResponse>> getMyInquiries() {
        return API.OK(inquiryBusiness.getMyInquiries());
    }
}
