package coffee.machine.termui;

import coffee.machine.Error;
import coffee.machine.termui.spec.ArgumentSpec;
import coffee.machine.termui.spec.CommandSpec;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CommandParser {
    private final Reader reader;
    private final Map<String, CommandSpec> commandSpecMap;

    public CommandParser(Reader reader, List<CommandSpec> commandSpecList) {
        this.reader = reader;
        this.commandSpecMap = new HashMap<>();

        for (var command : commandSpecList) {
            commandSpecMap.put(command.name(), command);
        }
    }


    public static CommandSpec command(String name, String description, List<ArgumentSpec> argumentSpecs) {
        return new CommandSpec(name, description, argumentSpecs);
    }

    public static CommandSpec command(String name, String description) {
        return new CommandSpec(name, description, List.of());
    }

    public static List<ArgumentSpec> arguments(String... values) {
        if (values.length % 2 != 0) {
            throw new IllegalArgumentException("Expected argument, description pairs");
        }

        List<ArgumentSpec> argumentSpecs = new ArrayList<>();

        for (int i = 0; i < values.length; i += 2) {
            argumentSpecs.add(new ArgumentSpec(values[i], values[i + 1]));
        }

        return argumentSpecs;
    }

    public Stream<Result<Command>> commands() {
        BufferedReader br = new BufferedReader(this.reader);
        return br.lines()
                .map(String::strip)
                .filter(line -> line.length() > 0)
                .map(line -> {
                    var parts = line.split("\s+");
                    String command = parts[0].toLowerCase();

                    if (!commandSpecMap.containsKey(command)) {
                        return Result.error(Error.Code.COMMAND_NOT_FOUND, command);
                    }

                    CommandSpec commandSpec = commandSpecMap.get(command);
                    Map<String, String> arguments = new HashMap<>();

                    int i = 1;
                    for (var arg : commandSpec.arguments()) {
                        if (i >= parts.length) {
                            return Result.error(Error.Code.MISSING_ARGUMENT, arg.name());
                        }
                        arguments.put(arg.name(), parts[i++]);
                    }

                    return Result.success(new Command(parts[0], arguments));
                });
    }

}
