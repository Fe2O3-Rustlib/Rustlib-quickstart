package org.rustlib.logging;

import static org.rustlib.logging.JsonKeys.LOG_MESSAGE_KEY;
import static org.rustlib.logging.MessageActions.NEW_LOG;
import static org.rustlib.rustboard.MessageActions.MESSAGE_ACTION_KEY;

import org.java_websocket.WebSocket;
import org.rustlib.rustboard.RustboardServer;

import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

public class Logger {
    private static final ArrayList<LogEntry> logEntries = new ArrayList<>();

    public static void log(String tag, String data) {
        logEntries.add(new LogEntry(tag, data));
    }

    public static void log(String tag, Exception e) {
        log(tag, e.getMessage());
    }

    private static void log(String tag, Object o) {
        log(tag, o.toString());
    }

    public static void log(String tag, JsonObject data) {
        logEntries.add(new LogEntry(tag, data));
    }

    public static void sendLog(WebSocket connection) {
        JsonArrayBuilder logEntryBuilder = Json.createArrayBuilder();
        logEntries.forEach((logEntry) -> logEntryBuilder.add(logEntry.getJson()));
        JsonObject message = Json.createObjectBuilder()
                .add(MESSAGE_ACTION_KEY, NEW_LOG)
                .add(LOG_MESSAGE_KEY, logEntryBuilder)
                .build();
        RustboardServer.sendToConnection(connection, message.toString());
    }
}
