package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.subsystems.Placer;
import org.rustlib.commandsystem.Command;

import java.util.function.DoubleSupplier;

public class PlacerGrip extends Command {
    public Placer placer;
    public DoubleSupplier positionSupplier;
    public PlacerGrip(Placer placer, DoubleSupplier position){
        this.placer = placer;
        this.positionSupplier = position;
        addRequirements(this.placer);
    }

    public void execute() {
        placer.setGripPosition(positionSupplier.getAsDouble());
    }
}
