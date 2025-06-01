package com.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class HealthController {

    //Public Url
    @GetMapping("/health")
    public String health() {
        return "Health okay";
    }

    //Authenticated Url
    @GetMapping("/health1")
    public String health1() {
        return "Health okay";
    }

    @GetMapping("/mono")
    public Mono<String> getMono() throws InterruptedException {
        //   Thread.sleep(5000);
        return Mono.just("Hello from Mono!");
    }

    @GetMapping("/flux")
    public Flux<String> getFlux() {
        return Flux.just("Spring", "Boot", "Reactive", "WebFlux");
    }
}

