package coffee.machine.events;

import coffee.machine.models.Recipe;

public record IngredientsConsumeEvent(String storeId, String slotId, String orderId,
                                      Recipe recipe) {
}
