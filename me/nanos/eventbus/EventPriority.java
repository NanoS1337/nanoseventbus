package me.nanos.eventbus;

public enum EventPriority {
    LOWEST(0),
    LOW(1),
    NORMAL(2),
    HIGH(3),
    HIGHEST(4);

    private final int comparator;

    EventPriority(int comparator) {
        this.comparator = comparator;
    }

    public int getComparator() {
        return comparator;
    }
}
