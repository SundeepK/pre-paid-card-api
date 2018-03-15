package com.curve.controller;

import com.curve.domain.*;
import com.curve.exception.CardAlreadyExistsException;
import com.curve.exception.InvalidRequestException;
import com.curve.exception.NotFoundException;
import com.curve.service.CardService;
import com.curve.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Component
@RestController
@RequestMapping("/cards")
public class CardController {

    private static final String EPOCH_MILLIS = "epochMillis";
    public static final String TOTAL_PAGES_HEADER = "total-pages";
    public static final String TOTAL_ITEMS_HEADER = "total";
    private CardService cardService;
    private TransactionService transactionService;

    @Autowired
    public CardController(CardService cardService, TransactionService transactionService) {
        this.cardService = cardService;
        this.transactionService = transactionService;
    }
    
    @RequestMapping(value = "", method = POST)
    @ResponseBody
    public Card createCard(@RequestBody Card card) throws InvalidRequestException, CardAlreadyExistsException {
        return cardService.createCard(card);
    }

    @RequestMapping(value = "/{id}", method = GET)
    @ResponseBody
    public Card getCard(@PathVariable int id) throws NotFoundException {
         return cardService.getCard(id);
    }

    @RequestMapping(value = "/deposit", method = POST)
    @ResponseBody
    public Transaction updateBalance(@RequestBody Payment payment) throws NotFoundException, InvalidRequestException {
        return cardService.updateCardBalance(payment, PaymentType.DEPOSIT);
    }

    @RequestMapping(value = "/{id}/balance", method = GET)
    @ResponseBody
    public Balance getBalance(@PathVariable int id) throws NotFoundException, InvalidRequestException {
        return cardService.getBalance(id);
    }

    @RequestMapping(value = "/{id}/transactions", method = GET)
    @ResponseBody
    public List<Transaction> getTransactions(@PathVariable int id,
                                    @PageableDefault(value=10, page=0) Pageable pageable,
                                    HttpServletResponse response)
            throws NotFoundException, InvalidRequestException {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.ASC, EPOCH_MILLIS);
        }
        Page<Transaction> transactionPage = transactionService.getTransactionsByCardId(id, pageable);
        response.setHeader(TOTAL_PAGES_HEADER, String.valueOf(transactionPage.getTotalPages()));
        response.setHeader(TOTAL_ITEMS_HEADER, String.valueOf(transactionPage.getTotalElements()));
        return transactionPage.getContent();
    }


}
