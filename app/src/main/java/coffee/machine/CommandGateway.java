package coffee.machine;

import coffee.machine.aggregates.Aggregate;
import coffee.machine.aggregates.AggregateStore;
import coffee.machine.commands.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class CommandGateway {
    private final AggregateStore aggregateStore;
    private final Map<Class<? extends Command<? extends Aggregate>>, BiConsumer<? extends Aggregate, ? extends Command<? extends Aggregate>>> commandHandlers;

    public CommandGateway(AggregateStore aggregateStore, Map<Class<? extends Command<? extends Aggregate>>, BiConsumer<? extends Aggregate, ? extends Command<? extends Aggregate>>> commandHandlers) {
        this.aggregateStore = aggregateStore;
        this.commandHandlers = commandHandlers;
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("unchecked")
    public <A extends Aggregate, M extends Command<A>> void send(M command) {
        Class<A> target = command.target();
        A aggregate = aggregateStore.get(target, command.targetId());

        BiConsumer<A, M> handler = (BiConsumer<A, M>) commandHandlers.get(command.getClass());

        if (handler != null) {
            handler.accept(aggregate, command);
        }
    }

    public static class Builder {
        private final Map<Class<? extends Command<? extends Aggregate>>, BiConsumer<? extends Aggregate, ? extends Command<? extends Aggregate>>> commandHandlers = new HashMap<>();
        private AggregateStore aggregateStore;

        public <A extends Aggregate, T extends Command<A>> Builder handle(Class<T> commandClass, BiConsumer<A, T> handler) {
            this.commandHandlers.put(commandClass, handler);
            return this;
        }

        public Builder with(AggregateStore aggregateStore) {
            this.aggregateStore = aggregateStore;
            return this;
        }

        public CommandGateway build() {
            return new CommandGateway(aggregateStore, commandHandlers);
        }
    }
}
