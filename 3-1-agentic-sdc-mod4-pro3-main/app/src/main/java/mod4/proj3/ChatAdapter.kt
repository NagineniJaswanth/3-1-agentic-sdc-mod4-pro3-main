package mod4.proj3

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val messages: List<ChatMessage>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMessage: TextView = view.findViewById(R.id.tvMessage)
        val container: LinearLayout = view as LinearLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]
        holder.tvMessage.text = message.text

        if (message.isUser) {
            // User message: Align Right, Blue background
            holder.container.gravity = Gravity.END
            holder.tvMessage.setBackgroundColor(0xFFADD8E6.toInt()) // Light Blue
        } else {
            // Bot message: Align Left, Gray background
            holder.container.gravity = Gravity.START
            holder.tvMessage.setBackgroundColor(0xFFE0E0E0.toInt()) // Gray
        }
    }

    override fun getItemCount() = messages.size
}