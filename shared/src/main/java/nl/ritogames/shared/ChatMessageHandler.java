package nl.ritogames.shared;

import nl.ritogames.shared.dto.ChatMessage;

@FunctionalInterface
public interface ChatMessageHandler {
    void onChatReceived(ChatMessage chatMessage);
}
