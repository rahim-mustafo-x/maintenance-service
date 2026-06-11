package org.safa.maintenanceservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    /** For singed users it works**/
    @GetMapping("/v1/hi")
    public String hi() {
        return "hi";
    }
}
