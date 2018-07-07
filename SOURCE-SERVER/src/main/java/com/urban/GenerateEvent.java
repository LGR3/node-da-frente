package com.urban;

import java.util.ArrayList;
import java.util.List;

import com.espertech.esper.client.EPServiceProvider;

public class GenerateEvent implements Runnable {
    private List<String> events;
    private int sleep;
    private EPServiceProvider engine;

    public GenerateEvent(EPServiceProvider engine, int seconds) {
        this.engine = engine;
        this.events = new ArrayList<String>();
        this.sleep = seconds * 1000;
    }

    public void addEvent(String event) {
        this.events.add(event);
    }

    public void run() {
        while (true) {
            // generate event
            if (this.events.size() > 0) {
                String event = this.events.get(0);
                this.events.remove(0);
                String name = event.split(";")[0];
                System.out.println("- gerando evento " + name);
                this.engine.getEPRuntime().sendEvent(new BusEvent(name, event));
            }
            try {
                Thread.currentThread().sleep(this.sleep);
            } catch (Exception e) {
            }
        }
    }

}