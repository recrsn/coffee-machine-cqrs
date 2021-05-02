package coffee.machine.events;

public record CoffeeBrewCancelEvent(String slotId, String coffeeMachineId, String orderId) {
}
