package coffee.machine.events;

import coffee.machine.models.Ingredient;

public record IngredientAddEvent(String storeId, String coffeeMachineId, Ingredient ingredient, int quantity) {
}
