package org.rustlib.logging;

import static org.rustlib.logging.JsonKeys.DATA_KEY;
import static org.rustlib.logging.JsonKeys.HAS_JSON_KEY;
import static org.rustlib.logging.JsonKeys.TAG_KEY;
import static org.rustlib.logging.JsonKeys.TIME_KEY;
import static org.rustlib.logging.MessageActions.LOG_ENTRY;
import static org.rustlib.rustboard.MessageActions.MESSAGE_ACTION_KEY;

import org.rustlib.rustboard.RustboardServer;
import org.rustlib.rustboard.Time;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class LogEntry {
    private final Time time;
    private final String tag;
    private final boolean hasJson;
    private final String data;
    private final JsonObject jsonData;

    public static class LogEntryTags {
        public final String ERROR = "E";
        public final String WARNING = "W";
        public final String MESSAGE = "M";
    }

    public LogEntry(String tag, String message) {
        time = Time.now();
        this.tag = tag;
        hasJson = false;
        data = message;
        jsonData = null;
        sendToClient();
    }

    public LogEntry(String tag, JsonObject message) {
        time = Time.now();
        this.tag = tag;
        hasJson = true;
        data = null;
        jsonData = message;
        sendToClient();
    }

    public JsonObject getJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder()
                .add(TIME_KEY, time.getTimeMS()) // getJson() builds a new JsonObject each time to ensure that the time.getTimeMS() value is calibrated
                .add(TAG_KEY, tag)
                .add(HAS_JSON_KEY, hasJson);
        if (hasJson) {
            builder.add(DATA_KEY, jsonData);
        } else {
            builder.add(DATA_KEY, data);
        }
        return builder.build();
    }

    void sendToClient() {
        JsonObject json = Json.createObjectBuilder()
                .add(MESSAGE_ACTION_KEY, LOG_ENTRY)
                .add(JsonKeys.LOG_MESSAGE_KEY, getJson())
                .build();
        RustboardServer.getInstance().broadcastToClients(json.toString());
    }
}
