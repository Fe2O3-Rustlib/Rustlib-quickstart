package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Placer;
import org.firstinspires.ftc.teamcode.subsystems.PlacerSlide;
import org.rustlib.commandsystem.Command;

import java.util.function.DoubleSupplier;

public class TiltPlacer extends Command {
    Placer placer;
    DoubleSupplier speedSupplier;

    public TiltPlacer(Placer placer, DoubleSupplier speedSupplier) {
        this.placer = placer;
        this.speedSupplier = speedSupplier;
        addRequirements(this.placer);
    }

    @Override
    public void execute() {
        placer.runTilt(speedSupplier.getAsDouble());
    }
}
