package com.cs29.api.controllers;

import com.cs29.api.dtos.AccountMapper;
import com.cs29.api.dtos.PortfolioDto;
import com.cs29.api.models.Account;
import com.cs29.api.services.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {
    private final String TEST_ID = "test_id";
    private final int TEST_NUM = 1;
    private final Account TEST_ACCOUNT = Account.builder()
            .userId(TEST_ID)
            .numPortfolios(TEST_NUM)
            .portfolios(new ArrayList<>())
            .build();
    private final String TEST_NAME = "TEST_NAME";
    private final int TEST_NUM_REGIONS = 0;
    private final String TEST_DATE = "10/10/2020";
    private final PortfolioDto TEST_PORTFOLIO_DTO = PortfolioDto
            .builder()
            .portfolioId(TEST_NAME)
            .userId(TEST_ID)
            .tag(TEST_NAME)
            .regions(null)
            .numRegions(TEST_NUM_REGIONS)
            .createdOn(TEST_DATE)
            .updatedOn(TEST_DATE)
            .build();

    private MockMvc mockMvc;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private JacksonTester<Account> jsonAccount;

    private JacksonTester<PortfolioDto> jsonPortfolioDto;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    @SneakyThrows
    public void canRetrieveByIdWhenExists() {
        given(accountService.getAccount(TEST_ID)).willReturn(AccountMapper.toAccountDto(TEST_ACCOUNT));
        MockHttpServletResponse response = mockMvc.perform(
                get(String.format("/account/getAccount/%s", TEST_ID))
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }

    @Test
    @SneakyThrows
    public void canRetrieveByIdWhenDoesNotExist() {
        given(accountService.getAccount(TEST_ID)).willThrow(NoSuchElementException.class);
        MockHttpServletResponse response = mockMvc.perform(
                get(String.format("/account/getAccount/%s", TEST_ID))
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }

    @Test
    @SneakyThrows
    public void canCreateNewAccount() {
        MockHttpServletResponse response = mockMvc.perform(
                post(String.format("/account/createAccount/%s", TEST_ID)).contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }

    @Test
    @SneakyThrows
    public void statusOkWhenServiceThrowsIllegalArgumentException() {
        doThrow(IllegalArgumentException.class).when(accountService).createAccount(TEST_ID);
        MockHttpServletResponse response = mockMvc.perform(
                post(String.format("/account/createAccount/%s", TEST_ID)).contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }

    @Test
    @SneakyThrows
    public void canAddPortfolio() {
        MockHttpServletResponse response = mockMvc.perform(
                post(String.format("/account/addPortfolio/%s", TEST_ID)).contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPortfolioDto.write(TEST_PORTFOLIO_DTO).getJson())
        ).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }

    @Test
    @SneakyThrows
    public void statusOkWhenServiceThrows() {
        doThrow(NoSuchElementException.class).when(accountService).addPortfolio(TEST_ID, TEST_PORTFOLIO_DTO);
        MockHttpServletResponse response = mockMvc.perform(
                post(String.format("/account/addPortfolio/%s", TEST_ID)).contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPortfolioDto.write(TEST_PORTFOLIO_DTO).getJson())
        ).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }
}
