package coffee.machine.aggregates;

import coffee.machine.EventBus;
import coffee.machine.commands.AddIngredientCommand;
import coffee.machine.commands.InitializeIngredientStoreCommand;
import coffee.machine.commands.ProvideCoffeeIngredientsCommand;
import coffee.machine.events.IngredientAddEvent;
import coffee.machine.events.IngredientLevelChangeEvent;
import coffee.machine.events.IngredientRefillEvent;
import coffee.machine.events.IngredientStoreInitializeEvent;
import coffee.machine.events.IngredientsConsumeEvent;
import coffee.machine.models.Ingredient;

import java.util.EnumMap;
import java.util.Map;

public class IngredientsStoreAggregate implements Aggregate {
    private final EventBus eventBus;
    public Map<Ingredient, Integer> ingredients = new EnumMap<>(Ingredient.class);
    private String id;
    private String coffeeMachineId;

    public IngredientsStoreAggregate(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void init(InitializeIngredientStoreCommand command) {
        this.eventBus.emit(new IngredientStoreInitializeEvent(command.storeId(), command.coffeeMachineId()));
    }

    public void onIngredientStoreInitializeEvent(IngredientStoreInitializeEvent event) {
        this.id = event.storeId();
        this.coffeeMachineId = event.coffeeMachineId();
    }

    public void onAddIngredientCommand(AddIngredientCommand command) {
        this.eventBus.emit(new IngredientAddEvent(this.id,
                this.coffeeMachineId,
                command.ingredient(),
                command.quantity()));

    }

    public void onIngredientAddEvent(IngredientAddEvent event) {
        ingredients.merge(event.ingredient(), event.quantity(), Integer::sum);
        int newQuantity = ingredients.get(event.ingredient());

        this.eventBus.emit(new IngredientLevelChangeEvent(this.id, event.ingredient(), newQuantity));
    }

    public void onProvideCoffeeIngredients(ProvideCoffeeIngredientsCommand command) {
        command.recipe().contents().forEach((ingredient, qty) -> {
            Integer currentQuantity = ingredients.get(ingredient);
            if (currentQuantity == null || currentQuantity < qty) {
                throw new InsufficientIngredientException(ingredient);
            }
        });


        this.eventBus.emit(new IngredientsConsumeEvent(command.id(),
                command.slotId(),
                command.orderId(),
                command.recipe()));
    }

    public void onIngredientConsumeEvent(IngredientsConsumeEvent event) {
        event.recipe().contents().forEach((ingredient, quantity) -> {
            int currentQuantity = ingredients.get(ingredient);
            int newQuantity = currentQuantity - quantity;
            ingredients.put(ingredient, newQuantity);

            this.eventBus.emit(new IngredientLevelChangeEvent(this.id, ingredient, newQuantity));

        });
    }


    @Override
    public String id() {
        return id;
    }
}
