package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.constants.SubsystemConstants;
import org.rustlib.commandsystem.Subsystem;
import org.rustlib.control.PIDController;
import org.rustlib.hardware.PairedEncoder;
import org.rustlib.rustboard.Rustboard;

public class PlacerSlide extends Subsystem {
    public final DcMotor motor0;
    public final DcMotor motor1;
    public final PairedEncoder encoder;
    private final PIDController controller;
    public final TouchSensor limit;
    private final Placer placer;
    private double feedforward = 0;
    private int targetPosition = 0;
    private double lastSpeed = 0;
    private double lastInput = 0;

    public PlacerSlide(HardwareMap hardwareMap, Placer placer) {
        motor0 = hardwareMap.get(DcMotor.class, "placerSlide1");
        motor0.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor0.setDirection(DcMotorSimple.Direction.FORWARD);
        motor1 = hardwareMap.get(DcMotor.class, "placerSlide2");
        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor1.setDirection(DcMotorSimple.Direction.FORWARD);
        encoder = new PairedEncoder(hardwareMap.get(DcMotor.class, "placerSlide1"), false);
        encoder.reset();
        limit = hardwareMap.get(TouchSensor.class, "limit");
        this.placer = placer;
        controller = new PIDController(0.0017, 0.0000008, 0.000003);
    }

    private static boolean gamepadActive(double input) {
        return Math.abs(input) > 0.05;
    }

    public void mizoom(double input) {
        double calculatedSpeed;
        if (gamepadActive(input) && !(input > 0 && encoder.getTicks() > SubsystemConstants.PlacerSlide.maxExtensionPosition) && !(input < 0 && encoder.getTicks() < SubsystemConstants.PlacerSlide.minExtensionPosition)) { // If manual control is both requested and allowed
            calculatedSpeed = input + feedforward;
            lastInput = input;
        } else { // If automatic control is requested or manual control is not allowed
            if (gamepadActive(lastInput)) {
                targetPosition = encoder.getTicks();
            }
            calculatedSpeed = controller.calculate(encoder.getTicks(), targetPosition) + feedforward;
            if (encoder.getTicks() < 125 && targetPosition < 80) {
                calculatedSpeed -= 0.5;
            }
            lastInput = 0;
        }
        calculatedSpeed = applyAccelerationLimits(calculatedSpeed);
        Rustboard.updateTelemetryNode("slide speed", calculatedSpeed);
        drive(calculatedSpeed);
    }

    private double applyAccelerationLimits(double speed) {
        double accelMax = Rustboard.getDouble("slide accel", 0.5);
        if (speed > 0) {
            speed = Math.min(lastSpeed + accelMax, speed);
        } else {
            speed = Math.max(lastSpeed - accelMax, speed);
        }
        return speed;
    }

    public void drive(double speed) {
        if (limit.isPressed()) {
            encoder.reset();
            targetPosition = Math.max(targetPosition, 0);
        }

        motor0.setPower(Range.clip(speed, -SubsystemConstants.PlacerSlide.defaultSpeed, SubsystemConstants.PlacerSlide.defaultSpeed));
        motor1.setPower(Range.clip(speed, -SubsystemConstants.PlacerSlide.defaultSpeed, SubsystemConstants.PlacerSlide.defaultSpeed));
        lastSpeed = speed;
    }

    public void setTargetPosition(int targetPosition) {
        this.targetPosition = Range.clip(targetPosition, SubsystemConstants.PlacerSlide.minExtensionPosition,SubsystemConstants.PlacerSlide.maxExtensionPosition);
    }
    public int getTargetPosition(){
        return targetPosition;
    }
    public boolean atTargetPosition() {
        return Math.abs(targetPosition - encoder.getTicks()) < SubsystemConstants.PlacerSlide.maxTargetError;
    }

    public void runToPosition(){
        motor0.setTargetPosition(targetPosition);
        motor1.setTargetPosition(targetPosition);
        mizoom(controller.calculate(encoder.getPosition(),targetPosition));
    }

    @Override
    public void periodic() {
        if (limit.isPressed()) {
            encoder.reset();
            placer.close();
            targetPosition = Math.max(targetPosition, 0);
        }

        controller.setP(Rustboard.getDouble("slide kP", 0.0014));
        controller.setI(Rustboard.getDouble("slide kI", 0.0));
        controller.setD(Rustboard.getDouble("slide kD", 0.0008));
        feedforward = Rustboard.getDouble("slide feedforward", 0.2);
//        RustboardServer.log(controller.getGains().toString()); // TODO: add logging again
//        RustboardServer.log("feedforward: " + feedforward);
//        RustboardServer.setNodeValue("slide pose", encoder.getTicks());
//        RustboardServer.setNodeValue("slide velocity", encoder.ticksPerSecond());
//        RustboardServer.setNodeValue("slide target", targetPosition);
//        RustboardServer.setNodeValue("slide limit", limit.isPressed());
    }
}
