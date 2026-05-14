package com.duoc.perfumessus.service;

import com.duoc.perfumessus.dto.FragellaDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;

@Service
@Slf4j
public class FragellaService {

    @Autowired
    @Qualifier("fragellaWebClient")
    private WebClient fragellaWebClient;

    public List<FragellaDTO> buscarPerfumeExterno(String nombrePerfume) {
        log.info("[FragellaService] -> Consultando API Fragella para: {}", nombrePerfume);

        return fragellaWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/fragrances")
                        .queryParam("search", nombrePerfume)
                        .build())
                .retrieve()
                .bodyToFlux(FragellaDTO.class)
                .collectList()
                .block();
    }
}