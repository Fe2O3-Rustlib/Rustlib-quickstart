package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.subsystems.IntakeSlide;
import org.firstinspires.ftc.teamcode.subsystems.Slide;
import org.rustlib.commandsystem.Command;

import java.util.function.DoubleSupplier;

public class IntakeSlideDefault extends Command {
    IntakeSlide slide;
    DoubleSupplier speedSupplier;

    public IntakeSlideDefault(IntakeSlide slide, DoubleSupplier speedSupplier) {
        this.slide = slide;
        this.speedSupplier = speedSupplier;
        addRequirements(this.slide);
    }

    @Override
    public void execute() {
        slide.run(speedSupplier.getAsDouble());
    }
}
