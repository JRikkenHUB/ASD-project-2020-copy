package nl.ritogames.chat;

import nl.ritogames.shared.Chat;
import nl.ritogames.shared.ChatHandler;
import nl.ritogames.shared.exception.NotImplementedException;

public class ASDChat implements Chat {
    @Override
    public boolean sendMessage(String message) {
        throw new NotImplementedException();
    }

    @Override
    public void registerChatChangedHandler(ChatHandler chatHandler) {
        throw new NotImplementedException();
    }
}
