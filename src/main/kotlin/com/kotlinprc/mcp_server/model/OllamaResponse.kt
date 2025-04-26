package com.kotlinprc.mcp_server.model


data class OllamaResponse(
    val model: String,
    val response: String,
    val done: Boolean
)

data class OllamaStreamResponse(
    val model: String,
    val response: String,
    val created_at: String? = null,
    val done: Boolean
)


data class OllamaModelsResponse(
    val models: List<OllamaModel>
)

data class OllamaModel(
    val name: String,
    val size: Long
)
