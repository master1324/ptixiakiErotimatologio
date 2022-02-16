package com.p16021.ptixiaki.erotimatologio.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;


@Data
@SuperBuilder
@JsonInclude(NON_NULL)
public class AppResponse {

    protected LocalDateTime timeStamp;
    protected int statusCode;
    protected HttpStatus status;
    protected String errorMessage;
    protected String message;
    protected Map<?,?> data;
}
