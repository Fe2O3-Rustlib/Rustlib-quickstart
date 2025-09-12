package org.firstinspires.ftc.teamcode.opmodes.tele;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.DriveDefault;
import org.firstinspires.ftc.teamcode.commands.IntakeSlideToPosition;
import org.firstinspires.ftc.teamcode.commands.IntakeTiltToPosition;
import org.firstinspires.ftc.teamcode.commands.PlacerGrip;
import org.firstinspires.ftc.teamcode.commands.PlacerSlideDefault;
import org.firstinspires.ftc.teamcode.commands.PlacerSlideToPosition;
import org.firstinspires.ftc.teamcode.commands.RunIntake;
import org.firstinspires.ftc.teamcode.commands.TiltIntake;
import org.firstinspires.ftc.teamcode.commands.TiltPlacer;
import org.firstinspires.ftc.teamcode.commands.TransferBlock;
import org.firstinspires.ftc.teamcode.constants.SubsystemConstants;
import org.firstinspires.ftc.teamcode.opmodes.Robot;
import org.rustlib.commandsystem.InstantCommand;
import org.rustlib.commandsystem.SequentialCommandGroup;
import org.rustlib.commandsystem.Trigger;
import org.rustlib.core.OpModeCore;
import org.rustlib.geometry.Pose2d;
import org.rustlib.rustboard.NoticeType;
import org.rustlib.rustboard.Rustboard;

@TeleOp(name = "TeleOp")
public class Tele extends Robot implements OpModeCore {
    public static Pose2d backdropPose = blueBackdropPose;
    public static Pose2d parkPose = leftParkPose;

    @Override
    public void opModeInit() {
        Rustboard.updateInputNode("input_1", "Hello from the robot");
        if (alliance == Alliance.RED) {
            backdropPose = redBackdropPose;
        }
        placerSlide.encoder.reset();
        intake.encoder.reset();
        drive.enableFastMode();
    }
        public void opModeStart() {

        // Player #1
        drive.setDefaultCommand(new DriveDefault(drive, () -> -controller1.leftStickY.getAsDouble(), () -> controller1.leftStickX.getAsDouble(), () -> -controller1.rightStickX.getAsDouble()));

        controller1.b.and(controller1.x).and(controller1.y).onTrue(new InstantCommand(() -> drive.getOdometry().setPosition(new Pose2d())));
        controller1.a.and(controller1.b).andNot(controller1.x).andNot(controller1.y).onTrue(new InstantCommand(() -> drive.enableFastMode()));
        controller1.a.and(controller1.x).andNot(controller1.b).andNot(controller1.y).onTrue(new InstantCommand(() -> drive.enableSlowMode()));

        controller1.leftTrigger.andNot(controller1.rightTrigger).onTrue(new RunIntake(intake, SubsystemConstants.Intake.IntakeModes.INTAKE.speed));
        controller1.rightTrigger.andNot(controller1.leftTrigger).onTrue(new RunIntake(intake, SubsystemConstants.Intake.IntakeModes.OUTTAKE.speed));
        controller1.leftTrigger.andNot(controller1.rightTrigger).onFalse(new RunIntake(intake, SubsystemConstants.Intake.IntakeModes.OFF.speed));
        controller1.rightTrigger.andNot(controller1.leftTrigger).onFalse(new RunIntake(intake, SubsystemConstants.Intake.IntakeModes.OFF.speed));

        controller1.dpadUp.andNot(controller1.dpadDown).onTrue(new TiltIntake(intake, () -> -1.0));
        controller1.dpadDown.andNot(controller1.dpadUp).onTrue(new TiltIntake(intake, () -> 1.0));
        controller1.dpadUp.andNot(controller1.dpadDown).onFalse(new TiltIntake(intake, () -> 0));
        controller1.dpadDown.andNot(controller1.dpadUp).onFalse(new TiltIntake(intake, () -> 0));



//            controller1.dpadUp.andNot(controller2.dpadDown).onTrue(new IntakeTiltToPosition(intake,() -> SubsystemConstants.Intake.transferPosition));
//            controller1.dpadUp.andNot(controller2.dpadDown).onTrue(new IntakeTiltToPosition(intake,() -> SubsystemConstants.Intake.transferPosition));

        controller1.leftBumper.andNot(controller2.rightBumper).onTrue(new IntakeSlideToPosition(intakeSlide, SubsystemConstants.IntakeSlide.stowedPosition));// In
        controller1.rightBumper.andNot(controller2.leftBumper).onTrue(new IntakeSlideToPosition(intakeSlide, SubsystemConstants.IntakeSlide.outPosition));// Out

        controller1.dpadLeft.andNot(controller1.dpadRight).onTrue( new TransferBlock(intake,() -> SubsystemConstants.Intake.openGatePosition));
        controller1.dpadRight.andNot(controller1.dpadLeft).onTrue( new TransferBlock(intake,() -> SubsystemConstants.Intake.closeGatePosition));

        // Automatic sequences and resets
        new Trigger(() -> intake.gateSensor.isPressed()).onTrue(new SequentialCommandGroup(new IntakeSlideToPosition(intakeSlide, 1.0), new TransferBlock(intake, () ->  SubsystemConstants.Intake.openGatePosition), new PlacerGrip(placer,()->SubsystemConstants.Placer.openPosition),new IntakeTiltToPosition(intake, () -> SubsystemConstants.Intake.transferPosition)));
//        new Trigger(() -> placerSlide.limit.isPressed()).onFalse(new InstantCommand(() -> placerSlide.encoder.reset()));

        // Player #2
        controller2.dpadLeft.andNot(controller2.dpadRight.or(controller1.dpadLeft.or(controller1.dpadRight))).onTrue( new TransferBlock(intake,() -> SubsystemConstants.Intake.closeGatePosition));
        controller2.dpadRight.andNot(controller2.dpadLeft.or(controller1.dpadLeft.or(controller1.dpadRight))).onTrue( new TransferBlock(intake,() -> SubsystemConstants.Intake.openGatePosition));

        controller2.dpadDown.andNot(controller2.dpadUp).onTrue( new TiltPlacer(placer, () -> -1.0));
        controller2.dpadUp.andNot(controller2.dpadDown).onTrue( new TiltPlacer(placer, () -> 1.0));
        controller2.dpadDown.andNot(controller2.dpadUp).onFalse( new TiltPlacer(placer, () -> 0));
        controller2.dpadUp.andNot(controller2.dpadDown).onFalse( new TiltPlacer(placer, () -> 0));

        controller2.a.andNot(controller2.b).onTrue( new PlacerGrip(placer,() -> SubsystemConstants.Placer.openPosition));
        controller2.b.andNot(controller2.a).onTrue( new PlacerGrip(placer,() -> SubsystemConstants.Placer.closePosition));

        placerSlide.setDefaultCommand(new PlacerSlideDefault(placerSlide, () -> -controller2.leftStickY.getAsDouble()));

        controller2.leftBumper.andNot(controller2.rightBumper).onTrue(new PlacerSlideToPosition(placerSlide, SubsystemConstants.PlacerSlide.PlacerSlideStowedPosition));
        controller2.rightBumper.andNot(controller2.leftBumper).onTrue(new PlacerSlideToPosition(placerSlide, SubsystemConstants.PlacerSlide.PlacerSlideHighPosition));
    }

    @Override
    public void opModeLoop() {
        Rustboard.notifyActiveClient("op mode running", NoticeType.POSITIVE);
        telemetry.addData("intakeTouch", intake.gateSensor.isPressed());
        telemetry.addData("intTilt Pose",intake.encoder.getTicks());
        telemetry.addData("Robot rotation:", drive.getOdometry().getPosition().rotation);
        telemetry.addData("Robot X position:", drive.getOdometry().getPosition().x);
        telemetry.addData("Robot Y position:", drive.getOdometry().getPosition().y);
        telemetry.addData("Robot BackEncoder:", drive.getOdometry().backEncoder.getTicks());
        telemetry.addData("Robot LeftEncoder:", drive.getOdometry().leftEncoder.getTicks());
        telemetry.addData("Robot RightEncoder:", drive.getOdometry().rightEncoder.getTicks());
        telemetry.addData("Placer Grip Position", placer.placer.getPosition());
    }
}

