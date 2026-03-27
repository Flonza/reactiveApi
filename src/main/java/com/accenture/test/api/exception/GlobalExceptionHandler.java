package com.accenture.test.api.exception;

import com.accenture.test.api.exception.custom.*;
import com.accenture.test.application.dto.response.GeneralResponse;
import com.mongodb.MongoException;
import com.mongodb.MongoTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.MissingRequestValueException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static com.accenture.test.api.exception.codes.ErrorCode.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ─── Franquicia ──────────────────────────────────────────────────
    @ExceptionHandler(FranchiseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<GeneralResponse<Void>> handleFranchiseNotFound(FranchiseNotFoundException ex) {
        return Mono.just(GeneralResponse.error(
                ex.getErrorCode().getHttpStatus().value(),
                ex.getErrorCode().getCode(),
                ex.getMessage()
        ));
    }

    // ─── Sucursal ────────────────────────────────────────────────────
    @ExceptionHandler(BranchNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<GeneralResponse<Void>> handleBranchNotFound(BranchNotFoundException ex) {
        return Mono.just(GeneralResponse.error(
                ex.getErrorCode().getHttpStatus().value(),
                ex.getErrorCode().getCode(),
                ex.getMessage()
        ));
    }

    // ─── Producto ────────────────────────────────────────────────────
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<GeneralResponse<Void>> handleProductNotFound(ProductNotFoundException ex) {
        return Mono.just(GeneralResponse.error(
                ex.getErrorCode().getHttpStatus().value(),
                ex.getErrorCode().getCode(),
                ex.getMessage()
        ));
    }

    // ─── Stock inválido ──────────────────────────────────────────────
    @ExceptionHandler(InvalidStockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<GeneralResponse<Void>> handleInvalidStock(InvalidStockException ex) {
        return Mono.just(GeneralResponse.error(
                ex.getErrorCode().getHttpStatus().value(),
                ex.getErrorCode().getCode(),
                ex.getMessage()
        ));
    }

    // ─── Nombre duplicado ────────────────────────────────────────────
    @ExceptionHandler(DuplicateNameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Mono<GeneralResponse<Void>> handleDuplicateName(DuplicateNameException ex) {
        return Mono.just(GeneralResponse.error(
                ex.getErrorCode().getHttpStatus().value(),
                ex.getErrorCode().getCode(),
                ex.getMessage()
        ));
    }

    // ─── Conexión MongoDB ────────────────────────────────────────────
    @ExceptionHandler(DatabaseConnectionException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Mono<GeneralResponse<Void>> handleDatabaseConnection(DatabaseConnectionException ex) {
        return Mono.just(GeneralResponse.error(
                ex.getErrorCode().getHttpStatus().value(),
                ex.getErrorCode().getCode(),
                ex.getMessage()
        ));
    }

    @ExceptionHandler(MongoTimeoutException.class)
    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    public Mono<GeneralResponse<Void>> handleMongoTimeout(MongoTimeoutException ex) {
        return Mono.just(GeneralResponse.error(
                DATABASE_TIMEOUT.getHttpStatus().value(),
                DATABASE_TIMEOUT.getCode(),
                DATABASE_TIMEOUT.getDefaultMessage() + ": " + ex.getMessage()
        ));
    }

    @ExceptionHandler(MongoException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<GeneralResponse<Void>> handleMongoException(MongoException ex) {
        return Mono.just(GeneralResponse.error(
                DATABASE_WRITE_ERROR.getHttpStatus().value(),
                DATABASE_WRITE_ERROR.getCode(),
                DATABASE_WRITE_ERROR.getDefaultMessage() + ": " + ex.getMessage()
        ));
    }

    // ─── Validaciones @Valid ─────────────────────────────────────────
    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<GeneralResponse<Void>> handleValidation(WebExchangeBindException ex) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return Mono.just(GeneralResponse.error(
                VALIDATION_ERROR.getHttpStatus().value(),
                VALIDATION_ERROR.getCode(),
                VALIDATION_ERROR.getDefaultMessage() + ": " + errors
        ));
    }

    // ─── Parámetros faltantes ────────────────────────────────────────
    @ExceptionHandler(MissingRequestValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<GeneralResponse<Void>> handleMissingParameter(MissingRequestValueException ex) {
        return Mono.just(GeneralResponse.error(
                MISSING_PARAMETER.getHttpStatus().value(),
                MISSING_PARAMETER.getCode(),
                MISSING_PARAMETER.getDefaultMessage() + ": " + ex.getMessage()
        ));
    }

    // ─── Formato inválido ────────────────────────────────────────────
    @ExceptionHandler(ServerWebInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<GeneralResponse<Void>> handleInvalidFormat(ServerWebInputException ex) {
        return Mono.just(GeneralResponse.error(
                INVALID_FORMAT.getHttpStatus().value(),
                INVALID_FORMAT.getCode(),
                INVALID_FORMAT.getDefaultMessage() + ": " + ex.getMessage()
        ));
    }

    // ─── Argumento inválido ──────────────────────────────────────────
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<GeneralResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        return Mono.just(GeneralResponse.error(
                INVALID_ARGUMENT.getHttpStatus().value(),
                INVALID_ARGUMENT.getCode(),
                INVALID_ARGUMENT.getDefaultMessage() + ": " + ex.getMessage()
        ));
    }

    // ─── Error genérico ──────────────────────────────────────────────
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<GeneralResponse<Void>> handleGeneric(Exception ex) {
        return Mono.just(GeneralResponse.error(
                INTERNAL_SERVER_ERROR.getHttpStatus().value(),
                INTERNAL_SERVER_ERROR.getCode(),
                INTERNAL_SERVER_ERROR.getDefaultMessage() + ": " + ex.getMessage()
        ));
    }
}