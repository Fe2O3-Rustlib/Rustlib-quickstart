package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.rustlib.commandsystem.Command;

import java.util.function.IntSupplier;

public class IntakeTiltToPosition extends Command {

    public Intake intake;
    public IntSupplier position;

    public IntakeTiltToPosition(Intake intake, IntSupplier position) {
        this.intake = intake;
        this.position = position;
        addRequirements(intake);
    }

    @Override
    public void initialize() {
        intake.SetIntakeTargetPosition(position.getAsInt());
    }

    @Override
    public void execute(){
        intake.runIntakeToPosition();
    }

    @Override
    public boolean isFinished() {
        return intake.atTargetPosition();
    }
}