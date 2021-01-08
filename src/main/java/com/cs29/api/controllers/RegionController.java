package com.cs29.api.controllers;

import com.cs29.api.dtos.RegionDto;
import com.cs29.api.dtos.Response;
import com.cs29.api.services.RegionService;

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
@RequestMapping("/region")
@CrossOrigin(origins = {"https://ca3-frontend.herokuapp.com", "http://localhost:3000"})
public class RegionController {

    private final RegionService regionService;

    @Autowired
    public RegionController(RegionService accountService) {
        this.regionService = accountService;
    }

    @GetMapping(value = "/getRegion/{name}/{userId}")
    @CrossOrigin(origins = {"https://ca3-frontend.herokuapp.com/", "http://localhost:3000/"})
    public Response getRegion(@PathVariable("name") String name,
                             @PathVariable("userId") String userId) {
        try {
            return Response.ok().setPayload(regionService.getRegion(name, userId));
        }
        catch (NoSuchElementException errorMessage) {
            return Response.exception();
        }
    }

    @GetMapping(value = "/getAllRegionsForUser/{userId}")
    @CrossOrigin(origins = {"https://ca3-frontend.herokuapp.com/", "http://localhost:3000/"})
    public Response getAllRegionsForUser(@PathVariable("userId") String userId) {
        try {
            return Response.ok().setPayload(regionService.getAllRegionsForUser(userId));
        } catch (NoSuchElementException errorMessage) {
            return Response.exception();
        }
    }


    @PostMapping(value = "/createRegion/{accountId}/{portfolioId}/{userId}")
    @CrossOrigin(origins = {"https://ca3-frontend.herokuapp.com/", "http://localhost:3000/"})
    public Response createRegion(@PathVariable("accountId") String accountId,
                                @PathVariable("portfolioId") String portfolioId,
                                @RequestBody RegionDto regionDto,
                                @PathVariable("userId") String userId){
        try {
            regionService.createRegion(accountId, portfolioId, regionDto, userId);
        }
        catch (IllegalArgumentException errorMessage) {
            return Response.duplicateEntity();
        }
        catch (NoSuchElementException errorMessage) {
            return Response.exception();
        }
        return Response.ok();
    }
}

