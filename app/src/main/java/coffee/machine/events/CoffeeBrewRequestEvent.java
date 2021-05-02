package coffee.machine.events;

import coffee.machine.models.Recipe;

public record CoffeeBrewRequestEvent(String coffeeMachineId, String slotId, String storeId, String orderId, Recipe recipe) {
}
