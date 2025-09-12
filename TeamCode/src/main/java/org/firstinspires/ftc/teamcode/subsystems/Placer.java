package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.constants.SubsystemConstants;
import org.rustlib.commandsystem.Subsystem;

public class Placer extends Subsystem {
//    public final TouchSensor touchSensor;
    private final CRServo lifter0;
    private final CRServo lifter1;
    public final Servo placer;
//    public DistanceSensor distanceSensor;

    public Placer(HardwareMap hardwareMap) {
        lifter0 = hardwareMap.get(CRServo.class, "placerTilt1");
        lifter0.setDirection(CRServo.Direction.FORWARD);
        lifter0.close();
        lifter1 = hardwareMap.get(CRServo.class, "placerTilt2");
        lifter1.setDirection(CRServo.Direction.FORWARD);
        lifter1.close();
        placer = hardwareMap.get(Servo.class, "placer");
        placer.setDirection(Servo.Direction.FORWARD);
        placer.close();
//        distanceSensor = hardwareMap.get(DistanceSensor.class, "distanceSensor");
//        touchSensor = hardwareMap.get(TouchSensor.class, "placerTouch");
    }

    public void runTilt(double speed) {
        lifter0.setPower(Range.clip(speed, -SubsystemConstants.Placer.defaultSpeed, SubsystemConstants.Placer.defaultSpeed));
        lifter1.setPower(Range.clip(-speed, -SubsystemConstants.Placer.defaultSpeed, SubsystemConstants.Placer.defaultSpeed));
    }

//    public void storagePosition() {
//        lifter0.setPower(SubsystemConstants.Placer.lifter0PlacePosition);
//        lifter1.setPower(SubsystemConstants.Placer.lifter1PlacePosition);
//        placer.setPosition(SubsystemConstants.Placer.closePosition);
//    }
    public void setGripPosition(double position){
        placer.setPosition(position);
    }

    public void open() {
        placer.setPosition(SubsystemConstants.Placer.openPosition);
    }

    public void close() {
        placer.setPosition(SubsystemConstants.Placer.closePosition);
    }

//    public double getDistance() {
//        return distanceSensor.getDistance(DistanceUnit.INCH);
//    }

//    @Override
//    public void periodic() {
//        Rustboard.updateTelemetryNode("distance", getDistance());
//    }
}
