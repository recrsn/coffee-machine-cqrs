package coffee.machine.projections;

import coffee.machine.events.CoffeeOrderStatusUpdateEvent;
import coffee.machine.termui.ResponseWriter;

public class CoffeeOrderProjection {
    private final ResponseWriter writer;

    public CoffeeOrderProjection(ResponseWriter writer) {
        this.writer = writer;
    }

    public void onCoffeeOrderStatusUpdateEvent(CoffeeOrderStatusUpdateEvent event) {
        writer.message(switch (event.status()) {
            case REQUESTED -> "requested";
            case ASSIGNED -> String.format("Order %s assigned slot %s", event.id(), event.slotId());
            case CANCELLED -> String.format("Order %s is cancelled", event.id());
            case BREWING -> String.format("Order %s is brewing", event.id());
            case READY -> String.format("Order %s is ready", event.id());
        });
    }
}
