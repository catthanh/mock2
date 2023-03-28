package com.example.mock2.common.exception;

import com.example.mock2.common.dto.response.Response;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final Pattern ENUM_MSG = Pattern.compile("values accepted for Enum class: \\[(.*)\\]");

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public Response<Void> handleParseEnumError(HttpMessageNotReadableException e) {
        logger.error("HttpMessageNotReadableException: {}", e.getClass().getName() + " " + e.getMessage());
        if (e.getCause() != null && e.getCause() instanceof InvalidFormatException) {
            Matcher match = ENUM_MSG.matcher(e.getCause().getMessage());
            if (match.find()) {
                return Response.error("Enum value should be one of: " + match.group(1));
            }
        }
        return Response.error("Invalid Request");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NotFoundException.class)
    public Response<Void> handleNotFoundException(Exception e) {
        return Response.error(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = CommonLogicException.class)
    public Response<Void> handleCommonLogicException(Exception e) {
        return Response.error(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Response<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error("MethodArgumentNotValidException: {}", e.getClass().getName() + " " + e.getMessage());
        return Response.error(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = { WrongcredentialException.class, AuthenticationException.class,
            AccessDeniedException.class })
    public Response<Void> handleUnauthorizedException(Exception e) {
        logger.error("Unauthorized Exception: {}", e.getClass().getName() + " " + e.getMessage());
        return Response.error(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public Response<Void> handleException(Exception e) {
        logger.error("Exception: {}", e.getClass().getName() + " " + e.getMessage());
        e.printStackTrace();
        return Response.internalServerError("Internal Server Error");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public Response<Void> handleMaxSizeException(MaxUploadSizeExceededException e) {
        logger.error("MaxUploadSizeExceededException: {}", e.getClass().getName() + " " + e.getMessage());
        return Response.error("One or More files are too large!");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BadRequestException.class)
    public Response<Void> handleBadRequestException(BadRequestException e) {
        logger.error("BadRequestException: {}", e.getClass().getName() + " " + e.getMessage());
        return Response.error(e.getMessage());
    }
}
