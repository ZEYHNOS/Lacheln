package aba3.lucid.payment.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;

@Business
@RequiredArgsConstructor
public class PaymentBusiness {

    private final PaymentService paymentService;



}
