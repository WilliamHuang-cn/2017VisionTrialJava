package org.usfirst.frc.team1256.robot;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.AxisCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 * This is a demo program showing the use of OpenCV to do vision processing. The
 * image is acquired from the USB camera, then a rectangle is put on the image and
 * sent to the dashboard. OpenCV has many methods for different types of
 * processing.
 */
public class Robot extends IterativeRobot {
	Thread usbVisionThread;
	Thread axisVisionThread;

	@Override
	public void robotInit() {
		usbVisionThread = new Thread(() -> {
			
			// Get the UsbCamera from CameraServer
			UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
			// Set the resolution
			camera.setResolution(640, 480);

			// Get a CvSink. This will capture Mats from the camera
			CvSink cvSink = CameraServer.getInstance().getVideo();
			// Setup a CvSource. This will send images back to the Dashboard
			CvSource outputStream = CameraServer.getInstance().putVideo("USBFeed", 640, 480);

			// Mats are very memory expensive. Lets reuse this Mat.
			Mat mat = new Mat();

			// This cannot be 'true'. The program will never exit if it is. This
			// lets the robot stop this thread when restarting robot code or
			// deploying.
			while (!Thread.interrupted()) {
				// Tell the CvSink to grab a frame from the camera and put it
				// in the source mat.  If there is an error notify the output.
				if (cvSink.grabFrame(mat) == 0) {
					// Send the output the error.
					outputStream.notifyError(cvSink.getError());
					// skip the rest of the current iteration
					continue;
				}
				
				
				
				
				// Put a rectangle on the image
				Imgproc.rectangle(mat, new Point(100, 100), new Point(400, 400),
						new Scalar(0, 0, 255), 5);
				// Give the output stream a new image to display
				outputStream.putFrame(mat);
			}
		});
		
		axisVisionThread = new Thread(() -> {
			
			// Create pipeline instance
			Pipeline pipe = new Pipeline();
			
			// Get the UsbCamera from CameraServer
			AxisCamera camera = CameraServer.getInstance().addAxisCamera("Axis","10.13.91.3");
			// Set the resolution
			camera.setResolution(640, 480);

			// Get a CvSink. This will capture Mats from the camera
			CvSink cvSink = CameraServer.getInstance().getVideo();
			// Setup a CvSource. This will send images back to the Dashboard
			CvSource outputStream = CameraServer.getInstance().putVideo("AxisFeed", 640, 480);

			// Mats are very memory expensive. Lets reuse this Mat.
			Mat mat = new Mat();

			// This cannot be 'true'. The program will never exit if it is. This
			// lets the robot stop this thread when restarting robot code or
			// deploying.
			while (!Thread.interrupted()) {
				// Tell the CvSink to grab a frame from the camera and put it
				// in the source mat.  If there is an error notify the output.
				if (cvSink.grabFrame(mat) == 0) {
					// Send the output the error.
					outputStream.notifyError(cvSink.getError());
					// skip the rest of the current iteration
					continue;
				}
				
				// 
				pipe.setsource0(mat);
				pipe.process();
				
				String tempString = pipe.findContoursOutput().get(0).toString();
//				Imgproc.putText(mat, tempString, cv::Point(10,50), cv::FONT_HERSHEY_SIMPLEX, 1, cv::Scalar(255,255,0));
				Imgproc.putText(mat,tempString,new Point(10,50),Core.FONT_HERSHEY_SIMPLEX,1,new Scalar(255,255,0));
				
				// Put a rectangle on the image
//				Imgproc.rectangle(mat, new Point(100, 100), new Point(400, 400), new Scalar(0, 0, 255), 5);
				// Give the output stream a new image to display
				outputStream.putFrame(mat);
			}
		});
		
//		usbVisionThread.setDaemon(true);
//		usbVisionThread.start();
		axisVisionThread.setDaemon(true);
		axisVisionThread.start();
	}
}
