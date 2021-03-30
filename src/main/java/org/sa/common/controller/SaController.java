package org.sa.common.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j
@RequestMapping("/sa")
public class SaController {

    //private final

    @GetMapping("/home")
    public String getList(Model model) {

        log.info("home....................");

        return "/sa/home";
    }

    @GetMapping("/static")
    public String getStatic(Model model) {

        log.info("static....................");

        return "/sa/static";
    }

}
