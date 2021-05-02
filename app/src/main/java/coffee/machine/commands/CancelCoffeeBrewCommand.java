package coffee.machine.commands;

import coffee.machine.aggregates.SlotAggregate;

public record CancelCoffeeBrewCommand(String slotId) implements Command<SlotAggregate> {
    @Override
    public Class<SlotAggregate> target() {
        return SlotAggregate.class;
    }

    @Override
    public String targetId() {
        return slotId;
    }
}
