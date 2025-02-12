package aba3.lucid.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "User API", description = "사용자 관리 API")
public class UserController {

    @GetMapping("/{id}")
    @Operation(summary = "사용자 조회", description = "사용자의 ID를 입력하면 사용자 정보를 조회합니다.")
    public String getUserById(
            @Parameter(description = "사용자의 ID", example = "1")
            @PathVariable Long id) {
        return "User ID: " + id;
    }

    @PostMapping("/create")
    @Operation(summary = "사용자 생성", description = "새로운 사용자를 등록합니다.")
    public String createUser(
            @Parameter(description = "사용자 이름", example = "John Doe")
            @RequestParam String name) {
        return "User Created: " + name;
    }
}
