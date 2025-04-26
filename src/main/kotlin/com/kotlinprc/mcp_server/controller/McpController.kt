package com.kotlinprc.mcp_server.controller

import com.kotlinprc.mcp_server.service.OllamaService
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/v1/ollama")
@RequiredArgsConstructor
class McpController(private val ollamaService: OllamaService) {

    @GetMapping("/models")
    fun listModels(): Mono<Map<String, List<String>>> {
        return ollamaService.listModels()
            .map { models -> mapOf("models" to models) }
    }

}