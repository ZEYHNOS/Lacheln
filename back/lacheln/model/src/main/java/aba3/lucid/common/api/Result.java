package aba3.lucid.common.api;

import aba3.lucid.common.status_code.StatusCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result {

    // 사용자에게 보여줄 코드 (주로 에러 용도로 사용)  ex ) 코드가 10001 일 때 어디서 에러가 발생했는지 식별 가능
    Integer resultCode;

    // 사용자에게 보여줄 상세 설명
    String description;

    public static Result OK() {
        return Result.builder()
                .resultCode(200)
                .description("OK")
                .build()
                ;
    }

    public static Result OK(StatusCodeIfs statusCode) {
        return Result.builder()
                .resultCode(statusCode.getServerStatusCode())
                .description(statusCode.getDescription())
                .build()
                ;
    }

    public static Result OK(String description) {
        return Result.builder()
                .resultCode(200)
                .description(description)
                .build()
                ;
    }

    public static Result ERROR() {
        return Result.builder()
                .resultCode(400)
                .description("ERROR")
                .build()
                ;
    }

    public static Result ERROR(String description) {
        return Result.builder()
                .resultCode(400)
                .description(description)
                .build()
                ;
    }

    public static Result ERROR(StatusCodeIfs statusCode) {
        return Result.builder()
                .resultCode(statusCode.getServerStatusCode())
                .description(statusCode.getDescription())
                .build()
                ;
    }

    public static Result ERROR(StatusCodeIfs statusCode, String description) {
        return Result.builder()
                .resultCode(statusCode.getServerStatusCode())
                .description(description)
                .build()
                ;
    }

}
