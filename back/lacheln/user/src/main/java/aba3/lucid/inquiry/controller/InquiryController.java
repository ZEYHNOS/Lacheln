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
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * InquiryController
 * - 문의 작성 및 조회에 대한 HTTP 요청을 처리하는 컨트롤러 클래스
 * - 사용자는 문의를 작성하고 본인이 작성한 문의 내역을 조회할 수 있음
 * - 관리자 응답 기능, 수정/삭제 기능은 제공하지 않음
 */
@Tag(name = "Inquiry Controller", description = "문의 작성 및 조회")
@RestController
@RequestMapping("/inquiry")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryBusiness inquiryBusiness;

    /**
     * [POST] /inquiry
     * 문의 작성 API
     *
     * - 사용자가 제목, 카테고리, 내용, 이미지 URL 리스트를 포함한 문의를 작성함
     * - 작성자는 현재 로그인한 사용자로 제한되며, userId를 통해 확인함
     * - 문의 상태는 기본적으로 'RECEIVED'로 저장됨
     * - 이미지 URL이 포함된 경우 별도의 InquiryImageEntity로 저장됨
     *
     * @param userId 로그인한 사용자 UUID (JWT 미적용 상태에서는 임시 파라미터)
     * @param request 문의 작성 요청 DTO (제목, 카테고리, 내용, 이미지 URL 리스트 포함)
     * @return API<Void> 201 Created 응답 반환 (실제 body는 없음)
     */
    @PostMapping("")
    @Operation(
            summary = "문의 작성",
            description = "로그인한 사용자가 새로운 문의를 작성합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "문의 작성 성공"),
                    @ApiResponse(responseCode = "400", description = "입력값 유효성 검증 실패 또는 사용자 정보 없음")
            }
    )
    public API<Void> createInquiry(
            @RequestParam String userId, // JWT 적용 전까지 임시 방식
            @Valid @RequestBody InquiryCreateRequest request
    ) {
        inquiryBusiness.createInquiry(userId, request);
        return API.<Void>OK();// body 없이 200 OK 응답
    }

    /**
     * [GET] /inquiry
     * 내 문의 목록 조회 API
     *
     * - 로그인한 사용자가 작성한 문의 목록을 모두 조회
     * - 단순히 InquiryEntity 목록을 InquiryResponse DTO로 변환하여 반환함
     * - 추후 페이징 적용 가능
     *
     * @param userId 로그인한 사용자 UUID (JWT 미적용 상태에서는 임시 파라미터)
     * @return API<List<InquiryResponse>> 사용자가 작성한 문의 응답 리스트
     */
    @GetMapping("")
    @Operation(
            summary = "내 문의 내역 조회",
            description = "로그인한 사용자가 본인이 작성한 문의 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "문의 내역 조회 성공")
            }
    )
    public API<List<InquiryResponse>> getMyInquiries(
            @RequestParam String userId // JWT 적용 전까지 임시 방식
    ) {
        List<InquiryResponse> response = inquiryBusiness.getMyInquiries(userId);
        return API.OK(response);
    }
}
