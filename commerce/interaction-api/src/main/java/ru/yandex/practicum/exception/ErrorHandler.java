package ru.yandex.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoProductsInShoppingCart(final NoProductsInShoppingCartException e) {
        return new ErrorResponse(
                e.getCause(),
                e.getStackTrace(),
                HttpStatus.BAD_REQUEST,
                "Нет искомых товаров в корзине.",
                e.getMessage(),
                e.getSuppressed(),
                e.getLocalizedMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoSpecifiedProductUnWarehouse(final NoSpecifiedProductInWarehouseException e) {
        return new ErrorResponse(
                e.getCause(),
                e.getStackTrace(),
                HttpStatus.BAD_REQUEST,
                "Нет информации о товаре на складе.",
                e.getMessage(),
                e.getSuppressed(),
                e.getLocalizedMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleNotAuthorizedUser(final NotAuthorizedUserException e) {
        return new ErrorResponse(
                e.getCause(),
                e.getStackTrace(),
                HttpStatus.UNAUTHORIZED,
                "Имя пользователя не должно быть пустым (неавторизованный пользователь).",
                e.getMessage(),
                e.getSuppressed(),
                e.getLocalizedMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleProductInShoppingCartLowQuantityInWarehouse(final ProductInShoppingCartLowQuantityInWarehouse e) {
        return new ErrorResponse(
                e.getCause(),
                e.getStackTrace(),
                HttpStatus.BAD_REQUEST,
                "Ошибка, товар из корзины не находится в требуемом количестве на складе.",
                e.getMessage(),
                e.getSuppressed(),
                e.getLocalizedMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleProductNotFound(final ProductNotFoundException e) {
        return new ErrorResponse(
                e.getCause(),
                e.getStackTrace(),
                HttpStatus.NOT_FOUND,
                "Ошибка, товар по идентификатору в БД не найден.",
                e.getMessage(),
                e.getSuppressed(),
                e.getLocalizedMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSpecifiedProductAlreadyInWarehouse(final SpecifiedProductAlreadyInWarehouseException e) {
        return new ErrorResponse(
                e.getCause(),
                e.getStackTrace(),
                HttpStatus.BAD_REQUEST,
                "Ошибка, товар с таким описанием уже зарегистрирован на складе.",
                e.getMessage(),
                e.getSuppressed(),
                e.getLocalizedMessage()
        );
    }

}
