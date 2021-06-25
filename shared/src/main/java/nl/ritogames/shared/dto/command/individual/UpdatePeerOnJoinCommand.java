package nl.ritogames.shared.dto.command.individual;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.exception.AgentBuilderException;
import nl.ritogames.shared.exception.CommandFailedException;
import nl.ritogames.shared.exception.ModificationException;

import java.util.ArrayList;
import java.util.Base64;

public class UpdatePeerOnJoinCommand extends IndividualCommand {

    private ArrayList<Boolean> isHost;
    private ArrayList<String> ip;
    private ArrayList<String> individualIDs;
    private ArrayList<String> agentJSONs;

    public UpdatePeerOnJoinCommand() {
        isHost = new ArrayList<>();
        ip = new ArrayList<>();
        individualIDs = new ArrayList<>();
        agentJSONs = new ArrayList<>();
    }

    public UpdatePeerOnJoinCommand(String individualId, String gameName) {
        super(individualId);
    }

    public UpdatePeerOnJoinCommand(String individualId) {
        super(individualId);
    }

    public ArrayList<Boolean> getIsHost() {
        return isHost;
    }

    public void setIsHost(ArrayList<Boolean> isHost) {
        this.isHost = isHost;
    }

    public void addIsHost(Boolean isHost) {
        this.isHost.add(isHost);
    }

    public ArrayList<String> getIp() {
        return ip;
    }

    public void setIp(ArrayList<String> ip) {
        this.ip = ip;
    }

    public void addIp(String ip) {
        this.ip.add(ip);
    }

    public ArrayList<String> getIndividualIDs() {
        return individualIDs;
    }

    public void setIndividualIDs(ArrayList<String> individualIDs) {
        this.individualIDs = individualIDs;
    }

    public void addInvidualId(String indivualId) {
        this.individualIDs.add(indivualId);
    }

    public ArrayList<String> getAgentJSONs() {
        return agentJSONs;
    }

    public void setAgentJSONs(ArrayList<String> agentJSONs) {
        this.agentJSONs = agentJSONs;
    }

    public void addAgentJSON(String agentJSON) {
        this.agentJSONs.add(agentJSON);
    }

    public void decodedAgentJSON() {
        for(int i = 0; i < agentJSONs.size(); i++) {
            agentJSONs.set(i, new String(Base64.getDecoder().decode(agentJSONs.get(i))));
        }
    }

    public void encodeAgentJSON() {
        for(int i = 0; i < agentJSONs.size(); i++) {
            agentJSONs.set(i, Base64.getEncoder().encodeToString(agentJSONs.get(i).getBytes()));
        }
    }

    @Override
    public void execute(GameStateModifier gameStateModifier) throws CommandFailedException {
        if (gameStateModifier.getGameState() == null) {
            gameStateModifier.setGameState(new GameState(""));
        }

        try {
            gameStateModifier.getGameState().getIndividuals().clear();
            for (int i = 0; i < individualIDs.size(); i++) {
                gameStateModifier.addCharacter(individualIDs.get(i));
                gameStateModifier.getAgentService().buildAgent(agentJSONs.get(i), gameStateModifier.getGameState().getIndividual(individualIDs.get(i)));
            }
        } catch(AgentBuilderException | ModificationException e) {
            throw new CommandFailedException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public String generateUIText() {
        return null;
    }
}
