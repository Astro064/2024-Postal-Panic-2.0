package org.firstinspires.ftc.teamcode;

import android.util.Size;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

public class AprilTag {


    static AprilTagProcessor myAprilTagProcessor;


    public static void InitAprilTag(WebcamName webcamName) {
        AprilTagProcessor.Builder myAprilTagProcessorBuilder;

        //Create a new AprilTag Proccessor Builder Project and assigning it to a variable
        myAprilTagProcessorBuilder = new AprilTagProcessor.Builder();

        //Optional: Specify a custom library of AprilTags
        //myAprilTagProcessorBuilder.setTagLibrary(myAprilTagLibrary);     //The OpMode must have already created a Library

        //Set the default tag library
        //"CurrentGameTagLibrary should show the current game library
        myAprilTagProcessorBuilder.setTagLibrary(AprilTagGameDatabase.getCurrentGameTagLibrary());


        //Optional: set other custom features of the AprilTag Processor (4 are shown here)
        myAprilTagProcessorBuilder.setDrawTagID(true);                  //Default: ture, for all detections
        myAprilTagProcessorBuilder.setDrawTagOutline(true);            //Default: true, when tag size was provided (thus eligible for pose estimation)
        myAprilTagProcessorBuilder.setDrawAxes(true);                  //Default: false
        myAprilTagProcessorBuilder.setDrawCubeProjection(true);        //Default: false

        // Create an AprilTagProcessor by calling build()

        myAprilTagProcessor = myAprilTagProcessorBuilder.build();


        //TensorFlow Initialisation
// TensorFlow Model needs to be uploaded to code (Everything is a pixel according to the code below.)


        TfodProcessor.Builder myTfodProcessorBuilder;
        TfodProcessor myTfodProcessor;
// Create a new TFOD Processor Builder object.
        myTfodProcessorBuilder = new TfodProcessor.Builder();

// Optional: set other custom features of the TFOD Processor (4 are shown here).
        myTfodProcessorBuilder.setMaxNumRecognitions(10);  // Max. number of recognitions the network will return
        myTfodProcessorBuilder.setUseObjectTracker(true);  // Whether to use the object tracker
        myTfodProcessorBuilder.setTrackerMaxOverlap((float) 0.2);  // Max. % of box overlapped by another box at recognition time
        myTfodProcessorBuilder.setTrackerMinSize(16);  // Min. size of object that the object tracker will track

// Create a TFOD Processor by calling build()
        myTfodProcessor = myTfodProcessorBuilder.build();

        //VisionPortal Initialization - Builder - - - - - - - - -

        VisionPortal.Builder myVisionPortalBuilder;
        VisionPortal myVisionPortal;

// Create a new VisionPortal Builder object.
        myVisionPortalBuilder = new VisionPortal.Builder();

// Specify the camera to be used for this VisionPortal.
        myVisionPortalBuilder.setCamera(webcamName);      // Other choices are: RC phone camera and "switchable camera name".


// Add the AprilTag Processor to the VisionPortal Builder.
        myVisionPortalBuilder.addProcessor(myAprilTagProcessor);       // An added Processor is enabled by default.
        myVisionPortalBuilder.addProcessor(myTfodProcessor);

// Optional: set other custom features of the VisionPortal (4 are shown here).
        myVisionPortalBuilder.setCameraResolution(new Size(640, 480));  // Each resolution, for each camera model, needs calibration values for good pose estimation.
        myVisionPortalBuilder.setStreamFormat(VisionPortal.StreamFormat.YUY2);  // MJPEG format uses less bandwidth than the default YUY2.
        myVisionPortalBuilder.enableLiveView(true);      // Enable LiveView (RC preview).
        myVisionPortalBuilder.setAutoStopLiveView(true);     // Automatically stop LiveView (RC preview) when all vision processors are disabled.

// Create a VisionPortal by calling build()
        myVisionPortal = myVisionPortalBuilder.build();

        // Enable or disable the AprilTag processor.
        myVisionPortal.setProcessorEnabled(myAprilTagProcessor, true);

// Enable or disable the TensorFlow Object Detection processor.
        myVisionPortal.setProcessorEnabled(myTfodProcessor, true);

// Enable or disable the AprilTag processor.
        // myVisionPortal.setProcessorEnabled(myAprilTagProcessor, true);

    }

    public static void telemetryAprilTag(Telemetry telemetry) {
        // <-- telemetry availabile

        List<AprilTagDetection> currentDetections = myAprilTagProcessor.getDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));

            } else {
                telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
            telemetry.update();
        }   // end for() loop

        // Add "key" information to telemetry
        /*
        telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
        telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
        telemetry.addLine("RBE = Range, Bearing & Elevation");
        telemetry.addData("Range", );
        telemetry.addData("Bearing", AprilTagDetection.ftcPose.bearing);
        telemetry.addData("Elevation", AprilTagDetection.ftcPose.elevation);
        telemetry.addData("Pitch", AprilTagDetection.ftcPose.pitch);
        telemetry.addData("Roll", AprilTagDetection.ftcPose.roll);
        telemetry.addData("Yaw", AprilTagDetection.ftcPose.yaw);
        */

    }

    public static int UpdateAprilTag()    {
        //AprilTag FOR Loop:


        List<AprilTagDetection> myAprilTagDetections;  // list of all detections
        int myAprilTagIdCode = -1;                           // ID code of current detection, in for() loop

// Get a list of AprilTag detections.
        myAprilTagDetections = myAprilTagProcessor.getDetections();

// Cycle through through the list and process each AprilTag.
        for (AprilTagDetection tempTag : myAprilTagDetections) {

            if (tempTag.metadata != null) {  // This check for non-null Metadata is not needed for reading only ID code.
                myAprilTagIdCode = tempTag.id;

                // Now take action based on this tag's ID code, or store info for later action.m

                //Retrive AprilTag Name:
//                    AprilTagDetection myAprilTagDetection;
                String myAprilTagName;
                myAprilTagName = tempTag.metadata.name;

                //Asign variables for AprilTagDetection:

                AprilTagDetection myAprilTagDetection;
                myAprilTagDetection = tempTag;
                double myTagPoseX = myAprilTagDetection.ftcPose.x;
                double myTagPoseY = myAprilTagDetection.ftcPose.y;
                double myTagPoseZ = myAprilTagDetection.ftcPose.z;
                double myTagPosePitch = myAprilTagDetection.ftcPose.pitch;
                double myTagPoseRoll = myAprilTagDetection.ftcPose.roll;
                double myTagPoseYaw = myAprilTagDetection.ftcPose.yaw;


                //Caluclated Extension of the Basic Pose
                // Range, direct (point-to-point) distance to the tag center

                //Bearing, the angle the camera must turn (left/right) to point directly at the tag center

                //Elevation, the angle the camera must tilt (up/down) to point directly at the tag center

                double myTagPoseRange = myAprilTagDetection.ftcPose.range;
                double myTagPoseBearing = myAprilTagDetection.ftcPose.bearing;
                double myTagPoseElevation = myAprilTagDetection.ftcPose.elevation;


                //April Tag Detection (To remain after variable definitions:

                myAprilTagIdCode = myAprilTagDetection.id;
            }


        }

        return myAprilTagIdCode;

    }

}


