package coffee.machine.mappers;

import coffee.machine.models.Recipe;

import java.util.List;

public class RecipeListMapper {
    public String serialize(List<Recipe> recipeList) {
        StringBuilder builder = new StringBuilder();

        for (var recipe : recipeList) {
            builder.append(String.format("%s: %s (%d seconds)\n",
                    recipe.id(),
                    recipe.name(),
                    recipe.preparationTime() / 1000));
        }

        return builder.toString().strip();
    }
}
