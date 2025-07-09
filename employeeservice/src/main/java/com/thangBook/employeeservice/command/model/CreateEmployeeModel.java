package com.thangBook.employeeservice.command.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeModel {

    @NotBlank(message = "First name is mandatory")
    
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @JsonProperty("LastName")
    private String LastName;
    
    @NotBlank(message = "Kin is mandatory")
    @JsonProperty("Kin")
    private String Kin;
}