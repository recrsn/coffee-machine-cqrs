package coffee.machine.events;

import coffee.machine.models.Ingredient;

public record IngredientRefillEvent(String storeId, String coffeeMachineId, Ingredient ingredient, int quantity) {
}
