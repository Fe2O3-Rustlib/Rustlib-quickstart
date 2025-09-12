package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.rustlib.commandsystem.Command;

import java.util.function.DoubleSupplier;

public class TiltIntake extends Command {
    Intake intake;
    DoubleSupplier speedSupplier;

    public TiltIntake(Intake intake, DoubleSupplier speedSupplier) {
        this.intake = intake;
        this.speedSupplier = speedSupplier;
        addRequirements(this.intake);
    }

    @Override
    public void execute() {
        intake.runIntTiltMotor(speedSupplier.getAsDouble());
    }
}
