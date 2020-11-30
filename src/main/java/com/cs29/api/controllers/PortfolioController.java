package com.cs29.api.controllers;

import com.cs29.api.dtos.PortfolioDto;
import com.cs29.api.dtos.Response;
import com.cs29.api.services.PortfolioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;


@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @Autowired
    public PortfolioController(PortfolioService portfolioService) { this.portfolioService = portfolioService;
    }

    @GetMapping(value = "/getPortfolio/{tag}/{userId}")
    public Response getPortfolio(@PathVariable("tag") String tag,
                              @PathVariable("userId") String userId) {
        try {
            return Response.ok().setPayload(portfolioService.getPortfolio(tag, userId));
        } catch (NoSuchElementException errorMessage) {
            return Response.exception();
        }
    }

    @PostMapping(value = "/createPortfolio/{userId}")
    public Response createPortfolio(@PathVariable("userId") String userId,
                                 @RequestBody PortfolioDto portfolioDto){
        try {
            portfolioService.createPortfolio(userId, portfolioDto);
        }
        catch (IllegalArgumentException errorMessage) {
            return Response.duplicateEntity();
        }
        catch(NoSuchElementException errorMessage){
            return Response.exception();
        }
        return Response.ok();
    }

    @PostMapping(value = "/updatePortfolio/{accountId}")
    public Response updatePortfolio(@PathVariable("accountId") String accountId,
                                    @RequestBody PortfolioDto portfolioDto) {
        try {
            portfolioService.updatePortfolio(accountId, portfolioDto);
        }
        catch (NoSuchElementException errorMessage){
            return Response.exception();
        }
        return Response.ok();
    }

    @GetMapping(value = "/getAllByTag/{tag}")
    public Response getAllByTag(@PathVariable("tag") String tag) {
        try {
            return Response.ok().setPayload(portfolioService.getAllByTag(tag));
        }
        catch (NoSuchElementException errorMessage) {
            return Response.exception();
        }
    }


    @GetMapping(value = "/getAllByUserId/{userId}")
    public Response getAllByUserId(@PathVariable("userId") String tag) {
        try {
            return Response.ok().setPayload(portfolioService.getAllByUserId(tag));
        }
        catch (NoSuchElementException errorMessage) {
            return Response.exception();
        }
    }



}
