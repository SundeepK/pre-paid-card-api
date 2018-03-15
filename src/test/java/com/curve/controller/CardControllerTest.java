package com.curve.controller;

import com.curve.domain.Transaction;
import com.curve.service.CardService;
import com.curve.service.TransactionService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class CardControllerTest {

    private static final String CARDS_ENDPOINT = "/cards";

    @Mock
    private CardService cardService;
    @Mock
    private TransactionService transactionService;

    private CardController underTest;
    private Page<Transaction> transactions;
    private MockMvc mockMvc;

    @Before
    public void setUp(){
        underTest = new CardController(cardService, transactionService);

        mockMvc = standaloneSetup(underTest)
                .setCustomArgumentResolvers(pageRequestArgumentResolver())
                .build();
    }

    @Test
    public void testItGetsTransactions() throws Exception {
        when(transactionService.getTransactionsByCardId(1, new PageRequest(0, 10, new Sort(Sort.Direction.ASC, "amount")))).thenReturn(getTransactions());

        MockHttpServletRequestBuilder param = MockMvcRequestBuilders
                .get(CARDS_ENDPOINT + "/1/transactions")
                .param("sort", "amount,asc")
                .param("size", "10")
                .param("page", "0");

        mockMvc.perform(param)
                .andExpect((MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].cardId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].amount").value(2d))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].cardId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].amount").value(5d))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].cardId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].amount").value(10d))
                .andReturn();

    }

    @Test
    public void testItSetsDefaultSorting() throws Exception {
        when(transactionService.getTransactionsByCardId(1, new PageRequest(0, 10, Sort.Direction.ASC, "epochMillis"))).thenReturn(getTransactions());

        MockHttpServletRequestBuilder param = MockMvcRequestBuilders
                .get(CARDS_ENDPOINT + "/1/transactions")
                .param("sort", "epochMillis,asc")
                .param("size", "10")
                .param("page", "0");

        mockMvc.perform(param)
                .andExpect((MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3))))
                .andReturn();

    }

    @Test
    public void testItSetsPagination() throws Exception {
        when(transactionService.getTransactionsByCardId(1, new PageRequest(2, 5, Sort.Direction.ASC, "epochMillis"))).thenReturn(getTransactions());

        MockHttpServletRequestBuilder param = MockMvcRequestBuilders
                .get(CARDS_ENDPOINT + "/1/transactions")
                .param("sort", "epochMillis,asc")
                .param("size", "5")
                .param("page", "2");

        mockMvc.perform(param)
                .andExpect((MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3))))
                .andReturn();

    }

    @Test
    public void testItDefaultsPaginationAndSorting() throws Exception {
        when(transactionService.getTransactionsByCardId(1, new PageRequest(0, 10, Sort.Direction.ASC, "epochMillis"))).thenReturn(getTransactions());

        MockHttpServletRequestBuilder param = MockMvcRequestBuilders
                .get(CARDS_ENDPOINT + "/1/transactions");

        mockMvc.perform(param)
                .andExpect((MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3))))
                .andReturn();

    }

    @Test
    public void testItSetsHeaders() throws Exception {
        when(transactionService.getTransactionsByCardId(1, new PageRequest(0, 10, Sort.Direction.ASC, "epochMillis"))).thenReturn(getTransactions());

        MockHttpServletRequestBuilder param = MockMvcRequestBuilders
                .get(CARDS_ENDPOINT + "/1/transactions");

        MvcResult response = mockMvc.perform(param)
                .andExpect((MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3))))
                .andReturn();

        String pages = response.getResponse().getHeader("total-pages");
        String total = response.getResponse().getHeader("total");
        assertEquals("1", pages);
        assertEquals("3", total);
    }

    public Page<Transaction> getTransactions() {
        Transaction tran1 = Transaction.newBuilder().cardId(1).amount(2d).build();
        Transaction tran2 = Transaction.newBuilder().cardId(1).amount(5d).build();
        Transaction tran3 = Transaction.newBuilder().cardId(1).amount(10d).build();
        return new PageImpl<>(Arrays.asList(tran1, tran2, tran3));
    }

    static SortHandlerMethodArgumentResolver sortArgumentResolver() {
        return new SortHandlerMethodArgumentResolver();
    }

    static PageableHandlerMethodArgumentResolver pageRequestArgumentResolver() {
        return new PageableHandlerMethodArgumentResolver(sortArgumentResolver());
    }


}