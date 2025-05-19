package com.nata.reactive;

import static com.google.common.collect.MoreCollectors.onlyElement;
import static reactor.core.scheduler.Schedulers.parallel;

import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class HealthCheck {

    public static Mono<String> checkOne(String url) {
        return WebClient.builder()
            .baseUrl("http://localhost:8080/bird/")
            .build()
            .get()
            .uri(url)
            .retrieve()
            .bodyToMono(String.class)

            .doOnError(throwable -> log.info("health check for {} failed ", url))
            .doOnSuccess(response -> log.info("health check for {} ok {}", url, response))
            .flatMap(response -> Mono.just(url))
            .onErrorResume(ex -> Mono.empty())
            .timeout(Duration.ofSeconds(1));
    }

    public static String checkAll(List<String> urls) {
        return Flux.fromIterable(urls)
            .parallel()
            .runOn(parallel())
            .flatMap(HealthCheck::checkOne)
            .sequential()
            .collect(onlyElement())
            .block();
    }

    public static void main(String[] args) {
           System.out.println(checkAll(List.of("bound","sound", "hound")));
    }
}
