package com.example.mcp.sample.server;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class CalculatorController {

    @GetMapping(value = "/calculate", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> calculate(@RequestParam double a,
                                                   @RequestParam double b,
                                                   @RequestParam String op) {

        double result = switch (op) {
            case "add" -> a + b;
            case "sub" -> a - b;
            case "mul" -> a * b;
            case "div" -> b != 0 ? a / b : Double.NaN;
            default -> Double.NaN;
        };

        return Flux.just(
                        ServerSentEvent.<String>builder()
                                .event("info")
                                .data("Calculating: " + a + " " + op + " " + b)
                                .build(),
                        ServerSentEvent.<String>builder()
                                .event("result")
                                .data(String.valueOf(result))
                                .build()
                )
                .delayElements(Duration.ofSeconds(1));
    }

}
