package com.cs29.api.controllers;

import com.cs29.api.dtos.OfficeDto;
import com.cs29.api.dtos.Response;
import com.cs29.api.services.OfficeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;


@RestController
@RequestMapping("/office")
public class OfficeController {

    private final OfficeService officeService;

    @Autowired
    public OfficeController(OfficeService officeService) { this.officeService = officeService;
    }

    @GetMapping(value = "/getOffice/{name}/{userId}")
    public Response getOffice(@PathVariable("name") String name,
                             @PathVariable("userId") String userId) {
        try {
            return Response.ok().setPayload(officeService.getOffice(name, userId));
        }
        catch (NoSuchElementException errorMessage) {
            return Response.exception();
        }
    }

    @GetMapping(value = "/getAllUsersOffices/{userId}")
    public Response getAllUsersOffices(@PathVariable("userId") String userId) {
        try {
            return Response.ok().setPayload(officeService.getAllUsersOffices(userId));
        }
        catch (NoSuchElementException errorMessage) {
            return Response.exception();
        }

    }

    @PostMapping(value = "/createOffice/{regionId}/{portfolioId}/{accountId}")
    public Response createOffice(@PathVariable("regionId") String regionId,
                                @PathVariable("portfolioId") String portfolioId,
                                @PathVariable("accountId") String accountId,
                                @RequestBody OfficeDto officeDto){
        try {
            officeService.createOffice( regionId, portfolioId,accountId, officeDto);
        }
        catch (NoSuchElementException errorMessage) {
            return Response.exception();
        }
        return Response.ok();
    }


}
