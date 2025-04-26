package com.kotlinprc.mcp_server.model

data class OllamaModelsResponse(
    val models: List<OllamaModel>
)

data class OllamaModel(
    val name: String,
    val size: Long
)
