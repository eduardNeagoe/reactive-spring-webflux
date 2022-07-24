package com.reactivespring.sinks;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Sinks;

public class SinksTest {

    @Test
    void sink() {
        // replays all the events to new subscribers, from start to finish
        Sinks.Many<Integer> replaySinks = Sinks.many().replay().all();

        // would replay only the latest event to all the subscribers
        // Sinks.Many<Integer> replaySinks = Sinks.many().replay().latest();

        // 1 -publish events
        // try emitting a non-null element
        Sinks.EmitResult emitResult = replaySinks.tryEmitNext(1);
        // prints OK
        System.out.println("emitResult :  " + emitResult);

        // if a failure occurs while emitting events, the stream will fail fast - no retry
        replaySinks.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

        // 2 - subscribe to events
        // both the subscriber will receive events 1 and 2
        replaySinks.asFlux()
                .subscribe(s -> System.out.println("Subscriber 1: " + s));
        replaySinks.asFlux()
                .subscribe(s -> System.out.println("Subscriber 2: " + s));

        // will trigger the print of event 3 for both subscribers
        replaySinks.tryEmitNext(3);
    }


    @Test
    void sink_multicast() {
        // It can hold up to 256 elements by default
        // remembers elements pushed via Sinks.Many.tryEmitNext(Object) before the first Subscriber is registered
        Sinks.Many<Integer> multiCast = Sinks.many().multicast().onBackpressureBuffer();

        // publish
        multiCast.tryEmitNext(1);
        multiCast.tryEmitNext(2);

        // subscribe
        // will receive all the events because it's the first subscriber
        multiCast.asFlux()
                .subscribe(s -> System.out.println("Subscriber 1 : " + s));

        multiCast.tryEmitNext(3);

        // will only receive elements emitted after this point => only receives event 4
        multiCast.asFlux()
                .subscribe(s -> System.out.println("Subscriber 2 : " + s));

        multiCast.tryEmitNext(4);
    }

    @Test
    void sink_unicast() {
        Sinks.Many<Integer> multiCast = Sinks.many().unicast().onBackpressureBuffer();

        // publish
        multiCast.tryEmitNext(1);
        multiCast.tryEmitNext(2);

        // subscribe
        // will receive all the events
        multiCast.asFlux()
                .subscribe(s -> System.out.println("Subscriber 1 : " + s));

        multiCast.tryEmitNext(3);

        // would throw an exception because unicast allows a single subscriber only
        // java.lang.IllegalStateException: UnicastProcessor allows only a single Subscriber
        // multiCast.asFlux()
        // .subscribe(s -> System.out.println("Subscriber 2 : " + s));

        multiCast.tryEmitNext(4);
    }


}
