package com.curve.controller;

import com.curve.domain.Merchant;
import com.curve.domain.MerchantBalance;
import com.curve.exception.NotFoundException;
import com.curve.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Component
@RestController
@RequestMapping("/merchants")
public class MerchantController {

    private MerchantService merchantService;

    @Autowired
    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @RequestMapping(value = "", method = POST)
    @ResponseBody
    public Merchant creatMerchant(@RequestBody Merchant merchant) {
        return merchantService.createMerchant(merchant);
    }

    @RequestMapping(value = "/{id}", method = GET)
    @ResponseBody
    public Merchant getMerchant(@PathVariable int id) throws NotFoundException {
        return merchantService.getMerchant(id);
    }

    @RequestMapping(value = "/{id}/balance", method = GET)
    @ResponseBody
    public MerchantBalance getMerchantBalance(@PathVariable int id) throws NotFoundException {
        return merchantService.getBalance(id);
    }

}
