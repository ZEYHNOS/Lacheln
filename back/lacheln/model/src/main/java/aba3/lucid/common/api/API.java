package aba3.lucid.common.api;

import aba3.lucid.common.status_code.StatusCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class API<T> {

    private Result result;
    private T data;
    private String description;
    private Pagination pagination;

    public static <T>API<T> OK(){
        return API.<T>builder()
                .result(Result.OK())
                .data(null)
                .build()
                ;
    }

    public static <T>API<T> OK(T data) {
        return API.<T>builder()
                .result(Result.OK())
                .data(data)
                .build()
                ;
    }

    public static <T>API<T> OK(String description) {
        return API.<T>builder()
                .result(Result.OK())
                .description(description)
                .build()
                ;
    }

    public static <T>API<T> OK(T data, String description) {
        return API.<T>builder()
                .result(Result.OK())
                .data(data)
                .description(description)
                .build()
                ;
    }

    public static <T>API<T> OK(T data, Pagination pagination) {
        return API.<T>builder()
                .result(Result.OK())
                .data(data)
                .pagination(pagination)
                .build()
                ;
    }

    public static <T>API<T> OK(StatusCodeIfs statusCode) {
        return API.<T>builder()
                .result(Result.OK(statusCode))
                .data(null)
                .build()
                ;
    }

    public static <T>API<T> OK(T data, StatusCodeIfs statusCode) {
        return API.<T>builder()
                .result(Result.OK(statusCode))
                .data(data)
                .build()
                ;
    }

    public static <T>API<T> OK(T data, StatusCodeIfs statusCode, Pagination pagination) {
        return API.<T>builder()
                .result(Result.OK(statusCode))
                .data(data)
                .pagination(pagination)
                .build()
                ;
    }


    public static <T>API<T> ERROR(StatusCodeIfs statusCode) {
        return API.<T>builder()
                .result(Result.ERROR(statusCode))
                .data(null)
                .build()
                ;
    }

    public static <T>API<T> ERROR(StatusCodeIfs statusCode, String description) {
        return API.<T>builder()
                .result(Result.ERROR(statusCode, description))
                .data(null)
                .build()
                ;
    }

    public static <T>API<T> ERROR(T data, StatusCodeIfs statusCode) {
        return API.<T>builder()
                .result(Result.ERROR(statusCode))
                .data(data)
                .build()
                ;
    }

    public static <T>API<T> ERROR(T data, StatusCodeIfs statusCode, String description) {
        return API.<T>builder()
                .result(Result.ERROR(statusCode))
                .data(data)
                .description(description)
                .build()
                ;
    }
}
