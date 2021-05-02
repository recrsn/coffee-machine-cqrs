package coffee.machine.events;

import coffee.machine.models.Recipe;

public record CoffeeOrderCreatedEvent(String orderId, String coffeeMachineId, Recipe recipe) {
}
