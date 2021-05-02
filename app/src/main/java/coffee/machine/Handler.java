package coffee.machine;

import coffee.machine.aggregates.SlotUnavailableException;
import coffee.machine.commands.CreateCoffeeOrderCommand;
import coffee.machine.commands.InitializeCoffeeMachineCommand;
import coffee.machine.commands.RefillIngredientCommand;
import coffee.machine.mappers.RecipeListMapper;
import coffee.machine.models.Ingredient;
import coffee.machine.termui.Command;
import coffee.machine.termui.ResponseWriter;

import java.util.UUID;

public class Handler {
    /**
     * Coffee-machine is a singleton aggregate in our system
     * creating an aggregate allows you to scale the app to unlimited
     * coffee machines with little code change
     */
    public static final String COFFEE_MACHINE_ID = "coffee-machine-1";

    private final CommandGateway commandGateway;
    private final RecipeRegistry recipeRegistry;
    private final RecipeListMapper recipeListMapper;

    public Handler(CommandGateway commandGateway, RecipeRegistry recipeRegistry, RecipeListMapper recipeListMapper) {
        this.commandGateway = commandGateway;
        this.recipeRegistry = recipeRegistry;
        this.recipeListMapper = recipeListMapper;
    }

    public void init(Command command, ResponseWriter responseWriter) {
        command.argument("slots")
                .map(Integer::valueOf)
                .ifPresentOrElse(slots -> {

                    this.commandGateway.send(new InitializeCoffeeMachineCommand(COFFEE_MACHINE_ID, slots));
                    responseWriter.ok();
                }, () -> responseWriter.error(new Error(Error.Code.MISSING_ARGUMENT, "slots is required")));
    }

    public void listRecipes(Command __, ResponseWriter responseWriter) {
        responseWriter.message(recipeListMapper.serialize(recipeRegistry.findAll()));
    }

    public void makeCoffee(Command command, ResponseWriter responseWriter) {
        String orderId = UUID.randomUUID().toString();
        var recipeName = command.argument("recipe");

        if (recipeName.isEmpty()) {
            responseWriter.error(new Error(Error.Code.MISSING_ARGUMENT, "recipe"));
            return;
        }

        recipeName.flatMap(recipeRegistry::find)
                .ifPresentOrElse(recipe -> {
                    try {
                        this.commandGateway.send(new CreateCoffeeOrderCommand(orderId, COFFEE_MACHINE_ID, recipe));
                        responseWriter.ok();
                    } catch (SlotUnavailableException e) {
                        responseWriter.error(new Error(Error.Code.UNAVAILABLE, e.getMessage()));
                    }
                }, () -> responseWriter.error(new Error(Error.Code.UNKNOWN_RECIPE, recipeName.get())));

    }

    public void fillIngredient(Command command, ResponseWriter responseWriter) {
        var ingredientName = command.argument("name");

        if (ingredientName.isEmpty()) {
            responseWriter.error(new Error(Error.Code.MISSING_ARGUMENT, "name is required"));
            return;
        }

        var quantity = command.argument("quantity");

        if (quantity.isEmpty()) {
            responseWriter.error(new Error(Error.Code.MISSING_ARGUMENT, "quantity is required"));
            return;
        }


        try {
            Ingredient ingredient = Ingredient.valueOf(ingredientName.get());
            this.commandGateway.send(new RefillIngredientCommand(COFFEE_MACHINE_ID,
                    ingredient,
                    Integer.parseInt(quantity.get())));
            responseWriter.ok();
        } catch (IllegalArgumentException e) {
            responseWriter.error(new Error(Error.Code.UNKNOWN_INGREDIENT, ingredientName.get()));
        }
    }
}
