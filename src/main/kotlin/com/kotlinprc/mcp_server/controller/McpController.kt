package com.kotlinprc.mcp_server.controller

import com.kotlinprc.mcp_server.model.MCPResponse
import com.kotlinprc.mcp_server.model.MCPStreamResponse
import com.kotlinprc.mcp_server.model.McpRequest
import com.kotlinprc.mcp_server.service.OllamaService
import lombok.RequiredArgsConstructor
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/v1/ollama")
@RequiredArgsConstructor
class McpController(private val ollamaService: OllamaService) {

    @PostMapping("/chat/completions")
    fun chatCompletions(@RequestBody request: McpRequest): Mono<MCPResponse> {
        return ollamaService.generateCompletion(request)
    }

    @PostMapping(value = ["/chat/completions/stream"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun chatCompletionsStream(@RequestBody request: McpRequest): Flux<MCPStreamResponse> {
        val streamRequest = request.copy(stream = true)
        return ollamaService.generateCompletionStream(streamRequest)
    }


    @GetMapping("/models")
    fun listModels(): Mono<Map<String, List<String>>> {
        return ollamaService.listModels()
            .map { models -> mapOf("models" to models) }
    }

}