package coffee.machine;

import coffee.machine.models.Recipe;

import java.util.List;
import java.util.Optional;

public class RecipeRegistry {
    private final List<Recipe> recipeList;

    public RecipeRegistry(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    public Optional<Recipe> find(String id) {
        return recipeList.stream().filter(recipe -> recipe.id().equals(id)).findAny();
    }

    public List<Recipe> findAll() {
        return recipeList;
    }
}
