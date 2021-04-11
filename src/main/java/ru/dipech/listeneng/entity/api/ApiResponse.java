package ru.dipech.listeneng.entity.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

import static ru.dipech.listeneng.util.ExceptionUtils.getCleanedMessage;

@Getter
@Setter
@RequiredArgsConstructor
public class ApiResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final ResponseStatus status;
    private final String message;
    private final T data;

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(ResponseStatus.OK, null, null);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(ResponseStatus.OK, null, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(ResponseStatus.ERROR, message, null);
    }

    public static <T> ApiResponse<T> error(Throwable throwable) {
        return new ApiResponse<>(ResponseStatus.ERROR, getCleanedMessage(throwable), null);
    }

    public static <T> ApiResponse<T> error(HttpStatus httpStatus) {
        return error(httpStatus.getReasonPhrase() + " (" + httpStatus.value() + ")");
    }

}
