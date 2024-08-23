package org.rustlib.logging;

import static org.rustlib.logging.JsonKeys.DATA_KEY;
import static org.rustlib.logging.JsonKeys.HAS_JSON_KEY;
import static org.rustlib.logging.JsonKeys.TAG_KEY;
import static org.rustlib.logging.JsonKeys.TIME_KEY;

import org.rustlib.rustboard.RustboardServer;
import org.rustlib.rustboard.Time;

import javax.json.Json;
import javax.json.JsonObject;

public class LogEntry {
    private final JsonObject json;

    public static class LogEntryTags {
        public final String ERROR = "E";
        public final String WARNING = "W";
        public final String MESSAGE = "M";
    }

    public LogEntry(String tag, String message) {
        json = Json.createObjectBuilder()
                .add(TIME_KEY, Time.now().getTimeMS())
                .add(TAG_KEY, tag)
                .add(HAS_JSON_KEY, false)
                .add(DATA_KEY, message)
                .build();
        RustboardServer.getInstance().broadcastToClients(json.toString());
    }

    public LogEntry(String tag, JsonObject data) {
        json = Json.createObjectBuilder()
                .add(TIME_KEY, Time.now().getTimeMS())
                .add(TAG_KEY, tag)
                .add(HAS_JSON_KEY, true)
                .add(DATA_KEY, data)
                .build();
        RustboardServer.getInstance().broadcastToClients(json.toString());
    }

    public JsonObject getJson() {
        return json;
    }
}
