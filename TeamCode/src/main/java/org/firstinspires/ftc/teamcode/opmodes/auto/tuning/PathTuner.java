package org.firstinspires.ftc.teamcode.opmodes.auto.tuning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.opmodes.auto.AutonomousContainer;
import org.rustlib.commandsystem.Command;
import org.rustlib.commandsystem.PurePursuitAutonomousCommand;
import org.rustlib.drive.FollowPathCommand;
import org.rustlib.drive.Path;
import org.rustlib.geometry.Pose2d;

@Disabled
@Autonomous(name = "Path Tuner")
public class PathTuner extends AutonomousContainer {
    @Override
    public Command getAutonomousCommand() {
        return new PurePursuitAutonomousCommand(new Pose2d(), new FollowPathCommand(Path.load("red sample path"), drive));
    }
}
