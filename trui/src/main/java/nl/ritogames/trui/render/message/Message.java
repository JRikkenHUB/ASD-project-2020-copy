package nl.ritogames.trui.render.message;

/**
 * <p>Functional interface to hold the messages</p>
 */
public class Message {
  String content;
  String sender;

  public Message(String content, String sender) {
    this.content = content;
    this.sender = sender;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }
}
