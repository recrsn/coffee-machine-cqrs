package coffee.machine.events;

public record CoffeeBrewCompleteEvent(String slotId, String coffeeMachineId, String orderId) {
}
