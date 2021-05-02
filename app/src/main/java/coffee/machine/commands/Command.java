package coffee.machine.commands;

import coffee.machine.aggregates.Aggregate;

public interface Command<T extends Aggregate> {
    Class<T> target();

    String targetId();
}
