package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.commands.TiltIntake;
import org.firstinspires.ftc.teamcode.constants.SubsystemConstants;
import org.rustlib.commandsystem.Subsystem;
import org.rustlib.control.PIDController;
import org.rustlib.hardware.Encoder;
import org.rustlib.hardware.PairedEncoder;
import org.rustlib.rustboard.Rustboard;

public class Intake extends Subsystem {
//    public final DcMotor motor;
    private final PIDController controller;
    int targetPosition;
    public final Encoder encoder;
    public final DcMotor motor;
    public final CRServo crServo2;
    public final CRServo crServo1;
    public final Servo intGate;
    public final TouchSensor gateSensor;
    private double lastInput = 0;
    private double lastSpeed = 0;
    private double feedforward = 0;

    public Intake(HardwareMap hardwareMap) {

        this.motor = hardwareMap.dcMotor.get("intakeTilt");
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);

        encoder = new PairedEncoder(hardwareMap.get(DcMotor.class, "intakeTilt"), false);

        intGate = hardwareMap.servo.get("intakeGate");
        gateSensor = hardwareMap.touchSensor.get("gateSensor");

        this.crServo2 = hardwareMap.crservo.get("intakeServo2");
        crServo2.setDirection(DcMotorSimple.Direction.REVERSE);
        this.crServo1 = hardwareMap.crservo.get("intakeServo1");

        this.controller = new PIDController(0.0017, 0.0000008, 0.000003);
    }

    public void intTiltWithLim(double input){
        double calculatedSpeed;
        if (gamepadActive(input) && !(input > 0 && encoder.getTicks() > SubsystemConstants.Intake.maxIntakeTiltPosition) && !(input < 0 && encoder.getTicks() < SubsystemConstants.Intake.minIntakeTiltPosition)) { // If manual control is both requested and allowed
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
        runIntTiltMotor(calculatedSpeed);
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

    private boolean gamepadActive(double input) {
        return Math.abs(input) > 0.05;
    }

    public void runIntTiltMotor(double speed){
        motor.setPower(Range.clip(speed, -0.5, 0.5));
    }

    public void run(double speed) {
        crServo1.setPower(Range.clip(speed, -1.0, 1.0));
        crServo2.setPower(Range.clip(speed, -1.0, 1.0));
    }

    public void transferBlock(double position){
        intGate.setPosition(position);
    }

    public void SetIntakeTargetPosition(int targetPosition) {
        this.targetPosition = Range.clip(targetPosition, SubsystemConstants.Intake.minIntakeTiltPosition,SubsystemConstants.Intake.maxIntakeTiltPosition);
    }


    public boolean atTargetPosition() {
        return Math.abs(targetPosition - encoder.getTicks()) < SubsystemConstants.PlacerSlide.maxTargetError;
    }

    public void runIntakeToPosition() {
        motor.setTargetPosition(targetPosition);
        run(controller.calculate(encoder.getTicks(),targetPosition));
    }
}
