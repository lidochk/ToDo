package com.example.todo.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiException {

    private int statusCode;
    private String message;


}
