package coffee.machine.projections;

import coffee.machine.events.IngredientLevelChangeEvent;
import coffee.machine.termui.ResponseWriter;

public class IngredientStoreProjection {
    private final ResponseWriter writer;

    public IngredientStoreProjection(ResponseWriter writer) {
        this.writer = writer;
    }

    public void onIngredientLevelChangeEvent(IngredientLevelChangeEvent event) {
        if (event.newQuantity() == 0) {
            writer.message(String.format("%s in store %s running low", event.ingredient(), event.storeId()));
        }
    }
}
