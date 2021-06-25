package nl.ritogames.shared.dto.event;

import java.util.Objects;

public class ChatEvent extends Event {
    private String message;

    public ChatEvent() {
        super();
    }

    /**
     * Event used for when a player tries to send a message in the chat
     * @param individualId
     * @param message
     */
    public ChatEvent(String individualId, String message) {
        super(individualId);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatEvent chatEvent = (ChatEvent) o;
        return Objects.equals(message, chatEvent.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }

    @Override
    public String toString() {
        return "ChatEvent{" +
                ", message='" + message + '\'' +
                '}';
    }
}
