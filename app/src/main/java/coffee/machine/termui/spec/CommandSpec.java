package coffee.machine.termui.spec;

import java.util.List;

public record CommandSpec(String name, String description, List<ArgumentSpec> arguments) {
}
