package org.firstinspires.ftc.teamcode.opmodes.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.opmodes.Robot;
import org.rustlib.core.OpModeCore;

@TeleOp(name = "MotorDebugger")
public class MotorDirectionDebugger extends Robot implements OpModeCore {

    @Override
    public void opModeInit() {

    }

    @Override
    public final void opModeLoop() {

        if(gamepad1.a)
        {
            drive.getBase().drive(0.5, 0, 0, 0,  0);
            telemetry.addData("Motor" , "LEFT FRONT");
        }
        else if(gamepad1.b)
        {
            drive.getBase().drive(0, 0.5, 0, 0,  0);
            telemetry.addData("Motor" , "RIGHT FRONT");
        }
        else if(gamepad1.x)
        {
            drive.getBase().drive(0, 0, 0, 0.5,  0);
            telemetry.addData("Motor" , "RIGHT BACK");
        }
        else if(gamepad1.y)
        {
            drive.getBase().drive(0, 0, 0.5, 0,  0);
            telemetry.addData("Motor" , "LEFT BACK");
        }
        else
        {
            drive.getBase().drive(0, 0, 0, 0,  0);
            telemetry.addData("Motor" , "");
        }
        telemetry.update();
    }
}
