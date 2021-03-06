package edu.birzeit.swms.controllers;

import edu.birzeit.swms.dtos.BinDto;
import edu.birzeit.swms.dtos.ReportDto;
import edu.birzeit.swms.enums.Status;
import edu.birzeit.swms.services.BinService;
import io.swagger.annotations.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log
@RestController
@RequestMapping("/bins")
@Api(tags = "Operations related to bins in SWMS")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully done operation"),
        @ApiResponse(code = 201, message = "Successfully created entity"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
})
public class BinController {

    @Autowired
    BinService binService;

    @GetMapping
    @ApiOperation(value = "This API used to get all bins", authorizations = {@Authorization(value = "jwtToken")})
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CITIZEN')")
    public List<BinDto> getBins(
            @ApiParam(value = "Filter the list in terms of status") @Nullable @RequestParam Status status,
            @ApiParam(value = "Get the closest n bins to the passed location", example = "31.9031238") @Nullable @RequestParam Double latitude,
            @ApiParam(value = "Get the closest n bins to the passed location", example = "35.1977626") @Nullable @RequestParam Double longitude,
            @ApiParam(value = "Get the closest n bins to the passed location", example = "5") @Nullable @RequestParam Integer n) {
        return binService.findByLocationAndStatus(latitude, longitude, n, status);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "This API a specific bin based on id", authorizations = {@Authorization(value = "jwtToken")})
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CITIZEN')")
    public BinDto getBin(@PathVariable int id) {
        return binService.getBin(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "This API used to add new bin", authorizations = {@Authorization(value = "jwtToken")})
    @PreAuthorize("hasAnyRole('ADMIN')")
    public BinDto addBin(@RequestBody BinDto binDto) {
        return binService.addBin(binDto);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "This API used update bin based on id", authorizations = {@Authorization(value = "jwtToken")})
    @PreAuthorize("hasAnyRole('ADMIN')")
    public BinDto updateBin(@RequestBody BinDto binDto, @PathVariable int id) {
        return binService.updateBin(binDto, id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "This API used to delete bin based on id", authorizations = {@Authorization(value = "jwtToken")})
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void deleteBin(@PathVariable int id) {
        binService.deleteBin(id);
    }

    @GetMapping("/{id}/reports")
    @ApiOperation(value = "This API used to get all reports of a specific bin", authorizations = {@Authorization(value = "jwtToken")})
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public List<ReportDto> getBinReports(@PathVariable int id) {
        return binService.getReports(id);
    }

    @PutMapping
    @ApiOperation(value = "This API used by bins to update their status",
            authorizations = {@Authorization(value = "jwtToken")})
    public BinDto updateBinStatus(@RequestParam Status status, int id) {
        return binService.updateBinStatus(status, id);
    }


    @PostMapping("/{id}")
    @ApiOperation(value = "This API used by bins to send emergency notification" +
            "to whom responsible to its area",
            authorizations = {@Authorization(value = "jwtToken")})
    public String sendEmergencyNotification(@PathVariable int id) {
        return binService.sendEmergencyNotification(id);
    }

}
