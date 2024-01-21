package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp
public class FieldCentric_Comp_Bot extends LinearOpMode{
    private int selection = 0;
    private double botHeading;

    private static double RampDownPos = 0.39
    private static double RampStorePos = 0.37
    private static double RampUpPos = 0.0

    private boolean isRampDown = false
    private boolean isRampStore = false
    private boolean isRampUp = true
    
    DcMotorEx MTR_LA = (DcMotorEx) hardwareMap.dcMotor.get("left_viper_mtr");   // These varbiales do not match wiki documentation. Update.
    DcMotorEx MTR_RA = (DcMotorEx) hardwareMap.dcMotor.get("right_viper_mtr"); // See above.
    DcMotor MTR_LF = hardwareMap.dcMotor.get("left_front_mtr");
    DcMotor MTR_LB = hardwareMap.dcMotor.get("left_back_mtr");
    DcMotor MTR_RF = hardwareMap.dcMotor.get("right_front_mtr");
    DcMotor MTR_RB = hardwareMap.dcMotor.get("right_back_mtr");
    DcMotor MTR_I = hardwareMap.dcMotor.get("intake_mtr");
    IMU imu = hardwareMap.get(IMU.class, "imu");
    Servo SRV_R = hardwareMap.get(Servo.class, "ramp_srv");


/*
    TODO:
        1. Check IMU parameters (Done)
        2. Static variables for ramp position (Done)
        3. Global variables to check ramp position (Done)
        4. Implement arm height code (WIP)
        5. Fix sleep settings and fine tune during testing (WIP)
*/
    
    //Standard IMU Configuration
    IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
            RevHubOrientationOnRobot.LogoFacingDirection.UP,
            RevHubOrientationOnRobot.UsbFacingDirection.LEFT));

    @Override
    public void runOpMode() throws InterruptedException {
        boolean initialized = false;
        waitForStart();
        if (isStopRequested()) return;
        while (opModeIsActive()){
            //Initialize Robot
            if (!initialized){
                MTR_LF.setDirection(DcMotor.Direction.REVERSE);
                MTR_LB.setDirection(DcMotor.Direction.REVERSE);
                imu.initialize(parameters);
                SRV_R.setDirection(Servo.Direction.REVERSE);
                initialize();
                initialized = true;
            }

            drive();

            if(gamepad1.dpad_up){
                pickup();
            }
            if(gamepad1.dpad_down){
                storage();
            }
            if(gamepad1.dpad_left){
                up();
            }

            //---------------------Gamepad 2 Controls/Arm Movement----------------------
            //Hotkeys (Automation)
            if (gamepad2.y)
                selection = 1;
            if (gamepad2.b)
                selection = 2;
            if (gamepad2.x)
                selection = 3;
            if (gamepad2.a)
                selection = 4;

            // Show the elapsed game time and wheel power.
            //Useful telemetry data incase needed for testing and to find heading of robot
            telemetry.addData("Left Front: ", MTR_LF.getPower());
            telemetry.addData("Left Back: ", MTR_LB.getPower());
            telemetry.addData("Right Front: ", MTR_RF.getPower());
            telemetry.addData("Right Back: ", MTR_RB.getPower());
            telemetry.addData("Heading: ", ((int)Math.toDegrees(botHeading)) + " degrees");
            telemetry.update();
        }
    }
    public void drive(){
        //---------------------Gamepad 1 Controls/Drivetrain Movement----------------------

        double y = -gamepad1.left_stick_y; //Reversed Value
        double x = gamepad1.left_stick_x * 1.7 ; //The double value on the left is a sensitivity setting (change when needed)
        double rx = gamepad1.right_stick_x; //Rotational Value

        //Find the first angle (Yaw) to get the robot heading
        botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        //Translate to robot heading from field heading for motor values
        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        //Denominator is the largest motor power
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower = (rotY + rotX - rx) / denominator;

        MTR_LF.setPower(frontLeftPower);
        MTR_LB.setPower(backLeftPower);
        MTR_RF.setPower(frontRightPower);
        MTR_RB.setPower(backRightPower);
    }

    public void armMovement(int selection){
        //---------------------Gamepad 3 Controls/Arm Movement----------------------
        //Need code from aarush's laptop for this?
        System.out.println("filler");
    }

    public void initialize(){
        //---------------------Start of Match----------------------
        SRV_R.setPosition(RampStorePos); //110 degrees
        MTR_I.setPower(-0.5);
        sleep(1500);    // This might need to be a little longer...
        MTR_I.setPower(0);
        isRampDown = false
        isRampStore = true
        isRampUp = false
    }


    public void pickup(){
        //---------------------Gamepad 1 Controls/Intake Movement----------------------
        SRV_R.setPosition(RampDownPos);//115 degrees
        sleep(1000);
        MTR_I.setPower(1);
        isRampDown = true
        isRampStore = false
        isRampUp = false
    }


    public void storage(){
        //---------------------Gamepad 1 Controls/Ramp Movement----------------------
        MTR_I.setPower(0);
        SRV_R.setPosition(0.37); //110 degrees
        isRampDown = false
        isRampStore = true
        isRampUp = false
    }

    public void up(){
        //---------------------Gamepad 1 Controls/Ramp Movement----------------------
        SRV_R.setPosition(RampUpPos); //110 degrees
        isRampDown = false
        isRampStore = false
        isRampUp = true
    }
}

