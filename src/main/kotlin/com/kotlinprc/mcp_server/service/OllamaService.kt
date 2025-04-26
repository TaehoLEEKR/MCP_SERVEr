package com.kotlinprc.mcp_server.service

import com.kotlinprc.mcp_server.model.OllamaModelsResponse
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service

class OllamaService(private val webClient: WebClient) {

    private val log = LoggerFactory.getLogger(OllamaService::class.java)

    @Value("\${ollama.base-url}")
    private lateinit var ollamaBaseUrl: String


    fun listModels(): Mono<List<String>> {

        log.info("List models : {} ", ollamaBaseUrl)

        return webClient
            .get()
            .uri("$ollamaBaseUrl/api/tags")
            .retrieve()
            .bodyToMono(OllamaModelsResponse::class.java)
            .map { response -> response.models.map { it.name } }
    }

}