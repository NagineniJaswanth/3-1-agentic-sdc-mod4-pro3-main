package mod4.proj3

data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

// API Request/Response Models
data class ApiRequest(val session_id: String, val message: String)
data class ApiResponse(val reply: String, val history_length: Int)