package aba3.lucid.domain.review.dto;

public record ReviewSearchRequest(
        String userId,
        Long companyId,
        Long productId
) {
}
