package com.urban;

public class BusEvent {
    private String name;
    private String content;

    public BusEvent(String name, String content) {
        this.name = name;
        this.content = content;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}