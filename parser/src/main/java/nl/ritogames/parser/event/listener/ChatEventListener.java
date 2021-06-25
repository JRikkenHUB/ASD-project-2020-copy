package nl.ritogames.parser.event.listener;

import nl.ritogames.shared.Chat;
import nl.ritogames.shared.EventListener;
import nl.ritogames.shared.dto.event.ChatEvent;

public class ChatEventListener implements EventListener<ChatEvent> {

  private Chat chat;

  public ChatEventListener(Chat chat) {
    this.chat = chat;
  }

  @Override
  public void onEvent(ChatEvent event) {
    this.chat.sendMessage(event.getMessage());
  }
}
