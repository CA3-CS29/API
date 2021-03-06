package com.cs29.api.controllers;

import com.cs29.api.dtos.OfficeDto;
import com.cs29.api.dtos.Response;
import com.cs29.api.services.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;


@RestController
@RequestMapping("/office")
@CrossOrigin(origins = {"https://ca3-frontend.herokuapp.com", "http://localhost:3000"})
public class OfficeController {

    private final OfficeService officeService;

    @Autowired
    public OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }

    @GetMapping(value = "/getOffice/{name}/{userId}/{regionId}")
    @CrossOrigin(origins = {"https://ca3-frontend.herokuapp.com/", "http://localhost:3000/"})
    public Response getOffice(@PathVariable("name") String name,
                              @PathVariable("userId") String userId,
                              @PathVariable("regionId") String regionId) {
        try {
            return Response.ok().setPayload(officeService.getOffice(name, userId, regionId));
        } catch (NoSuchElementException errorMessage) {
            return Response.exception();
        }
    }

    @GetMapping(value = "/getAllUsersOffices/{userId}")
    @CrossOrigin(origins = {"https://ca3-frontend.herokuapp.com/", "http://localhost:3000/"})
    public Response getAllUsersOffices(@PathVariable("userId") String userId) {
        try {
            return Response.ok().setPayload(officeService.getAllUsersOffices(userId));
        } catch (NoSuchElementException errorMessage) {
            return Response.exception();
        }

    }

    @PostMapping(value = "/createOffice/{regionId}/{portfolioId}/{accountId}")
    @CrossOrigin(origins = {"https://ca3-frontend.herokuapp.com/", "http://localhost:3000/"})
    public Response createOffice(@PathVariable("regionId") String regionId,
                                 @PathVariable("portfolioId") String portfolioId,
                                 @PathVariable("accountId") String accountId,
                                 @RequestBody OfficeDto officeDto) {
        try {
            officeService.createOffice(regionId, portfolioId, accountId, officeDto);
        } catch (NoSuchElementException errorMessage) {
            return Response.exception();
        }
        return Response.ok();
    }

    @DeleteMapping(value = "/delete/{officeId}/{regionId}/{regionName}/{portfolioId}/{portfolioTag}/{userId}")
    @CrossOrigin(origins = {"https://ca3-frontend.herokuapp.com/", "http://localhost:3000/"})
    public Response deleteOffice(@PathVariable("portfolioId") String portfolioId,
                                 @PathVariable("regionId") String regionId,
                                 @PathVariable("regionName") String regionName,
                                 @PathVariable("portfolioTag") String portTag,
                                 @PathVariable("userId") String userId,
                                 @PathVariable("officeId") String officeId) {
        try {
            officeService.deleteOffice(officeId, portfolioId, portTag, regionId, regionName, userId);
            return Response.ok();
        } catch (NoSuchElementException e) {
            return Response.exception();
        }
    }

}
