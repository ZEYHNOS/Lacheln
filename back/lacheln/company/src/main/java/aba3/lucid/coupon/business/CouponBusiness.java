package aba3.lucid.coupon.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.coupon.service.CouponBoxService;
import aba3.lucid.coupon.service.CouponService;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.coupon.convertor.CouponConvertor;
import aba3.lucid.domain.coupon.dto.CouponRequest;
import aba3.lucid.domain.coupon.dto.CouponResponse;
import aba3.lucid.domain.coupon.dto.CouponVerifyRequest;
import aba3.lucid.domain.coupon.entity.CouponBoxEntity;
import aba3.lucid.domain.coupon.entity.CouponEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.product.service.ProductService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Business
@RequiredArgsConstructor
public class CouponBusiness {

    private final CouponService couponService;
    private final CompanyService companyService;
    private final ProductService productService;
    private final CouponBoxService couponBoxService;

    private final CouponConvertor couponConvertor;

    private final RabbitTemplate rabbitTemplate;

    // 쿠폰 등록
    public CouponResponse issueCouponForCompany(Long companyId, CouponRequest couponRequest) {
        Validator.throwIfNull(couponRequest);
        Validator.throwIfInvalidId(companyId);

        // TODO 업체에서 쿠폰 발급 횟수 정할건지

        // DTO -> Entity
        CompanyEntity company = companyService.findByIdWithThrow(companyId);

        CouponEntity coupon = couponConvertor.toEntity(couponRequest, company);


        CouponEntity newCoupon = couponService.issueCouponForCompany(coupon);

        return couponConvertor.toResponse(newCoupon);
    }


    // 유저가 쿠폰 받기
    public void claimCoupon(String userId, String couponId) {
        Validator.throwIfNull(userId, couponId);

        CouponEntity coupon = couponService.findByIdWithThrow(couponId);

        couponBoxService.claimCoupon(userId, coupon);
    }


    // 유저가 쿠폰 사용
    public void useCoupon(String userId, Long couponBoxId) {
        Validator.throwIfNull(userId, couponBoxId);

        CouponBoxEntity couponBox = couponBoxService.findByIdWithThrow(couponBoxId);

        couponBoxService.consumeCoupon(couponBox);
    }


    // 쿠폰 삭제
    public void deleteCoupon(String couponId, Long companyId) {
        Validator.throwIfInvalidId(companyId);
        Validator.throwIfNull(couponId);

        // 삭제를 요청한 업체의 쿠폰인지 확인
        CouponEntity coupon = couponService.findByIdWithThrow(couponId);
        CompanyEntity company = companyService.findByIdWithThrow(companyId);
        couponService.validateCompanyCouponOwnership(company, coupon);

        couponService.deleteCouponById(coupon);
    }


    // 쿠폰 수정
    public CouponResponse updateCoupon(CouponRequest request, Long companyId, Long productId) {
        Validator.throwIfNull(request);
        Validator.throwIfInvalidId(companyId, productId);

        // 업체 Entity 가지고 오기
        CompanyEntity company = companyService.findByIdWithThrow(companyId);

        // DTO -> Entity
        CouponEntity coupon = couponConvertor.toEntity(request, company);
        couponService.validateCompanyCouponOwnership(company, coupon);

        // Entity -> DTO
        CouponEntity newCouponEntity = couponService.updateCoupon(coupon);
        return couponConvertor.toResponse(newCouponEntity);
    }

    // 사용자가 결제 전 쿠폰 유효성 확인하기
    // RabbitMQ 연결하기 RPC 스타일로
    @RabbitListener(queues = "to.company")
    public void couponVerify(Message message, Channel channel) throws IOException {
        Validator.throwIfNull(message);

        String replyTo = message.getMessageProperties().getReplyTo();
        String correlationId = message.getMessageProperties().getCorrelationId();


        CouponVerifyRequest request = (CouponVerifyRequest) rabbitTemplate.getMessageConverter().fromMessage(message);


        if (request.getCouponBoxIdList().isEmpty()) {
            // TODO 응답값 넣기
            return;
        }

        String userId = request.getUserId();
        List<CouponBoxEntity> couponBoxEntityList = couponBoxService.findAllById(request.getCouponBoxIdList());
        List<ProductEntity> productEntityList = productService.findAllById(request.getProductIdList());

        couponService.verificationBeforePayment(userId, couponBoxEntityList, productEntityList);

        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        // 응답 메시지 속성 설정
        MessageProperties replyProps = new MessageProperties();
        replyProps.setCorrelationId(correlationId);

        // 응답 메시지 생성
        Message replyMessage = rabbitTemplate.getMessageConverter().toMessage(request, replyProps);

        // 응답 메시지 전송
        rabbitTemplate.send(replyTo, replyMessage);
        channel.basicAck(deliveryTag, false);
        log.info("rpc rabbitmq end");
    }



}
