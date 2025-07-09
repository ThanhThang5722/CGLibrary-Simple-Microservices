package com.thangBook.employeeservice.command.controller;

import jakarta.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.thangBook.employeeservice.command.command.CreateEmployeeCommand;
import com.thangBook.employeeservice.command.command.DeleteEmployeeCommand;
import com.thangBook.employeeservice.command.command.UpdateEmployeeCommand;
import com.thangBook.employeeservice.command.model.CreateEmployeeModel;
import com.thangBook.employeeservice.command.model.UpdateEmployeeModel;

import io.swagger.v3.oas.annotations.Hidden;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeCommandController {

    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    public String addEmployee(@Valid @RequestBody CreateEmployeeModel model){
        CreateEmployeeCommand command =
                new CreateEmployeeCommand(UUID.randomUUID().toString(),model.getFirstName(),model.getLastName(),model.getKin(),false);
        return commandGateway.sendAndWait(command);
    }

    @PutMapping("/{employeeId}")
    public String updateEmployee(@Valid @RequestBody UpdateEmployeeModel model, @PathVariable String employeeId){
        UpdateEmployeeCommand command = new UpdateEmployeeCommand(employeeId,model.getFirstName(),model.getLastName(),model.getKin(),model.getIsDisciplined());
        return commandGateway.sendAndWait(command);
    }

    @Hidden
    @DeleteMapping("/{employeeId}")
    public String deleteEmployee(@PathVariable String employeeId){
        DeleteEmployeeCommand command = new DeleteEmployeeCommand(employeeId);
        return commandGateway.sendAndWait(command);
    }
}