package coffee.machine;

public interface EventBus {
    @SuppressWarnings("unchecked")
    <T> void emit(T message);
}
