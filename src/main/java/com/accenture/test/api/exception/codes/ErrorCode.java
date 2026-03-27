package com.accenture.test.api.exception.codes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ─── Franquicia ──────────────────────────────────────────────────
    FRANCHISE_NOT_FOUND("ERR-001", "Franquicia no encontrada", HttpStatus.NOT_FOUND),
    FRANCHISE_DUPLICATE_NAME("ERR-002", "Ya existe una franquicia con ese nombre", HttpStatus.CONFLICT),

    // ─── Sucursal ────────────────────────────────────────────────────
    BRANCH_NOT_FOUND("ERR-101", "Sucursal no encontrada", HttpStatus.NOT_FOUND),
    BRANCH_DUPLICATE_NAME("ERR-102", "Ya existe una sucursal con ese nombre", HttpStatus.CONFLICT),

    // ─── Producto ────────────────────────────────────────────────────
    PRODUCT_NOT_FOUND("ERR-201", "Producto no encontrado", HttpStatus.NOT_FOUND),
    PRODUCT_DUPLICATE_NAME("ERR-202", "Ya existe un producto con ese nombre en esta sucursal", HttpStatus.CONFLICT),
    PRODUCT_INVALID_STOCK("ERR-203", "El stock no puede ser negativo", HttpStatus.BAD_REQUEST),
    PRODUCT_NO_STOCK("ERR-204", "La sucursal no tiene productos con stock disponible", HttpStatus.BAD_REQUEST),

    // ─── Base de datos ───────────────────────────────────────────────
    DATABASE_CONNECTION_ERROR("ERR-301", "Error de conexión con la base de datos", HttpStatus.SERVICE_UNAVAILABLE),
    DATABASE_TIMEOUT("ERR-302", "Tiempo de espera agotado en la base de datos", HttpStatus.GATEWAY_TIMEOUT),
    DATABASE_WRITE_ERROR("ERR-303", "Error al escribir en la base de datos", HttpStatus.INTERNAL_SERVER_ERROR),

    // ─── Validación ──────────────────────────────────────────────────
    VALIDATION_ERROR("ERR-401", "Error de validación en los datos enviados", HttpStatus.BAD_REQUEST),
    INVALID_ARGUMENT("ERR-402", "Argumento inválido", HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER("ERR-403", "Parámetro requerido no encontrado", HttpStatus.BAD_REQUEST),
    INVALID_FORMAT("ERR-404", "Formato de datos inválido", HttpStatus.BAD_REQUEST),

    // ─── Servidor ────────────────────────────────────────────────────
    INTERNAL_SERVER_ERROR("ERR-501", "Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE("ERR-502", "Servicio no disponible temporalmente", HttpStatus.SERVICE_UNAVAILABLE);

    private final String code;
    private final String defaultMessage;
    private final HttpStatus httpStatus;
}