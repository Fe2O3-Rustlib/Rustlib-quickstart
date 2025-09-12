package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.Robot;
import org.rustlib.core.OpModeCore;
import org.rustlib.rustboard.Rustboard;

@Autonomous(name = "TimedLeftPark")
public class TimedLeftPark extends Robot implements OpModeCore {

    @Override
    public void opModeStart(){
        runtime.startTime();
        runtime.reset();
        leftTimedAutoPark.schedule();
    }

    @Override
    public void opModeLoop() {
        telemetry.addData("Runtime", runtime.seconds());
        telemetry.addData("XPosition", drive.getOdometry().getPosition().x);
        telemetry.addData("YPosition", drive.getOdometry().getPosition().y);
        telemetry.addData("heading", drive.getOdometry().getPosition().rotation.getAngleDegrees());
        telemetry.addData("Input 1", Rustboard.getString("input_1", ""));
        telemetry.addData("active rustboard uuid", Rustboard.getActiveRustboard().getUuid());
        //Rustboard.notifyActiveClient("op mode running", NoticeType.POSITIVE);
    }

}
