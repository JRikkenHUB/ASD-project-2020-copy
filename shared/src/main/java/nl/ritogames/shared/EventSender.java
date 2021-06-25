package nl.ritogames.shared;

import nl.ritogames.shared.dto.event.InteractionEvent;
import nl.ritogames.shared.dto.event.StopGameEvent;

public interface EventSender {
    void sendEvent(InteractionEvent event);

    void joinSession(String ip, String individualId);

    void stopGame(StopGameEvent stopGameEvent);

    void createSession(String sessionName, String individualId, String agentName);
}
