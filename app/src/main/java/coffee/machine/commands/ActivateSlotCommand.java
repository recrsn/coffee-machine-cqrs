package coffee.machine.commands;

import coffee.machine.aggregates.CoffeeMachineAggregate;

public record ActivateSlotCommand(String coffeeMachineId, String slotId) implements Command<CoffeeMachineAggregate> {
    @Override
    public Class<CoffeeMachineAggregate> target() {
        return CoffeeMachineAggregate.class;
    }

    @Override
    public String targetId() {
        return coffeeMachineId;
    }
}
