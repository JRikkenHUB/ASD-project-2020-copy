package nl.ritogames.shared;

public interface Chat {
    boolean sendMessage(String message);

    void registerChatChangedHandler(ChatHandler chatHandler);
}
