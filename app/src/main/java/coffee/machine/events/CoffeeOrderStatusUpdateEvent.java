package coffee.machine.events;

import coffee.machine.aggregates.CoffeeOrderAggregate;

public record CoffeeOrderStatusUpdateEvent(String id, String coffeeMachineId, String slotId,
                                           CoffeeOrderAggregate.Status status) {
}
