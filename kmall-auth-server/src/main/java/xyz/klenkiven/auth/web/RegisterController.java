package xyz.klenkiven.auth.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    @GetMapping("/reg")
    public String registerPage() {
        return "reg";
    }

}
