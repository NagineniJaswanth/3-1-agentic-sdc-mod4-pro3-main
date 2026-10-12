package mod4.proj3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val messages = mutableListOf<ChatMessage>()
    private lateinit var adapter: ChatAdapter
    private val sessionId = "session_${System.currentTimeMillis()}" // Unique session for memory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvChat = findViewById<RecyclerView>(R.id.rvChat)
        val etMessage = findViewById<EditText>(R.id.etMessage)
        val btnSend = findViewById<Button>(R.id.btnSend)

        adapter = ChatAdapter(messages)
        rvChat.layoutManager = LinearLayoutManager(this)
        rvChat.adapter = adapter

        btnSend.setOnClickListener {
            val text = etMessage.text.toString().trim()
            if (text.isNotEmpty()) {
                sendMessage(text)
                etMessage.text.clear()
            }
        }
    }

    private fun sendMessage(text: String) {
        // 1. Add user message to UI
        messages.add(ChatMessage(text, isUser = true))
        adapter.notifyItemInserted(messages.size - 1)

        // 2. Call Backend
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.sendMessage(
                    ApiRequest(session_id = sessionId, message = text)
                )

                // 3. Add bot response to UI
                messages.add(ChatMessage(response.reply, isUser = false))
                adapter.notifyItemInserted(messages.size - 1)

            } catch (e: Exception) {
                messages.add(ChatMessage("Error: ${e.message}", isUser = false))
                adapter.notifyItemInserted(messages.size - 1)
            }
        }
    }
}