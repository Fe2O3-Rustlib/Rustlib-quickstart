package org.rustlib.core;

import com.qualcomm.ftccommon.FtcEventLoopBase;
import com.qualcomm.ftccommon.FtcEventLoopHandler;
import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl.DefaultOpMode;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.rustlib.logging.Logger;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class RobotControllerActivity extends FtcRobotControllerActivity {
    private static WeakReference<RobotControllerActivity> activeInstance = new WeakReference<>(null);
    private static final Set<Runnable> onOpModeStartCallbacks = new HashSet<>();
    private static final Set<Runnable> onOpModeStopCallbacks = new HashSet<>();
    private static final Timer opModeEventChecker = new Timer();
    private static final int eventCheckPeriod = 10;

    static {
        opModeEventChecker.scheduleAtFixedRate(new OpModeEventCheckerTask(), 0, eventCheckPeriod);
    }

    public RobotControllerActivity() {
        activeInstance = new WeakReference<>(this);
        RobotBase.mainActivity = new WeakReference<>(this);
    }

    boolean isOpModeRunning() {
        try {
            return !(eventLoop.getOpModeManager().getActiveOpMode() instanceof DefaultOpMode); // The DefaultOpMode class runs whenever a user op mode isn't running to shut down the motors
        } catch (NullPointerException e) {
            return false;
        }
    }

    static boolean opModeRunning() {
        if (activeInstance.get() == null) {
            return false;
        } else {
            return activeInstance.get().isOpModeRunning();
        }
    }

    public void reloadHardwareMap() throws RuntimeException {
        try {
            FtcEventLoopHandler eventLoopHandler;
            Field eventLoopHandlerField = FtcEventLoopBase.class.getDeclaredField("ftcEventLoopHandler");
            eventLoopHandler = (FtcEventLoopHandler) eventLoopHandlerField.get(eventLoop);
            HardwareFactory hardwareFactory;
            Field hardwareFactoryField = FtcEventLoopHandler.class.getDeclaredField("hardwareFactory");
            hardwareFactory = (HardwareFactory) hardwareFactoryField.get(eventLoopHandler);
            hardwareFactory.setXmlPullParser(cfgFileMgr.getActiveConfig().getXml());
            assert eventLoopHandler != null;
            eventLoop.getOpModeManager().setHardwareMap(eventLoopHandler.getHardwareMap(eventLoop.getOpModeManager()));
            cfgFileMgr.sendActiveConfigToDriverStation();
        } catch (Exception e) {
            Logger.log(e);
            throw new RuntimeException("Could not reload the hardware map.");
        }
    }

    public static void onOpModeStart(Runnable callback) {
        onOpModeStartCallbacks.add(callback);
    }

    public static void onOpModeStop(Runnable callback) {
        onOpModeStopCallbacks.add(callback);
    }

    private static class OpModeEventCheckerTask extends TimerTask {
        boolean lastOpModeRunning;

        public void run() {
            boolean opModeRunning = opModeRunning();
            if (lastOpModeRunning && !opModeRunning) {
                if (opModeRunning) {
                    onOpModeStartCallbacks.forEach(Runnable::run);
                } else {
                    onOpModeStopCallbacks.forEach(Runnable::run);
                }
            }
            lastOpModeRunning = opModeRunning;
        }
    }
}