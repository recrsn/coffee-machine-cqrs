package coffee.machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DefaultEventBus implements EventBus {
    protected final Map<Class<?>, List<Consumer<?>>> listenerMap;

    public DefaultEventBus() {
        this.listenerMap = new HashMap<>();
    }

    public <T> void addListener(Class<T> messageClass, Consumer<T> listener) {
        if (!listenerMap.containsKey(messageClass)) {
            listenerMap.put(messageClass, new ArrayList<>());
        }

        listenerMap.get(messageClass).add(listener);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void emit(T message) {
        List<Consumer<?>> listeners = listenerMap.get(message.getClass());

        if (listeners == null) {
            return;
        }

        listeners.forEach(listener -> ((Consumer<T>) listener).accept(message));
    }
}
