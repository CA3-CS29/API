package com.cs29.api.controllers;

import com.cs29.api.dtos.PortfolioDto;
import com.cs29.api.dtos.Response;
import com.cs29.api.services.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Response getAccount(@PathVariable("accountId") String accountId) {
        try {
            return Response.ok().setPayload(accountService.getAccount(accountId));
        } catch (IllegalArgumentException errorMessage) {
            return Response.duplicateEntity();
        }
    }

    @PostMapping(value = "/createAccount/accountId")
    public Response createAccount(@RequestBody String accountId){
        try {
            accountService.createAccount(accountId);
        }
        catch (IllegalArgumentException errorMessage) {
            return Response.duplicateEntity();
        }
        return Response.ok();
    }

    @PostMapping(value = "/addPortfolio/{accountId}")
    public Response addPortfolio(@PathVariable("accountId") String accountId,
                                  @RequestBody PortfolioDto portfolioDto){
        try {
            accountService.addPortfolio(accountId, portfolioDto);
        }
        catch (NoSuchElementException errorMessage){
            return Response.exception();
        }
        return Response.ok();
    }

    @PostMapping(value = "/updatePortfolio/{accountId}")
    public Response updatePortfolio(@PathVariable("accountId") String accountId,
                                   @RequestBody PortfolioDto portfolioDto) {
        try {
            accountService.updatePortfolio(accountId, portfolioDto);
        }
        catch (NoSuchElementException errorMessage){
            return Response.exception();
        }
        return Response.ok();
    }

}
