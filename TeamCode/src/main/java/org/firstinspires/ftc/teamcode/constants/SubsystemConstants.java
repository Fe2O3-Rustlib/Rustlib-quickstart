package org.firstinspires.ftc.teamcode.constants;

public class SubsystemConstants {
    public static final boolean debugMode = false;

    public static final class ExtrudingArm {
        public static final double ServoRetracted = 0.0;//0.5
        public static final double ServoExtended = 0.5;//0.75
    }

    public static final class Intake {
        public static final double defaultSpeed = 1.0;
        public static final int minIntakeTiltPosition = -30;
        public static final int transferPosition = 0;
        public static final int IntakeTiltFeedPosition = 50;
        public static final int maxTargetError = 10;
        public static final int maxIntakeTiltPosition = 60;
        public static final double openGatePosition = 0.5;
        public static final double closeGatePosition = 0.0;



        public enum IntakeModes {
            INTAKE(defaultSpeed),
            OUTTAKE(-defaultSpeed),
            OFF(0.0);

            public final double speed;

            IntakeModes(double speed) {
                this.speed = speed;
            }
        }
    }

    public static final class IntakeSlide {
        public static final double stowedPosition = 0.65;
        public static final double outPosition = 0.01;

    }

    public static final class Slide {
        public static final int preparePlacerPosition = 350;
        public static final int stowPlacerPosition = 690;
        public static final int maxExtensionPosition = 1000;
        public static final int maxTargetError = 50;
        public static final int defaultPlacePosition = 760;
        public static final int stowedPosition = -100;
        public static final int autoPlacePosition = 550;
    }


    public static final class PlacerSlide {
        public static final double defaultSpeed = 1.0;
        public static final int PlacerSlideStowedPosition = 100;
        public static final int PlacerSlideMediumPosition = 12500;
        public static final int PlacerSlideHighPosition = 12500;
        public static final int maxTargetError = 200;
        public static final int maxExtensionPosition = 13000;
        public static final int minExtensionPosition = 75;

    }

    public static final class Placer {
        public static final double defaultSpeed = 0.5;
        public static final double lifter0PlacePosition = 0.1;
        public static final double lifter1PlacePosition = 0.7;
        public static final double lifter0StoragePosition = 0.3;
        public static final double lifter1StoragePosition = 0.5;
        public static final double openPosition = 0.45;
        public static final double closePosition = 0.9;
    }
}
