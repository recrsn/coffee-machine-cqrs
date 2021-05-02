package coffee.machine.events;

import coffee.machine.models.Recipe;

public record CoffeeOrderReceiveEvent(String id, String coffeeMachineId, Recipe recipe) {
}
