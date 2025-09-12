package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Placer;
import org.rustlib.commandsystem.Command;

import java.util.function.DoubleSupplier;

public class TransferBlock extends Command {
    public Intake intake;
    public DoubleSupplier positionSupplier;
    public TransferBlock(Intake intake, DoubleSupplier position){
        this.intake = intake;
        this.positionSupplier = position;
        addRequirements(this.intake);
    }

    public void execute() {
        intake.transferBlock(positionSupplier.getAsDouble());
    }
}
