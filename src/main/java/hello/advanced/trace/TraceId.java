package hello.advanced.trace;

import java.util.UUID;

public class TraceId {
    private final String id;
    private final Level level;

    private TraceId(String id, Level level) {
        this.id = id;
        this.level = level;
    }

    public TraceId() {
        this(createId(), new Level(0));
    }

    private static String createId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public TraceId createNextId() {
        return new TraceId(id, new Level(level.getLevel() + 1));
    }

    public TraceId createPreviousId() {
        return new TraceId(id, new Level(level.getLevel() - 1));
    }

    public boolean isFirstLevel() {
        return level.getLevel() == 0;
    }

    public String getId() {
        return id;
    }

    public Level getLevel() {
        return level;
    }
}
