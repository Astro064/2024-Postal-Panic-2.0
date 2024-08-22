package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp (name = "Mecanum TeleOp", group = "LinearOpMode")
public class FieldCentricMecanumTeleOp extends LinearOpMode {

    private CRServo ArmAxonCR;
    private Servo ClawAxon;

    private DcMotor frontRightMotor;


    @Override
    public void runOpMode() throws InterruptedException {
        // Declare motors
        int DesiredAngle1;
        int DesiredAngle2;
        int DesiredAngle3;
        double DesiredAngle4;
        int DeadBand;
       // int Arm_Pos;
        double servoPower = 0;
        int WhichAngle = 0;
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("backRightMotor");
        ArmAxonCR = hardwareMap.get(CRServo.class, "ArmAxonCR");
        ClawAxon = hardwareMap.get(Servo.class, "ClawAxon");
        // Find a motor in the hardware map named "frontRightMotor"
        DcMotor motor = hardwareMap.dcMotor.get("frontRightMotor");



        DesiredAngle1 = -250;
        DesiredAngle2 = -180;
        DesiredAngle3 = -75;
        DesiredAngle4 = -0.1;
        DeadBand = 1;
        ArmAxonCR.setDirection(CRServo.Direction.REVERSE);

//Reset the motor encoder so that it reads zero ticks
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Turn the motor back on, required if you use STOP_AND_RESET_ENCODER
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);



        // Reverse the right side motors. This may be wrong.
        // If robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // Create an object to receive the IMU angles
       /* YawPitchRollAngles robotOrientation;
        robotOrientation = imu.getRobotYawPitchRollAngles();

        // Create angular velocity array variable
        AngularVelocity myRobotAngularVelocity;
*/
        // Retrieve the IMU from the hardware map
        IMU imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);



        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = gamepad1.left_stick_y; // Y stick value is reversed
            double x = -gamepad1.left_stick_x;
            double rx = -gamepad1.right_stick_x;

            double CPR = 8192;

            // Get the current position of the motor
            int position = motor.getCurrentPosition();
            double revolutions = position/CPR;

            double angle = revolutions * 360;

            double Arm_Pos = -angle;



            // This button choice was made so that it is hard to hit on accident,
            // it can be freely changed based on preference.
            // The equivalent button is start on some controllers.
            if (gamepad1.options) {
                imu.resetYaw();
            }

            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            // Rotate the movement direction counter to the bot's rotation
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            rotX = rotX * 1.1;  // Counteract imperfect strafing

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;

            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);



            if (gamepad1.right_bumper) {
                ClawAxon.setPosition(0.3);
            } else if (gamepad1.left_bumper) {
                ClawAxon.setPosition(0);
            }
            if (gamepad1.x) {
                ArmAxonCR.setPower(1 * (1 - Arm_Pos / DesiredAngle1));
                servoPower = 1 * (1 - Arm_Pos / DesiredAngle1);
                WhichAngle = 1;
            } else if (gamepad1.a) {
                ArmAxonCR.setPower(1 * (1 - Arm_Pos / DesiredAngle2));
                servoPower = 1 * (1 - Arm_Pos / DesiredAngle2);
                WhichAngle = 2;
            } else if (gamepad1.b) {
                ArmAxonCR.setPower(0.5 + 1 * (1 - Arm_Pos / DesiredAngle3));
                servoPower = 0.5 + 1 * (1 - Arm_Pos / DesiredAngle3);
                WhichAngle = 3;
            } else if (gamepad1.y) {
                if (Arm_Pos < -20) {
                    ArmAxonCR.setPower(0.001 * (1 - Arm_Pos / DesiredAngle4));
                    servoPower = 0.001 * (1 - Arm_Pos / DesiredAngle4);
                    WhichAngle = 4;
                } else {
                    ArmAxonCR.setPower(0);
                    servoPower = 0;
                    WhichAngle = 4;
                }
            } else {
                if (WhichAngle == 1) {
                    ArmAxonCR.setPower(1 * (1 - Arm_Pos / DesiredAngle1));
                } else if (WhichAngle == 2) {
                    ArmAxonCR.setPower(1 * (1 - Arm_Pos / DesiredAngle2));
                } else if (WhichAngle == 3) {
                    ArmAxonCR.setPower(0.5 * (1 - Arm_Pos / DesiredAngle3));
                } else if (WhichAngle == 4) {
                    if (Arm_Pos < -20) {
                        ArmAxonCR.setPower(0.001 * (1 - Arm_Pos / DesiredAngle4));
                        servoPower = 0.001 * (1 - Arm_Pos / DesiredAngle4);
                    } else {
                        ArmAxonCR.setPower(0);
                        servoPower = 0;
                    }
                }
            }
            telemetry.addData("Arm_Pos", Arm_Pos);
            telemetry.addData("servoPower", servoPower);
            telemetry.update();
        }
    }
}


            /*
            double Yaw   = robotOrientation.getYaw(AngleUnit.DEGREES);
            double Pitch = robotOrientation.getPitch(AngleUnit.DEGREES);
            double Roll  = robotOrientation.getRoll(AngleUnit.DEGREES);

            // Read Angular Velocities
            myRobotAngularVelocity = imu.getRobotAngularVelocity(AngleUnit.DEGREES);

            float zRotationRate = myRobotAngularVelocity.zRotationRate;
            float xRotationRate = myRobotAngularVelocity.xRotationRate;
            float yRotationRate = myRobotAngularVelocity.yRotationRate;
*/




