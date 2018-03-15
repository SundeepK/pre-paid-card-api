package com.curve.controller;

import com.curve.domain.ErrorResponse;
import com.curve.exception.CardAlreadyExistsException;
import com.curve.exception.InvalidRequestException;
import com.curve.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.threeten.bp.DateTimeException;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Component
@ControllerAdvice
public class ExceptionHandlerController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse requestHandlingNoHandlerFound(HttpServletRequest request, final NoHandlerFoundException ex) {
        return new ErrorResponse("Not found");
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidCardException(HttpServletRequest request, Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(DateTimeException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDateTimeException(HttpServletRequest request, Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(CardAlreadyExistsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCardAlreadyExistsException(HttpServletRequest request, Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleObjectOptimisticLockingFailureException(HttpServletRequest request, Exception e) {
        return new ErrorResponse("Invalid nonce supplied");
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleAddressFormatException(HttpServletRequest request, Exception e) {
        logger.warn("Could not find url={}", getRequestUrl(request), e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleHttpStatusCodeException(HttpServletRequest request, HttpStatusCodeException e) {
        String err = e.getResponseBodyAsString();
        logger.warn("Internal error url={} with response {}", getRequestUrl(request), err, e);
        return new ErrorResponse(err);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(HttpServletRequest request, Exception e) {
        String requestUrl = getRequestUrl(request);
        logger.error("Caught unhandled exception from url={}", requestUrl, e);
        return new ErrorResponse(e.getMessage());
    }

    private static String getRequestUrl(HttpServletRequest request) {
        return request.getRequestURL().toString();
    }

}