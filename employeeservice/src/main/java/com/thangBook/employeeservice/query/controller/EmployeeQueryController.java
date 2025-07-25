package com.thangBook.employeeservice.query.controller;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.thangBook.commonservice.queries.GetDetailEmployeeQuery;
import com.thangBook.employeeservice.query.model.EmployeeResponseModel;
import com.thangBook.employeeservice.query.queries.GetAllEmployeeQuery;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Employee Query")
@Slf4j
public class EmployeeQueryController {
    @Autowired
    private QueryGateway queryGateway;

    @Operation(
        summary = "Get List Employee",
        description = "Get endpoint for employee with filter",
        responses = {
            @ApiResponse(
                description = "Success",
                responseCode = "200"
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized / Invalid Token"
            )
        }
    )
    @GetMapping
    public List<EmployeeResponseModel> getAllEmployee(@RequestParam(required = false, defaultValue = "false") Boolean isDisciplined){
            log.info("Get all Employee");
        return queryGateway
                    .query(new GetAllEmployeeQuery(isDisciplined), ResponseTypes.multipleInstancesOf(EmployeeResponseModel.class)).join();
    }

    @GetMapping("/{employeeId}")
    public EmployeeResponseModel getDetailEmployee(@PathVariable String employeeId){
        return queryGateway.query(new GetDetailEmployeeQuery(employeeId),ResponseTypes.instanceOf(EmployeeResponseModel.class)).join();
    }
}