package coffee.machine.events;

import coffee.machine.models.Recipe;

public record CoffeeBrewStartEvent(String slotId, String orderId, String storeId, Recipe recipe) {
}
