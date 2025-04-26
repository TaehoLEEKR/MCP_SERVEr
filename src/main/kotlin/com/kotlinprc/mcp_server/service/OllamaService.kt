package com.kotlinprc.mcp_server.service

import com.kotlinprc.mcp_server.model.MCPResponse
import com.kotlinprc.mcp_server.model.MCPStreamResponse
import com.kotlinprc.mcp_server.model.McpRequest
import com.kotlinprc.mcp_server.model.Message
import com.kotlinprc.mcp_server.model.OllamaModelsResponse
import com.kotlinprc.mcp_server.model.OllamaResponse
import com.kotlinprc.mcp_server.model.OllamaStreamResponse
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant

@Service

class OllamaService(private val webClient: WebClient) {

    private val log = LoggerFactory.getLogger(OllamaService::class.java)

    @Value("\${ollama.base-url:''}")
    private lateinit var ollamaBaseUrl: String

    fun generateCompletion(request: McpRequest): Mono<MCPResponse> {
        val ollamaRequest = mapToOllamaRequest(request)

        return webClient
            .post()
            .uri("$ollamaBaseUrl/api/generate")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(ollamaRequest)
            .retrieve()
            .bodyToMono(OllamaResponse::class.java)
            .map { mapToMCPResponse(it) }
    }

    fun generateCompletionStream(request: McpRequest): Flux<MCPStreamResponse> {
        val ollamaRequest = mapToOllamaRequest(request)

        return webClient
            .post()
            .uri("$ollamaBaseUrl/api/generate")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(ollamaRequest)
            .retrieve()
            .bodyToFlux(OllamaStreamResponse::class.java)
            .map { mapToMCPStreamResponse(it) }
    }


    // 매핑 메서드들
    private fun mapToOllamaRequest(request: McpRequest): Map<String, Any> {
        val prompt = request.message.joinToString("\n") { "${it.role}: ${it.content}" }

        return mapOf(
            "model" to request.model,
            "prompt" to prompt,
            "stream" to request.stream,
            "options" to (request.options ?: emptyMap())
        )
    }

    private fun mapToMCPResponse(response: OllamaResponse): MCPResponse {
        return MCPResponse(
            model = response.model,
            message = Message("assistant", response.response),
            created_at = Instant.now().toString(),
            done = response.done
        )
    }

    private fun mapToMCPStreamResponse(response: OllamaStreamResponse): MCPStreamResponse {
        return MCPStreamResponse(
            model = response.model,
            message = Message("assistant", response.response),
            created_at = response.created_at ?: Instant.now().toString(),
            done = response.done
        )
    }


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