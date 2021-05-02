package coffee.machine.events;

import coffee.machine.models.Ingredient;

public record IngredientLevelChangeEvent(String storeId, Ingredient ingredient, int newQuantity) {
}
