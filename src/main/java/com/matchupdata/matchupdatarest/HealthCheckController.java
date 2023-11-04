package com.matchupdata.matchupdatarest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController
{
    @GetMapping("/HealthCheck")
    public String healthCheck() {
        return "Pong";
    }
}
