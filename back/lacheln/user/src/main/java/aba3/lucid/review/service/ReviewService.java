package aba3.lucid.review.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.entity.PayManagementEntity;
import aba3.lucid.domain.payment.repository.PayManagementRepository;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.enums.ReviewStatus;
import aba3.lucid.domain.review.entity.ReviewEntity;
import aba3.lucid.domain.review.entity.ReviewImageEntity;
import aba3.lucid.domain.review.repository.ReviewImageRepository;
import aba3.lucid.domain.review.repository.ReviewRepository;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.payment.service.PayDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final PayDetailService payDetailService;
    private final ReviewRepository reviewRepository;

    // 리뷰 생성(리뷰 작성 상태 X)
    public void create(List<PayDetailEntity> payDetailEntityList) {
        List<ReviewEntity> reviewEntityList = new ArrayList<>();

        payDetailEntityList.forEach(
                it -> {
                    reviewEntityList.add(ReviewEntity.builder()
                            .payDetailEntity(it)
                            .rvStatus(ReviewStatus.REPLY_NEEDED)
                            .build())
                    ;
                }
        );

        reviewRepository.saveAll(reviewEntityList);
    }

    // 리뷰 작성(사용자가 리뷰 작성하기) -> 답글 생성하기
    public void writeReview() {

    }

    // 리뷰 수정(하는건가?)

    // 리뷰 삭제(리뷰 삭제하기)
    @Transactional
    public void delete() {

    }


}