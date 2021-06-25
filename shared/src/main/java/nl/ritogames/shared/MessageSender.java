package nl.ritogames.shared;

import nl.ritogames.shared.dto.ChatMessage;

public interface MessageSender {
    void sendMessage(ChatMessage message);

    void registerChatMessageHandler(ChatMessageHandler chatMessageHandler);
}
