package com.dafttech.eventmanager;

import java.util.List;

public class AsyncEventThread extends Thread {
    private volatile Event event;
    private volatile List<EventListenerContainer> eventListenerContainers;

    protected AsyncEventThread(Event event, List<EventListenerContainer> eventListenerContainers) {
        this.event = event;
        this.eventListenerContainers = eventListenerContainers;
        start();
    }

    @Override
    public final void run() {
        event.schedule(eventListenerContainers);
    }
}
