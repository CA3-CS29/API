package com.cs29.api.controllers;

import com.cs29.api.ApiApplication;
import com.cs29.api.dtos.PortfolioDto;
import com.cs29.api.dtos.Response;
import com.cs29.api.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = "/getAccount/{accountId}")
    @CrossOrigin(origins = {"https://ca3-frontend.herokuapp.com/", "http://localhost:3000/"})
    public Response getAccount(@PathVariable("accountId") String accountId) {
        ApiApplication.logger.info("AccountController received get request with accountId " + accountId);
        try {
            return Response.ok().setPayload(accountService.getAccount(accountId));
        } catch (IllegalArgumentException errorMessage) {
            return Response.duplicateEntity().setErrors(errorMessage);
        } catch (NoSuchElementException errorMessage) {
            return Response.exception().setErrors(errorMessage);
        }
    }

    @PostMapping(value = "/createAccount/{accountId}")
    @CrossOrigin(origins = {"https://ca3-frontend.herokuapp.com/", "http://localhost:3000/"})
    public Response createAccount(@PathVariable("accountId") String accountId) {
        ApiApplication.logger.info("AccountController received create request with accountId " + accountId);
        try {
            accountService.createAccount(accountId);
        } catch (IllegalArgumentException errorMessage) {
            return Response.duplicateEntity();
        } catch (NoSuchElementException errorMessage) {
            return Response.exception().setErrors(errorMessage);
        }
        return Response.ok();
    }

    @PostMapping(value = "/addPortfolio/{accountId}")
    public Response addPortfolio(@PathVariable("accountId") String accountId,
                                 @RequestBody PortfolioDto portfolioDto) {
        ApiApplication.logger.info("AccountController received create request for portfolio with accountId "
                + accountId + "\nand portfolio: " + portfolioDto.getTag());
        try {
            accountService.addPortfolio(accountId, portfolioDto);
        } catch (NoSuchElementException errorMessage) {
            return Response.exception();
        }
        return Response.ok();
    }

    @PostMapping(value = "/updatePortfolio/{accountId}")
    public Response updatePortfolio(@PathVariable("accountId") String accountId,
                                    @RequestBody PortfolioDto portfolioDto) {
        ApiApplication.logger.info("AccountController received update request for portfolio with accountId "
                + accountId + "\nand portfolio: " + portfolioDto.getTag());
        try {
            accountService.updatePortfolio(accountId, portfolioDto);
        } catch (NoSuchElementException errorMessage) {
            return Response.exception();
        }
        return Response.ok();
    }

}
