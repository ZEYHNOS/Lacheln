package aba3.lucid.alert;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/alert")
@RequiredArgsConstructor
public class UserAlertController {

    private final UserAlertService userAlertService;

}
