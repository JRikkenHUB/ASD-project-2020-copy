package nl.ritogames.ruleengine.validation;

import nl.ritogames.ruleengine.rules.general.GameIsInProgress;
import nl.ritogames.ruleengine.rules.interaction.IndividualInReach;
import nl.ritogames.shared.dto.event.AttackEvent;

public class AttackEventValidator extends ValidationStrategy<AttackEvent> {
    @Override
    public void instantiateRules() {
        this.rules.add(new IndividualInReach());
        this.rules.add(new GameIsInProgress<>());
    }
}
