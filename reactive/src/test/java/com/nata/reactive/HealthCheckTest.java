package com.nata.reactive;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class HealthCheckTest {

    @InjectMocks
    private HealthCheck healthCheck;

    @Mock
    private ExchangeFunction respondingFunction;
    @Mock
    private ExchangeFunction nonRespondingFunction;

    @Test
    void checkAll() {
        try (MockedStatic<HealthCheck> mocked = Mockito.mockStatic(HealthCheck.class)) {

            when(respondingFunction.exchange(any(ClientRequest.class)))
                .thenReturn(Mono.just(ClientResponse.create(HttpStatus.OK)
                    .body("{\"status\":\"UP\"}")
                    .build()));

            WebClient respondingClient = WebClient.builder().exchangeFunction(respondingFunction).build();

            when(nonRespondingFunction.exchange(any(ClientRequest.class)))
                .thenReturn(Mono.error(new WebClientResponseException(408, "timeout", null, null, null)));

            WebClient nonRespondingClient = WebClient.builder().exchangeFunction(nonRespondingFunction).build();

            mocked.when(HealthCheck::getWebClient)
                .thenReturn(nonRespondingClient)
                .thenReturn(respondingClient)
                .thenReturn(nonRespondingClient);

            String actual = healthCheck.checkAll(List.of("one", "two", "three"));

            System.out.println(actual);
        }
    }
}