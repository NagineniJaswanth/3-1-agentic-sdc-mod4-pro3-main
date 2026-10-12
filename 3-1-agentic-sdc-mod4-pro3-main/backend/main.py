from fastapi import FastAPI
from pydantic import BaseModel
from typing import Dict, List

app = FastAPI()

# In-memory storage for context recall: { "session_id": [ {"role": "user", "content": "..."} ] }
memory_db: Dict[str, List[dict]] = {}

class ChatRequest(BaseModel):
    session_id: str
    message: str

class ChatResponse(BaseModel):
    reply: str
    history_length: int

@app.post("/chat", response_model=ChatResponse)
async def chat_endpoint(request: ChatRequest):
    session_id = request.session_id

    # Initialize memory if new session
    if session_id not in memory_db:
        memory_db[session_id] = []

    # Store user message
    memory_db[session_id].append({"role": "user", "content": request.message})

    # --- CONTEXT RECALL LOGIC ---
    # In a real app, you would send 'memory_db[session_id]' to an LLM like GPT-4.
    # Here, we simulate recall by counting previous messages.

    history = memory_db[session_id]
    previous_context = ""

    if len(history) > 1:
        # Recalling the very first message in this session
        first_msg = history[0]['content']
        previous_context = f" (I remember you started by saying: '{first_msg}')"

    bot_reply = f"Received: '{request.message}'.{previous_context}"

    # Store bot response
    memory_db[session_id].append({"role": "assistant", "content": bot_reply})

    return ChatResponse(reply=bot_reply, history_length=len(history))

if __name__ == "__main__":
    import uvicorn
    # Run on 0.0.0.0 so the Android Emulator can access it via 10.0.2.2
    uvicorn.run(app, host="0.0.0.0", port=8000)