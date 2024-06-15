package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class PusherBot extends LinearOpMode{
    DrivetrainPB myDriveTrain = null;


    //@Override

    public void runOpMode() throws InterruptedException {
        myDriveTrain = new DrivetrainPB(this);

        waitForStart();
        if (isStopRequested()) return;
        while (opModeIsActive()){
            //**************************************************************************************
            // ---------------------Gamepad 1 Controls ---------------------------------------------
            myDriveTrain.drive();

            //--------------------- TELEMETRY Code --------------------------------------------
            // Useful telemetry data in case needed for testing and to find heading of robot
            myDriveTrain.getTelemetryData();
            telemetry.update();
        }
    }
}