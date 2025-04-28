package aba3.lucid.payment.controller;

import aba3.lucid.payment.business.PaymentBusiness;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentBusiness paymentBusiness;

}
