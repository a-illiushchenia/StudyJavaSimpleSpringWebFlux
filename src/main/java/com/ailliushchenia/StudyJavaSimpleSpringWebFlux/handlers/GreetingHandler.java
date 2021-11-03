package com.ailliushchenia.StudyJavaSimpleSpringWebFlux.handlers;

import com.ailliushchenia.StudyJavaSimpleSpringWebFlux.models.Greeting;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class GreetingHandler {

    public Mono<ServerResponse> helloText(ServerRequest request) {
        BodyInserter<String, ReactiveHttpOutputMessage> body =
                BodyInserters.fromValue("Hello, Spring! Text");

        return ServerResponse
                .ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(body);
    }

    public Mono<ServerResponse> helloJsonSimple(ServerRequest request) {
        BodyInserter<Greeting, ReactiveHttpOutputMessage> body =
                BodyInserters.fromValue(new Greeting("Hello, Spring! Json"));

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

    public Mono<ServerResponse> helloJson(ServerRequest request) {
        //пагинация
        Long start = request.queryParam("start")
                .map(Long::valueOf)
                .orElse(0L);

        Long count = request.queryParam("count")
                .map(Long::valueOf)
                .orElse(3L);

        Flux<Greeting> data = Flux
                .just(
                        "Hello, Reactive!",
                        "More then one",
                        "Third post",
                        "Fourth post",
                        "Fifth post"
                )
                .skip(start) //пагинация
                .take(count) //пагинация
                .map(Greeting::new);

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(data, Greeting.class);
    }

    public Mono<ServerResponse> helloMustache(ServerRequest request) {

        String user = request.queryParam("user")
                .orElse("Nobody");
        return ServerResponse
                .ok()
                .render("index", Map.of("user", user));
    }
}
