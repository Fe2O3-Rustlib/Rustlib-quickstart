package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSlide;
import org.rustlib.commandsystem.Command;

public class IntakeSlideToPosition extends Command {

    public IntakeSlide intakeSlide;
    public double targetPosition;

    public IntakeSlideToPosition(IntakeSlide intakeSlide, double targetPosition) {
        this.intakeSlide = intakeSlide;
        this.targetPosition = targetPosition;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {intakeSlide.intSlideServo1.setPosition(targetPosition);}

//    @Override
//    public boolean isFinished() {
//        return intake.atTargetPosition();
//    }
}
