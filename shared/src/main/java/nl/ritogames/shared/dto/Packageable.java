package nl.ritogames.shared.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import nl.ritogames.shared.dto.command.RelevantForContext;
import nl.ritogames.shared.dto.command.individual.*;
import nl.ritogames.shared.dto.command.menu.MenuCommand;
import nl.ritogames.shared.dto.command.menu.StartGameCommand;
import nl.ritogames.shared.dto.event.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AttackEvent.class, name = "AttackEvent"),
        @JsonSubTypes.Type(value = ChatEvent.class, name = "ChatEvent"),
        @JsonSubTypes.Type(value = CompileEvent.class, name = "CompileEvent"),
        @JsonSubTypes.Type(value = CreateGameEvent.class, name = "CreateGameEvent"),
        @JsonSubTypes.Type(value = GameEvent.class, name = "GameEvent"),
        @JsonSubTypes.Type(value = GameJoinEvent.class, name = "GameJoinEvent"),
        @JsonSubTypes.Type(value = GameLeaveEvent.class, name = "GameLeaveEvent"),
        @JsonSubTypes.Type(value = GameSaveEvent.class, name = "GameSaveEvent"),
        @JsonSubTypes.Type(value = InteractionEvent.class, name = "InteractionEvent"),
        @JsonSubTypes.Type(value = LobbyEvent.class, name = "LobbyEvent"),
        @JsonSubTypes.Type(value = MenuEvent.class, name = "MenuEvent"),
        @JsonSubTypes.Type(value = MoveEvent.class, name = "MoveEvent"),
        @JsonSubTypes.Type(value = PickUpEvent.class, name = "PickUpEvent"),
        @JsonSubTypes.Type(value = StartAgentEvent.class, name = "StartAgentEvent"),
        @JsonSubTypes.Type(value = StartGameEvent.class, name = "StartGameEvent"),
        @JsonSubTypes.Type(value = ChangeHostEvent.class, name = "ChangeHostEvent"),
        @JsonSubTypes.Type(value = StopGameEvent.class, name = "StopGameEvent"),
        @JsonSubTypes.Type(value = StopAgentEvent.class, name = "StopAgentEvent"),

        @JsonSubTypes.Type(value = MenuCommand.class, name = "MenuCommand"),
        @JsonSubTypes.Type(value = CreateGameCommand.class, name = "CreateGameCommand"),
        @JsonSubTypes.Type(value = RelevantForContext.class, name = "RelevantForContext"),
        @JsonSubTypes.Type(value = RelevantForOwnContext.class, name = "RelevantForOwnContext"),
        @JsonSubTypes.Type(value = AttributeCommand.class, name = "AttributeCommand"),
        @JsonSubTypes.Type(value = IndividualCommand.class, name = "IndividualCommand"),
        @JsonSubTypes.Type(value = TwoIndividualCommand.class, name = "TwoIndividualCommand"),
        @JsonSubTypes.Type(value = StartGameCommand.class, name = "StartGameCommand"),
        @JsonSubTypes.Type(value = JoinGameCommand.class, name = "JoinGameCommand"),
        @JsonSubTypes.Type(value = DamageIndividualCommand.class, name = "DamageIndividualCommand"),
        @JsonSubTypes.Type(value = MoveCommand.class, name = "MoveCommand"),
        @JsonSubTypes.Type(value = PickUpAttributeCommand.class, name = "PickUpAttributeCommand"),
        @JsonSubTypes.Type(value = WinCommand.class, name = "Wincommand"),
        @JsonSubTypes.Type(value = StartAgentCommand.class, name = "StartAgentCommand"),
        @JsonSubTypes.Type(value = UpdatePeerOnJoinCommand.class, name = "UpdatePeerOnJoinCommand"),
        @JsonSubTypes.Type(value = StopAgentCommand.class, name = "StopAgentCommand")
})

public interface Packageable {
}
