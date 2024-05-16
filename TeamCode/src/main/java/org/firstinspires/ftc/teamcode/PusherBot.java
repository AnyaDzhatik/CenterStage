package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class PusherBot extends LinearOpMode{
    DrivetrainPB myDriveTrain = null;
    DronePB myDrone;

    //@Override

    public void runOpMode() throws InterruptedException {
        myDriveTrain = new DrivetrainPB(this);
        myDrone = new DronePB(this);

        waitForStart();
        if (isStopRequested()) return;
        while (opModeIsActive()){
            //**************************************************************************************
            // ---------------------Gamepad 1 Controls ---------------------------------------------
            myDriveTrain.drive();

            // ---------------------Gamepad 2 Controls ---------------------------------------------
            if(gamepad2.left_bumper && gamepad2.right_bumper){myDrone.launchDrone();}

            //--------------------- TELEMETRY Code --------------------------------------------
            // Useful telemetry data in case needed for testing and to find heading of robot
            myDriveTrain.getTelemetryData();
            telemetry.update();
        }
    }
}