package com.curve.controller;

import com.curve.domain.EarMarked;
import com.curve.exception.NotFoundException;
import com.curve.service.EarMarkedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Component
@RestController
@RequestMapping("/earmarked")
public class EarMarkedController {

    private EarMarkedService earMarkedService;

    @Autowired
    public EarMarkedController(EarMarkedService earMarkedService) {
        this.earMarkedService = earMarkedService;
    }

    @RequestMapping(value = "/cardId/{cardId}/merchant/{merchantId}", method = GET)
    @ResponseBody
    public EarMarked getMerchant(@PathVariable int cardId, @PathVariable int merchantId) throws NotFoundException {
        return earMarkedService.getEarMarkedBalance(cardId, merchantId);
    }

}
