package nl.ritogames.ruleengine.validation;

import nl.ritogames.ruleengine.rules.general.PlayerIsAlive;
import nl.ritogames.ruleengine.rules.host.PlayerIsNotHost;
import nl.ritogames.shared.dto.event.ChangeHostEvent;

public class ChangeHostEventValidator extends ValidationStrategy<ChangeHostEvent> {
    @Override
    protected void instantiateRules() {
        this.rules.add(new PlayerIsAlive<>());
        this.rules.add(new PlayerIsNotHost<>());
    }
}
