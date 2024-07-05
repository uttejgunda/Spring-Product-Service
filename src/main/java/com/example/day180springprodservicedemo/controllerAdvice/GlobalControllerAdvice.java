package com.example.day180springprodservicedemo.controllerAdvice;

import com.example.day180springprodservicedemo.dto.ErrorResponseDto;
import com.example.day180springprodservicedemo.exceptions.DBNotFoundException;
import com.example.day180springprodservicedemo.exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleProductNotFoundException(ProductNotFoundException errorObject ) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage("From CONTROLLER ADVICE: " + errorObject.getMessage());
        ResponseEntity<ErrorResponseDto> responseEntity = new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        return responseEntity;
    }

    @ExceptionHandler(DBNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleDBNotFoundException (DBNotFoundException errorObject ) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(errorObject.getMessage());
        ResponseEntity<ErrorResponseDto> responseEntity = new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        return responseEntity;
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpClientErrorException (HttpClientErrorException errorObject ) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(errorObject.getMessage());
        ResponseEntity<ErrorResponseDto> responseEntity = new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
        return responseEntity;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneralException (HttpClientErrorException errorObject ) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(errorObject.getMessage());
        ResponseEntity<ErrorResponseDto> responseEntity = new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
        return responseEntity;
    }
}
