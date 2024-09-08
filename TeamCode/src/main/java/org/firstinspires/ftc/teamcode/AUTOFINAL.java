package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.JavaUtil;

@Autonomous(name = "AUTOFINAL (Blocks to Java)")
public class AUTOFINAL extends LinearOpMode {

    private DcMotor frontRightMotor;
    private DcMotor backLeftMotor;
    private DcMotor backRightMotor;
    private DcMotor frontLeftMotor;
    private Servo ClawAxon;
    private CRServo ArmAxonCR;

    double x;
    int xEncoder;
    int rx;
    double y;

    /**
     * This sample contains the bare minimum Blocks for any regular OpMode. The 3 blue
     * Comment Blocks show where to place Initialization code (runs once, after touching the
     * DS INIT button, and before touching the DS Start arrow), Run code (runs once, after
     * touching Start), and Loop code (runs repeatedly while the OpMode is active, namely not
     * Stopped).
     */
    @Override
    public void runOpMode() {
        int Distance;
        int DeadBand;
        int yEncoder;

        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeftMotor");
        backRightMotor = hardwareMap.get(DcMotor.class, "backRightMotor");
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        ClawAxon = hardwareMap.get(Servo.class, "ClawAxon");
        ArmAxonCR = hardwareMap.get(CRServo.class, "ArmAxonCR");

        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        DeadBand = 1000;
        waitForStart();
        if (opModeIsActive()) {
            // Exit park:
            while (!(x < 0.2)) {
                xEncoder = 1 - backLeftMotor.getCurrentPosition();
                Distance = 100000;
                x = 1.5 * (1 - xEncoder / Distance);
                y = 0;
                rx = 0;
                Drive();
            }
            // Drive to cube 1:
            y = 0.3;
            while (!(y < 0.2)) {
                yEncoder = backRightMotor.getCurrentPosition();
                Distance = 35000;
                x = 0;
                y = 0.5 * (1 - yEncoder / Distance);
                rx = 0;
                Drive();
            }
            // Score cube 1:
            x = -0.3;
            while (!(x > -0.2)) {
                xEncoder = 1 - backLeftMotor.getCurrentPosition();
                Distance = 15000;
                x = 0.5 * (1 - xEncoder / Distance);
                y = 0;
                rx = 0;
                Drive();
            }
            // Return:
            x = 0.3;
            while (!(x < 0.2)) {
                xEncoder = 1 - backLeftMotor.getCurrentPosition();
                Distance = 110000;
                x = 0.7 * (1 - xEncoder / Distance);
                y = 0;
                rx = 0;
                Drive();
            }
            // Drive to cube 2:
            y = 0.3;
            while (!(y < 0.2)) {
                yEncoder = backRightMotor.getCurrentPosition();
                Distance = 36000 + 57000;
                x = 0;
                y = 0.5 * (1 - yEncoder / Distance);
                rx = 0;
                Drive();
            }
            // Score cube 2:
            x = -0.3;
            while (!(x > -0.2)) {
                xEncoder = 1 - backLeftMotor.getCurrentPosition();
                Distance = 15000;
                x = 0.6 * (1 - xEncoder / Distance);
                y = 0;
                rx = 0;
                Drive();
            }
            // Return:
            x = 0.3;
            while (!(x < 0.2)) {
                xEncoder = 1 - backLeftMotor.getCurrentPosition();
                Distance = 110000;
                x = 1 * (1 - xEncoder / Distance);
                y = 0;
                rx = 0;
                Drive();
            }
            // Drive to cube 3:
            y = 0.3;
            while (!(y < 0.2)) {
                yEncoder = backRightMotor.getCurrentPosition();
                Distance = 36000 + 95000;
                x = 0;
                y = 0.5 * (1 - yEncoder / Distance);
                rx = 0;
                Drive();
            }
            // Score cube 3:
            x = -0.3;
            while (!(x > -0.2)) {
                xEncoder = 1 - backLeftMotor.getCurrentPosition();
                Distance = 15000;
                x = 0.7 * (1 - xEncoder / Distance);
                y = 0;
                rx = 0;
                Drive();
            }
            // ------------------------------------------------------FINAL PARK: NOT YET DETERMINED-------------------------------------------------------------------------------
            // Return(clear alliance partner):
            x = 0.3;
            while (!(x < 0.2)) {
                xEncoder = 1 - backLeftMotor.getCurrentPosition();
                Distance = 100000;
                x = 0.5 * (1 - xEncoder / Distance);
                y = 0;
                rx = 0;
                Drive();
            }
            // Forwards to clear other cubes:
            y = 0.3;
            while (!(y < 0.2)) {
                yEncoder = backRightMotor.getCurrentPosition();
                Distance = 135000 + 120000;
                x = 0;
                y = 0.5 * (1 - yEncoder / Distance);
                rx = 0;
                Drive();
            }
            // Drive to delivery zone:
            x = 0.3;
            while (!(x < 0.2)) {
                xEncoder = 1 - backLeftMotor.getCurrentPosition();
                Distance = 200000;
                x = 1.5 * (1 - xEncoder / Distance);
                y = 0;
                rx = 0;
                Drive();
            }
        }
        requestOpModeStop();
    }

    /**
     * Describe this function...
     */
    private void Drive() {
        double denominator;

        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(x), Math.abs(y), Math.abs(rx))), 1));
        frontLeftMotor.setPower((y + x + rx) / denominator);
        backLeftMotor.setPower(((y - x) + rx) / denominator);
        frontRightMotor.setPower(((y - x) - rx) / denominator);
        backRightMotor.setPower(((y + x) - rx) / denominator);
        // ------------------------------
        telemetry.addData("x", x);
        telemetry.addData("y", y);
        telemetry.addData("xEncoder", xEncoder);
        telemetry.update();
    }
}