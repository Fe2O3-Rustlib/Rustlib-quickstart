package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.rustlib.commandsystem.Subsystem;
import org.rustlib.hardware.Encoder;

public class IntakeSlide extends Subsystem {
    public Servo intSlideServo1;

    public Intake intake;

//    Encoder

    public IntakeSlide (HardwareMap hardwareMap, Intake intake){
        this.intSlideServo1 = hardwareMap.servo.get("intSlide");;

        this.intake = intake;
    }

    private static boolean gamepadActive(double input) {
        return Math.abs(input) > 0.05;
    }
//
//    public void mizoom(double input){
//        if (gamepadActive(input) && !()) {
//
//        }
//    }

    public void run(double speed){

    }
}
