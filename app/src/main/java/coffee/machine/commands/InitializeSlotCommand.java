package coffee.machine.commands;

import coffee.machine.aggregates.SlotAggregate;

public record InitializeSlotCommand(String id, String coffeeMachineId) implements Command<SlotAggregate> {
    @Override
    public Class<SlotAggregate> target() {
        return SlotAggregate.class;
    }

    public String targetId() {
        return id;
    }
}
