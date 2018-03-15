package com.curve.controller;


import com.curve.domain.Payment;
import com.curve.domain.Transaction;
import com.curve.exception.InvalidRequestException;
import com.curve.exception.NotFoundException;
import com.curve.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Component
@RestController
@RequestMapping("/refunds")
public class RefundController {

    private PaymentService paymentService;

    @Autowired
    public RefundController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @RequestMapping(value = "", method = POST)
    @ResponseBody
    public Transaction refund(@RequestBody Payment payment) throws NotFoundException, InvalidRequestException {
        return paymentService.refund(payment);
    }


}
