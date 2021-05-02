package coffee.machine.models;

import java.util.Map;

public record Recipe(String id, String name, int preparationTime, Map<Ingredient, Integer> contents) {
}
