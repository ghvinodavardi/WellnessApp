package com.example.wellnessapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wellnessapp.data.OpenAIClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean
)
class ChatViewModel : ViewModel() {

    private val _isAssistantTyping = MutableStateFlow(false)
    val isAssistantTyping: StateFlow<Boolean> = _isAssistantTyping
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages
    fun sendMessage(text: String) {
        if (text.isBlank()) return
        _messages.value += ChatMessage(text, true)
        viewModelScope.launch {
            _isAssistantTyping.value = true
            delay(200)
            val reply = OpenAIClient.ask(text)
            _isAssistantTyping.value = false
            _messages.value += ChatMessage(reply, false)
        }
    }
}
