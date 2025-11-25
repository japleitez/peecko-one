package com.peecko.one.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthCheckResource {
    @GetMapping("")
    public String checkHealth() {
        return "OK";
    }

}
