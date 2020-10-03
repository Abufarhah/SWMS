package edu.birzeit.swms.controllers;

import edu.birzeit.swms.dtos.AreaDto;
import edu.birzeit.swms.dtos.BinDto;
import edu.birzeit.swms.dtos.EmployeeDto;
import edu.birzeit.swms.exceptions.ResourceAssignedException;
import edu.birzeit.swms.exceptions.ResourceNotAssignedException;
import edu.birzeit.swms.exceptions.ResourceNotFoundException;
import edu.birzeit.swms.models.Area;
import edu.birzeit.swms.models.Bin;
import edu.birzeit.swms.services.AreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Log
@RestController
@RequestMapping("/areas")
@Api(tags = "Operations related to areas in SWMS")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully done operation"),
        @ApiResponse(code = 201, message = "Successfully created entity"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
})
public class AreaController {

    @Autowired
    AreaService areaService;

    @GetMapping
    public List<AreaDto> getAreas() {
        return areaService.getAreas();
    }

    @GetMapping("/{id}")
    public AreaDto getArea(@PathVariable int id) {
        return areaService.getArea(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AreaDto addArea(@RequestBody AreaDto areaDto) {
        return areaService.addArea(areaDto);
    }

    @PutMapping("/{id}")
    public AreaDto updateArea(@RequestBody AreaDto areaDto, @PathVariable int id) {
        return areaService.updateArea(areaDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteArea(@PathVariable int id) {
        areaService.deleteArea(id);
    }

    @GetMapping("/{id}/bins")
    public List<BinDto> getBinsOfArea(@PathVariable int id) {
        return areaService.getBinsOfArea(id);
    }

    @GetMapping("{areaId}/assign-bin/{binId}")
    public void assignBin(@PathVariable int areaId, @PathVariable int binId) {
        areaService.assignBin(areaId, binId);
    }

    @GetMapping("{areaId}/unassign-bin/{binId}")
    public void unAssignBin(@PathVariable int areaId, @PathVariable int binId) {
        areaService.unAssignBin(areaId, binId);
    }

    @GetMapping("{areaId}/assign-employee/{employeeId}")
    public void assignEmployee(@PathVariable int areaId, @PathVariable int employeeId) {
        areaService.assignEmployee(areaId, employeeId);
    }

    @GetMapping("{areaId}/unassign-employee/{employeeId}")
    public void unAssignEmployee(@PathVariable int areaId, @PathVariable int employeeId) {
        areaService.unAssignEmployee(areaId, employeeId);
    }

    @GetMapping("/{id}/employees")
    public List<EmployeeDto> getEmployeesOfArea(@PathVariable int id) {
        return areaService.getEmployeesOfArea(id);
    }


}
