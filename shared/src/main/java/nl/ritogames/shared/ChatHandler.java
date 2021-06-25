package nl.ritogames.shared;

import nl.ritogames.shared.dto.GameChat;

@FunctionalInterface
public interface ChatHandler {
    void onChatChanged(GameChat gameChat);
}
