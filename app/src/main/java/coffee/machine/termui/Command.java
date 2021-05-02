package coffee.machine.termui;

import java.util.Map;
import java.util.Optional;

public record Command(String name, Map<String, String> args) {
    public Optional<String> argument(String key) {
        return Optional.ofNullable(args.get(key));
    }
}
