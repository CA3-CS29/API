package com.cs29.api.controllers;

import com.cs29.api.dtos.EntryDto;
import com.cs29.api.dtos.Response;
import com.cs29.api.services.EntryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;


@RestController
@RequestMapping("/entry")
public class EntryController {

    private final EntryService entryService;

    @Autowired
    public EntryController(EntryService accountService) {
        this.entryService = accountService;
    }

    @GetMapping(value = "/getEntry/{tag}/{officeId}")
    public Response getEntry(@PathVariable("tag") String tag,
                             @PathVariable("officeId") String officeId) {
        try {
            return Response.ok().setPayload(entryService.getEntry(tag, officeId));
        }
        catch (NoSuchElementException errorMessage) {
            return Response.exception();
        }
    }

    @GetMapping(value = "/getEntriesFromOffice/{officeId}")
    public Response getEntriesFromOffice(@PathVariable("officeId") String officeId) {
        try {
            return Response.ok().setPayload(entryService.getEntriesFromOffice(officeId));
        } catch (NoSuchElementException errorMessage) {
            return Response.exception();
        }
    }

    @GetMapping(value = "/getAllBySource/{source}")
    public Response getAllBySource(@PathVariable("source") String source) {
        try {
            return Response.ok().setPayload(entryService.getAllBySource(source));
        }
        catch (NoSuchElementException errorMessage) {
            return Response.exception();
        }

    }

    @PostMapping(value = "/createEntry/{accountId}/{portfolioId}/{regionId}/{officeId}")
    public Response createEntry(@PathVariable("accountId") String accountId,
                                @PathVariable("portfolioId") String portfolioId,
                                @PathVariable("regionId") String regionId,
                                @PathVariable("officeId") String officeId,
                                @RequestBody EntryDto entryDto){
        try {
            entryService.createEntry(accountId, portfolioId, regionId, officeId, entryDto);
        }
        catch (NoSuchElementException errorMessage) {
            return Response.exception();
        }
        return Response.ok();
    }
}
