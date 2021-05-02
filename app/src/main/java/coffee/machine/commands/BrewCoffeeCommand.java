package coffee.machine.commands;

import coffee.machine.aggregates.SlotAggregate;
import coffee.machine.models.Recipe;

public record BrewCoffeeCommand(String slotId, String orderId, Recipe recipe) implements Command<SlotAggregate> {
    @Override
    public Class<SlotAggregate> target() {
        return SlotAggregate.class;
    }

    @Override
    public String targetId() {
        return slotId;
    }
}
