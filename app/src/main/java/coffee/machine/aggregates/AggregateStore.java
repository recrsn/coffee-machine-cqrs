package coffee.machine.aggregates;

import java.util.HashMap;
import java.util.Map;

public class AggregateStore {
    private final Map<Class<? extends Aggregate>, Map<String, Aggregate>> aggregateStore;

    @SafeVarargs
    public AggregateStore(Class<? extends Aggregate>... aggregates) {
        this.aggregateStore = new HashMap<>();

        for (var aggregate : aggregates) {
            aggregateStore.put(aggregate, new HashMap<>());
        }
    }

    public void add(String id, Aggregate aggregate) {
        Map<String, Aggregate> store = aggregateStore.get(aggregate.getClass());

        if (store == null) {
            throw new IllegalArgumentException("Unknown aggregate");
        }

        store.put(id, aggregate);
    }

    public <T extends Aggregate> T get(Class<T> tClass, String id) {
        var store = aggregateStore.get(tClass);
        if (store == null) {
            throw new IllegalArgumentException("Unknown aggregate");
        }

        var aggregate = store.get(id);

        return tClass.cast(aggregate);
    }
}
