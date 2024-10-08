package org.rustlib.drive;

import com.google.gson.JsonParseException;

import org.rustlib.geometry.Rotation2d;
import org.rustlib.rustboard.RustboardServer;
import org.rustlib.utils.FileUtils;
import org.rustlib.utils.Future;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class Path implements Supplier<Path> {
    public final ArrayList<Supplier<Waypoint>> waypoints;
    final double timeout;

    public Path(double timeout, Supplier<Waypoint>... waypoints) {
        this.waypoints = new ArrayList<>();
        this.waypoints.addAll(Arrays.asList(waypoints));
        this.timeout = timeout;
    }

    public Path(Supplier<Waypoint>... waypoints) {
        this(Double.POSITIVE_INFINITY, waypoints);
    }

    private Path(Builder builder) {
        waypoints = builder.waypoints;
        timeout = builder.timeout;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    private static Rotation2d getRotation(String data) {
        double angle;
        try {
            angle = Double.parseDouble(data);
            return new Rotation2d(angle);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static double parseTimeout(String data) {
        try {
            return Double.parseDouble(data);
        } catch (NumberFormatException e) {
            return Double.POSITIVE_INFINITY;
        }
    }

    public static Path load(String fileName) {
        fileName = fileName.replace(" ", "_") + ".json";
        Builder pathBuilder = Path.getBuilder();
        try {
            JsonObject path = FileUtils.loadJsonObject(fileName);
            double timeout = parseTimeout(path.getString("timeout"));
            JsonArray array = path.getJsonArray("points");
            for (int i = 0; i < array.size(); i++) {
                JsonObject object = array.getJsonObject(i);
                JsonObject fieldVector = object.getJsonObject("fieldVector");
                double x = fieldVector.getJsonNumber("x").doubleValue();
                double y = fieldVector.getJsonNumber("y").doubleValue();
                double followRadius = object.getJsonNumber("followRadius").doubleValue();
                Rotation2d targetFollowRotation = getRotation(object.get("targetFollowRotation").toString());
                Rotation2d targetEndRotation = getRotation(object.get("targetEndRotation").toString());
                double maxVelocity;
                try {
                    maxVelocity = object.getJsonNumber("maxVelocity").doubleValue();
                } catch (NullPointerException e) {
                    maxVelocity = Double.POSITIVE_INFINITY;
                }

                pathBuilder.addWaypoint(new Waypoint(x, y, followRadius, targetFollowRotation, targetEndRotation, maxVelocity));
            }
            return pathBuilder.setTimeout(timeout).build();
        } catch (IOException | JsonParseException e) {
            RustboardServer.log(e.toString());
        }
        return new Path();
    }

    public Waypoint[] generateWaypoints() {
        ArrayList<Waypoint> generatedWaypoints = new ArrayList<>();
        waypoints.forEach((Supplier<Waypoint> waypoint) -> generatedWaypoints.add(waypoint.get()));
        return generatedWaypoints.toArray(new Waypoint[]{});
    }

    Waypoint[][] generateLineSegments() {
        ArrayList<Waypoint[]> segments = new ArrayList<>();
        for (int i = 0; i < waypoints.size() - 1; i++) {
            segments.add(new Waypoint[]{waypoints.get(i).get(), waypoints.get(i + 1).get()});
        }
        return segments.toArray(new Waypoint[][]{});
    }

    public double getTimeout() {
        return timeout;
    }

    public Path appendWaypoint(Supplier<Waypoint> waypoint, double timeout) {
        return join(new Path(timeout, waypoint));
    }

    public Path join(Path path) {
        return join(path, path.timeout + timeout);
    }

    /**
     * Creates a new path by joining the specified path to the end of this path instance.
     *
     * @param path
     * @param timeout
     * @return
     */
    public Path join(Path path, double timeout) {
        Builder builder = getBuilder().setTimeout(timeout);
        for (Supplier<Waypoint> waypoint : waypoints) {
            builder.addWaypoint(waypoint);
        }
        for (Supplier<Waypoint> waypoint : path.waypoints) {
            builder.addWaypoint(waypoint);
        }
        return builder.build();
    }

    public Path translateX(double x) {
        return translate(x, 0);
    }

    public Path translateY(double y) {
        return translate(0, y);
    }

    public Path translate(double x, double y) {
        Builder builder = getBuilder().setTimeout(timeout);
        for (Supplier<Waypoint> waypoint : waypoints) {
            builder.addWaypoint(new Future<>(() -> waypoint.get().translate(x, y)));
        }
        return builder.build();
    }

    public Path mirror() {
        Builder builder = getBuilder().setTimeout(timeout);
        for (Supplier<Waypoint> waypoint : waypoints) {
            builder.addWaypoint(new Future<>(() -> waypoint.get().mirror()));
        }
        return builder.build();
    }

    @Override
    public Path get() {
        return this;
    }

    public static class Builder {
        private final ArrayList<Supplier<Waypoint>> waypoints;

        private double defaultRadiusIn = 8;

        private double defaultMaxVelocity = Double.POSITIVE_INFINITY;

        private double timeout = Double.POSITIVE_INFINITY;

        private Builder() {
            waypoints = new ArrayList<>();
        }

        public Builder addWaypoint(Supplier<Waypoint> waypoint) {
            waypoints.add(waypoint);
            return this;
        }

        public Builder setDefaultRadius(double defaultRadiusIn) {
            this.defaultRadiusIn = defaultRadiusIn;
            return this;
        }

        public Builder setDefaultMaxVelocity(double defaultMaxVelocity) {
            this.defaultMaxVelocity = defaultMaxVelocity;
            return this;
        }

        public Builder addWaypoint(double x, double y) {
            waypoints.add(new Waypoint(x, y, defaultRadiusIn, defaultMaxVelocity));
            return this;
        }

        public Builder join(Path path) {
            path.waypoints.forEach((Supplier<Waypoint> waypoint) -> waypoints.add(waypoint));
            return this;
        }

        public Builder setTimeout(double timeout) {
            this.timeout = timeout;
            return this;
        }

        public Path build() {
            return new Path(this);
        }
    }
}
