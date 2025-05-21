package aba3.lucid.inquiry.controller;

import aba3.lucid.domain.inquiry.dto.InquiryCreateRequest;
import aba3.lucid.domain.inquiry.dto.InquiryResponse;
import aba3.lucid.inquiry.service.InquiryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 문의 컨트롤러
 */
@RestController
@RequestMapping("/inquiry")
@RequiredArgsConstructor
@Tag(name = "Inquiry Controller", description = "문의 작성 및 조회")
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping
    @Operation(summary = "문의 작성", description = "사용자가 문의를 작성합니다.")
    public ResponseEntity<Void> createInquiry(
            @RequestHeader("X-USER-ID") String userId,
            @Valid @RequestBody InquiryCreateRequest request
    ) {
        inquiryService.createInquiry(userId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "내 문의 내역 조회", description = "로그인한 사용자가 보낸 문의 목록을 조회합니다.")
    public ResponseEntity<List<InquiryResponse>> getMyInquiries(
            @RequestHeader("X-USER-ID") String userId
    ) {
        List<InquiryResponse> response = inquiryService.getMyInquiries(userId);
        return ResponseEntity.ok(response);
    }
}
