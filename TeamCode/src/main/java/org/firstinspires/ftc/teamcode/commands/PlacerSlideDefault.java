package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.subsystems.PlacerSlide;
import org.rustlib.commandsystem.Command;

import java.util.function.DoubleSupplier;

public class PlacerSlideDefault extends Command {
    PlacerSlide slide;
    DoubleSupplier speedSupplier;

    public PlacerSlideDefault(PlacerSlide slide, DoubleSupplier speedSupplier) {
        this.slide = slide;
        this.speedSupplier = speedSupplier;
        addRequirements(this.slide);
    }

    @Override
    public void execute() {
        slide.mizoom(speedSupplier.getAsDouble());
    }
}