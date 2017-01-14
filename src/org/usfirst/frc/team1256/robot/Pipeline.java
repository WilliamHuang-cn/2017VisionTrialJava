package org.usfirst.frc.team1256.robot;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.imgproc.*;

/**
* Pipeline class.
*
* <P>Autogenerated pipeline from GRIP.
*
* <P>Make sure to set all sources using the setters before running process().
*
* @author GRIP
*/
public class Pipeline {

	//Outputs
	private Mat rgbThresholdOutput = new Mat();
	private ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();

	//Sources
	private Mat source0;
	static{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	/**
	 * This constructor sets up the pipeline
	 */
	public Pipeline() {
	}

	/**
	 * This is the primary method that runs the entire pipeline and updates the outputs.
	 */
	public void process() {
		//Step  RGB_Threshold0:
		Mat rgbThresholdInput = source0;
		double[] rgbThresholdRed = {0.0, 77.49575551782682};
		double[] rgbThresholdGreen = {0.0, 153.25976230899832};
		double[] rgbThresholdBlue = {0.0, 47.190152801358224};
		rgbThreshold(rgbThresholdInput, rgbThresholdRed, rgbThresholdGreen, rgbThresholdBlue, rgbThresholdOutput);

		//Step  Find_Contours0:
		Mat findContoursInput = rgbThresholdOutput;
		boolean findContoursExternalOnly = false;
		findContours(findContoursInput, findContoursExternalOnly, findContoursOutput);

	}

	/**
	 * This method is a generated setter for source0.
	 * @param source the Mat to set
	 */
	public void setsource0(Mat source0) {
		this.source0 = source0;
	}

	/**
	 * This method is a generated getter for the output of a RGB_Threshold.
	 * @return Mat output from RGB_Threshold.
	 */
	public Mat rgbThresholdOutput() {
		return rgbThresholdOutput;
	}

	/**
	 * This method is a generated getter for the output of a Find_Contours.
	 * @return ArrayList<MatOfPoint> output from Find_Contours.
	 */
	public ArrayList<MatOfPoint> findContoursOutput() {
		return findContoursOutput;
	}


	/**
	 * Segment an image based on color ranges.
	 * @param input The image on which to perform the RGB threshold.
	 * @param red The min and max red.
	 * @param green The min and max green.
	 * @param blue The min and max blue.
	 * @param output The image in which to store the output.
	 */
	private void rgbThreshold(Mat input, double[] red, double[] green, double[] blue,
		Mat out) {
		Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2RGB);
		Core.inRange(out, new Scalar(red[0], green[0], blue[0]),
			new Scalar(red[1], green[1], blue[1]), out);
	}

	/**
	 * Sets the values of pixels in a binary image to their distance to the nearest black pixel.
	 * @param input The image on which to perform the Distance Transform.
	 * @param type The Transform.
	 * @param maskSize the size of the mask.
	 * @param output The image in which to store the output.
	 */
	private void findContours(Mat input, boolean externalOnly,
		List<MatOfPoint> contours) {
		Mat hierarchy = new Mat();
		contours.clear();
		int mode;
		if (externalOnly) {
			mode = Imgproc.RETR_EXTERNAL;
		}
		else {
			mode = Imgproc.RETR_LIST;
		}
		int method = Imgproc.CHAIN_APPROX_SIMPLE;
		Imgproc.findContours(input, contours, hierarchy, mode, method);
	}
}

