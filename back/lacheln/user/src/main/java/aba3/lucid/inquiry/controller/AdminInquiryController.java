package aba3.lucid.inquiry.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.domain.inquiry.converter.InquiryConvertor;
import aba3.lucid.domain.inquiry.dto.InquiryAnswerRequest;
import aba3.lucid.domain.inquiry.dto.InquiryDetailResponse;
import aba3.lucid.domain.inquiry.dto.InquiryListResponse;
import aba3.lucid.domain.inquiry.entity.InquiryEntity;
import aba3.lucid.domain.inquiry.enums.InquiryStatus;
import aba3.lucid.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inquiry/admin")
@RequiredArgsConstructor
public class AdminInquiryController {

    private final InquiryService inquiryService;

    // ✅ 전체 목록 조회
    @GetMapping("/list")
    public API<List<InquiryListResponse>> getAll() {
        List<InquiryEntity> list = inquiryService.findAllInquiries();
        return API.OK(list.stream()
                .map(InquiryConvertor::toListResponseForAdmin)
                .toList());
    }

    // ✅ 상세 조회
    @GetMapping("/{inquiryId}")
    public API<InquiryDetailResponse> getDetail(@PathVariable Long inquiryId) {
        InquiryEntity inquiry = inquiryService.findByIdWithThrow(inquiryId);
        return API.OK(InquiryConvertor.toResponse(inquiry));
    }

    // ✅ 답변 등록
    @PostMapping("/{inquiryId}/answer")
    public API<Void> answer(@PathVariable Long inquiryId,
                            @RequestBody InquiryAnswerRequest request) {
        inquiryService.answerInquiry(inquiryId, request.getAnswer());
        return API.<Void>OK(); // ✅ Ambiguous 오류 해결
    }

    // ✅ 진행중 문의 개수 (대시보드)
    @GetMapping("/count-in-progress")
    public API<Integer> getCountInProgress() {
        return API.OK(inquiryService.countByStatus(InquiryStatus.IN_PROGRESS));
    }
}