package com.reactor.example.ReactiveProgramming;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@WebFluxTest
public class ReactiveProgrammingApplicationTests {

  @Autowired WebTestClient webTestClient;

  @Test
  public void flux_approach1() {
    Flux<Integer> integerFlux =
        webTestClient
            .get()
            .uri("/flux")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus()
            .isOk()
            .returnResult(Integer.class)
            .getResponseBody();

    StepVerifier.create(integerFlux)
        .expectSubscription()
        .expectNext(1)
        .expectNext(2)
        .expectNext(3)
        .expectNext(4)
        .verifyComplete();
  }

  @Test
  public void fluxInfiniteStream() {
    Flux<Long> integerFlux =
        webTestClient
            .get()
            .uri("/fluxinfinitestream")
            .accept(MediaType.APPLICATION_STREAM_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .returnResult(Long.class)
            .getResponseBody();

    StepVerifier.create(integerFlux)
        .expectSubscription()
        .expectNext(0l)
        .expectNext(1l)
        .expectNext(2l)
        .expectNext(3l)
        .thenCancel()
        .verify();
  }

  @Test
  public void mono() {
    webTestClient
        .get()
        .uri("/mono")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(Integer.class)
        .consumeWith(
            res -> {
              Assert.assertEquals(new Integer(1), res.getResponseBody());
            });
  }
}
