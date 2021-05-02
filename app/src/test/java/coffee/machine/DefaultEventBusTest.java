package coffee.machine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.function.Consumer;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class DefaultEventBusTest {
    @Mock
    private Consumer<KnownEvent> eventHandler;
    private DefaultEventBus eventBus;

    @Before
    public void setup() {
        eventBus = new DefaultEventBus();
        eventBus.addListener(KnownEvent.class, eventHandler);
    }

    @Test
    public void shouldCallEventHandlerOnReceivingKnownEvent() {
        var event = new KnownEvent();
        eventBus.emit(event);

        verify(eventHandler, only()).accept(event);
    }

    @Test
    public void shouldDoNothingOnReceivingUnknownEvent() {
        eventBus.emit(new UnknownEvent());
        verifyNoInteractions(eventHandler);
    }

    record KnownEvent() {
    }

    record UnknownEvent() {
    }
}
