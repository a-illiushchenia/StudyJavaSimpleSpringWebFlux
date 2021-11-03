package com.ailliushchenia.StudyJavaSimpleSpringWebFlux.config;

import com.ailliushchenia.StudyJavaSimpleSpringWebFlux.handlers.GreetingHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration(proxyBeanMethods = false)
public class GreetingRouter {

    @Bean
    public RouterFunction<ServerResponse> route(GreetingHandler greetingHandler) {
        RequestPredicate roteText =
                GET("/helloText")
                .and(accept(MediaType.TEXT_PLAIN));

        RequestPredicate roteJsonSimple =
                GET("/helloJsonSimple")
                        .and(accept(MediaType.TEXT_PLAIN));

        RequestPredicate roteJson =
                GET("/helloJson")
                        .and(accept(MediaType.TEXT_PLAIN));

        return RouterFunctions
                .route(roteText, greetingHandler::helloText)//пример с возвратом обычного текста
                .andRoute(roteJsonSimple, greetingHandler::helloJsonSimple)//пример с возвратом JSON
                .andRoute(roteJson, greetingHandler::helloJson)//пример с возвратом сложного JSON
                .andRoute(
                        RequestPredicates.GET("/"),//пример с реализацией без отдельного класса хендлера
                        serverRequest ->{
                            return ServerResponse
                                    .ok()
                                    .contentType(MediaType.TEXT_PLAIN)
                                    .body(
                                            BodyInserters.fromValue("Main page")
                                    );
                        }
                ).andRoute(
                        RequestPredicates.GET("/helloMustache"),//пример перехода на шаблон mustache
                        greetingHandler :: helloMustache
                );
    }
}
