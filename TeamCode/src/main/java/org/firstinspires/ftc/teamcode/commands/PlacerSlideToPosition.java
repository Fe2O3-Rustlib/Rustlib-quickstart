package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.subsystems.PlacerSlide;
import org.rustlib.commandsystem.Command;

public class PlacerSlideToPosition extends Command {
    private final PlacerSlide subsystem;
    private final int position;


    public PlacerSlideToPosition(PlacerSlide slide, int position) {
        subsystem = slide;
        this.position = position;
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        subsystem.setTargetPosition(position);
    }

    @Override
    public void execute(){
        subsystem.runToPosition();
    }

    @Override
    public boolean isFinished() {
        return subsystem.atTargetPosition();
    }
}
