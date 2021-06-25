package nl.ritogames;

import nl.ritogames.shared.GameStateContextListener;
import nl.ritogames.shared.GameStateContextProvider;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.GameStateContext;
import nl.ritogames.shared.dto.command.Command;
import nl.ritogames.shared.dto.command.RelevantForContext;
import nl.ritogames.shared.dto.command.individual.CreateGameCommand;
import nl.ritogames.shared.dto.command.individual.IndividualCommand;
import nl.ritogames.shared.dto.command.individual.RelevantForOwnContext;
import nl.ritogames.shared.dto.gameobject.ASDVector;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.world.tile.ASDTile;
import nl.ritogames.shared.dto.gameobject.world.tile.EdgeTile;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;
import nl.ritogames.shared.exception.UnknownCharacterException;
import nl.ritogames.shared.logger.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ASDGameStateContextProvider implements GameStateContextProvider {
    public final int vision;
    private static final int UPDATE_MARGIN = 1;
    private Command executedCommand;
    private final Map<String, GameStateContextListener> contextListeners;
    private GameStateModifier gameStateModifier;

    public ASDGameStateContextProvider(int vision) {
        this.vision = vision;
        contextListeners = new HashMap<>();
    }

    @Inject
    public ASDGameStateContextProvider() {
        this.vision = 4;
        contextListeners = new HashMap<>();
    }

    /**
     * Subscribes an individual to a GameStateContextListener.
     *
     * @param contextListener The contextListener the individual want to subscribe to.
     * @param individualId    The Id of the individual that want to subscribe to the contextlistener.
     */
    @Override
    public void subscribe(GameStateContextListener contextListener, String individualId) {
        if (!gameStateModifier.getGameState().hasIndividual(individualId))
            throw new UnknownCharacterException(String.format("Character with id '%s' does not exist.", individualId));
        contextListeners.put(individualId, contextListener);
    }


    @Override
    public void unsubscribe(String individualId) {
        contextListeners.get(individualId).unsubscribedSignal();
        contextListeners.remove(individualId);
    }

    @Override
    public boolean isSubscribed(String individualId) {
        return contextListeners.containsKey(individualId);
    }

    /**
     * Processes a Command and sends an update to all relevant listeners.
     *
     * @param command The command to process.
     */
    @Override
    public void processCommand(Command command) {
        Logger.logMethodCall(this);
        if (!(command instanceof RelevantForContext)) return;
        List<String> listenersToUpdate = new ArrayList<>();

        if (command instanceof RelevantForOwnContext) {
            listenersToUpdate.add(((RelevantForOwnContext) command).getInitiatingIndividualId());
        } else {
            ASDVector eventLocation = getEventLocation(command);
            if (eventLocation == null) {
                listenersToUpdate.addAll(contextListeners.keySet());
            } else {
                listenersToUpdate.addAll(getAffectedListeners(eventLocation));
            }
        }

        executedCommand = command;
        for (String listener : listenersToUpdate) alertListener(listener);
    }

    /**
     * Gets the affected listeners for a command that was executed at a given location.
     *
     * @param eventVector The ASDVector where a command was executed.
     * @return A list of the Ids of all the affected listeners.
     */
    private List<String> getAffectedListeners(ASDVector eventVector) {
        List<String> relevantListeners = new ArrayList<>();

        for (String listenerId : contextListeners.keySet()) {
            ASDVector listenerVector = gameStateModifier.getGameState().getIndividual(listenerId).getLocation();

            if (isInsideContextVision(listenerVector, eventVector)) relevantListeners.add(listenerId);
        }

        return relevantListeners;
    }

    /**
     * Gets the location of a given command.
     *
     * @param command The command to get the location from.
     * @return The location where the command was executed as an ASDVector.
     */
    protected ASDVector getEventLocation(Command command) {
        if (command instanceof CreateGameCommand) {
            return null;
        }
        if (command instanceof IndividualCommand) {
            return gameStateModifier.getGameState().getIndividual(((IndividualCommand) command).getInitiatingIndividualId()).getLocation();
        } else return null;
    }

    /**
     * Checks if the location of an executed command is inside the context of the given location of an individual.
     *
     * @param individualLocation The location of the individual.
     * @param commandLocation    The location of the executed command.
     * @return If the command was inside the context of the individual.
     */
    private boolean isInsideContextVision(ASDVector individualLocation, ASDVector commandLocation) {
        int minX = individualLocation.getX() - vision - UPDATE_MARGIN;
        int minY = individualLocation.getY() - vision - UPDATE_MARGIN;
        int maxX = individualLocation.getX() + vision + UPDATE_MARGIN;
        int maxY = individualLocation.getY() + vision + UPDATE_MARGIN;
        return (commandLocation.getX() < maxX && commandLocation.getY() < maxY && commandLocation.getX() >= minX && commandLocation.getY() >= minY);
    }

    /**
     * Alerts the user with the given individualId of the new context.
     *
     * @param individualId The Id of the individual.
     */
    private void alertListener(String individualId) {
        if (contextListeners.containsKey(individualId))
            contextListeners.get(individualId).updateContext(getContext(individualId));
    }

    /**
     * Gets the context surrounding the given individual.
     *
     * @param individualId The Id of the individual.
     * @return The context surrounding the individual.
     */
    @Override
    public GameStateContext getContext(String individualId) {
        final int visionInDiameter = vision * 2 + 1;
        GameState gameState = gameStateModifier.getGameState();

        if (gameState == null) {
            return new GameStateContext(new ASDTile[visionInDiameter][visionInDiameter]);
        }

        if (!gameState.hasIndividual(individualId)) {
            throw new UnknownCharacterException(String.format("Character with id '%s' does not exist.", individualId));
        }

        ASDVector characterLocation = gameState.getIndividual(individualId).getLocation();

        ASDTile[][] characterPointOfView = null;

        if (gameState.getWorld() != null) {
            WorldTile[][] worldTiles = gameState.getWorld().getTiles();
            characterPointOfView = new ASDTile[visionInDiameter][visionInDiameter];

            for (int i = 0; i < visionInDiameter; i++) {
                for (int j = 0; j < visionInDiameter; j++) {
                    int x = i + characterLocation.getX() - vision;
                    int y = j + characterLocation.getY() - vision;

                    if (x >= 0 && y >= 0 && x < worldTiles.length && y < worldTiles.length) {
                        characterPointOfView[i][j] = worldTiles[x][y];
                    } else {
                        characterPointOfView[i][j] = new EdgeTile();
                    }
                }
            }
        }

        return new GameStateContext(characterPointOfView, vision, executedCommand, getCharactersFromGameState(gameState), gameState.getStatus(), gameState.getName());
    }

    /**
     * Gets a list of all the characters in a given gamestate.
     *
     * @param gameState The gamestate.
     * @return A list of all the characters in the gamestate.
     */
    private Map<String, Character> getCharactersFromGameState(GameState gameState) {
        return gameState.getIndividuals().entrySet()
                .stream().parallel()
                .filter(map -> map.getValue() instanceof Character)
                .collect(Collectors.toMap(Map.Entry::getKey, map -> (Character) map.getValue()));
    }

    @Inject
    public void setGameStateProvider(GameStateModifier gameStateModifier) {
        this.gameStateModifier = gameStateModifier;
    }

    public Command getExecutedCommand() {
        return executedCommand;
    }
}