package coffee.machine.aggregates;

import coffee.machine.models.Ingredient;

public class InsufficientIngredientException extends IllegalStateException {
    public InsufficientIngredientException(Ingredient ingredient) {
        super("Insufficient ingredient: " + ingredient);
    }
}
