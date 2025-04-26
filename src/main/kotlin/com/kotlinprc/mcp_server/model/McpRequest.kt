package com.kotlinprc.mcp_server.model

data class McpRequest(
    val model:String,
    val message:List<Message>,
    val stream: Boolean = false,
    val options: Map<String, Any>? = null

)

data class Message(
    val role: String,
    val content: String
)

data class MCPResponse(
    val model: String,
    val message: Message,
    val created_at: String,
    val done: Boolean
)
data class MCPStreamResponse(
    val model: String,
    val message: Message,
    val created_at: String,
    val done: Boolean
)
