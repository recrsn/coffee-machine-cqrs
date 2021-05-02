package coffee.machine.termui;

import org.junit.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CommandTest {
    @Test
    public void shouldReturnValueWhenArgumentExists() {
        Command command = new Command("test", Map.of("foo", "bar"));
        assertEquals(Optional.of("bar"), command.argument("foo"));
    }

    @Test
    public void shouldReturnEmptyValueWhenArgumentDoesNotExist() {
        Command command = new Command("test", Map.of("foo", "bar"));
        assertTrue(command.argument("blah").isEmpty());
    }
}
