package coffee.machine.events;

public record CoffeeMachineInitializationStartEvent(String coffeeMachineId, int slots) {
}
