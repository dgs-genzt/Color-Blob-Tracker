package org.opencv.samples.colorblobdetect;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class ColorBlobDetectionActivity extends Activity implements OnTouchListener, CvCameraViewListener2, TextToSpeech.OnInitListener {
    private static final String  TAG              = "OCVSample::Activity";

    private boolean              mIsColorSelected = false;
    private Mat                  mRgba;
    private Mat                  mGray;
    private Scalar               mBlobColorRgba;
    private Scalar               mBlobColorHsv;
    private ColorBlobDetector    mDetector;
    private Mat                  mSpectrum;
    private Size                 SPECTRUM_SIZE;
    private Scalar               CONTOUR_COLOR;
    private TextToSpeech 		tts;

    private CameraBridgeViewBase mOpenCvCameraView;

    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                    mOpenCvCameraView.setOnTouchListener(ColorBlobDetectionActivity.this);
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public ColorBlobDetectionActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.color_blob_detection_surface_view);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.color_blob_detection_activity_surface_view);
        mOpenCvCameraView.setCvCameraViewListener(this);
        tts=new TextToSpeech(this, this);            
    }
        
    @Override
    public void onPause()
    {
    	tts.speak("Application has Paused", TextToSpeech.QUEUE_FLUSH, null);
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
    	tts.speak("Application has resumed, please touch to track", TextToSpeech.QUEUE_FLUSH, null);
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
    	        
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mDetector = new ColorBlobDetector();
        mSpectrum = new Mat();
        mBlobColorRgba = new Scalar(255);
        mBlobColorHsv = new Scalar(255);
        SPECTRUM_SIZE = new Size(200, 64);
        CONTOUR_COLOR = new Scalar(0,225,0,225);
        
    }

    public void onCameraViewStopped() {
        mRgba.release();
    }

    public boolean onTouch(View v, MotionEvent event) {
    	tts.speak("Application has started tracking", TextToSpeech.QUEUE_FLUSH, null);
        int cols = mRgba.cols();
        int rows = mRgba.rows();

        int xOffset = (mOpenCvCameraView.getWidth() - cols) / 2;
        int yOffset = (mOpenCvCameraView.getHeight() - rows) / 2;

        int x = (int)event.getX() - xOffset;
        int y = (int)event.getY() - yOffset;

        Log.i(TAG, "Touch image coordinates: (" + x + ", " + y + ")");

        if ((x < 0) || (y < 0) || (x > cols) || (y > rows)) return false;

        Rect touchedRect = new Rect();

        touchedRect.x = (x>4) ? x-4 : 0;
        touchedRect.y = (y>4) ? y-4 : 0;

        touchedRect.width = (x+4 < cols) ? x + 4 - touchedRect.x : cols - touchedRect.x;
        touchedRect.height = (y+4 < rows) ? y + 4 - touchedRect.y : rows - touchedRect.y;

        Mat touchedRegionRgba = mRgba.submat(touchedRect);

        Mat touchedRegionHsv = new Mat();
        Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

        // Calculate average color of touched region
        mBlobColorHsv = Core.sumElems(touchedRegionHsv);
        int pointCount = touchedRect.width*touchedRect.height;
        for (int i = 0; i < mBlobColorHsv.val.length; i++)
            mBlobColorHsv.val[i] /= pointCount;

        mBlobColorRgba = converScalarHsv2Rgba(mBlobColorHsv);
        
//        mBlobColorHsv.val[0] = 10;
//        mBlobColorHsv.val[1] = 10;
//        mBlobColorHsv.val[2] = 10.00;

        Log.i(TAG, "Touched rgba color: (" + mBlobColorRgba.val[0] + ", " + mBlobColorRgba.val[1] +
                ", " + mBlobColorRgba.val[2] + ", " + mBlobColorRgba.val[3] + ")");

        mDetector.setHsvColor(mBlobColorHsv);
        Imgproc.resize(mDetector.getSpectrum(), mSpectrum, SPECTRUM_SIZE);

        mIsColorSelected = true;

        touchedRegionRgba.release();
        touchedRegionHsv.release();

        return false; // don't need subsequent touch events
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        //doing blur
       // Imgproc.GaussianBlur(mRgba, mRgba, new org.opencv.core.Size(3, 3), 1, 1);
        //
        if (!mIsColorSelected) return mRgba;
            
            List<MatOfPoint> contours = mDetector.getContours();
            mDetector.process(mRgba);
            
            if (contours.size() <= 0) {
    			return mRgba;
    		}
            
            /*********************************************************************/
            RotatedRect rect = Imgproc.minAreaRect(new MatOfPoint2f(contours.get(0)	.toArray()));
    		
    		double boundWidth = rect.size.width;
    		double boundHeight = rect.size.height;
    		int boundPos = 0;

    		for (int i = 1; i < contours.size(); i++) {
    			rect = Imgproc.minAreaRect(new MatOfPoint2f(contours.get(i).toArray()));
    			if (rect.size.width * rect.size.height > boundWidth * boundHeight) {
    				boundWidth = rect.size.width;
    				boundHeight = rect.size.height;
    				boundPos = i;
    			}
    		}
    		
    		
    		Rect boundRect = Imgproc.boundingRect(new MatOfPoint(contours.get(boundPos).toArray()));
    		//Core.rectangle( mRgba, boundRect.tl(), boundRect.br(), CONTOUR_COLOR, 2, 8, 0 );
    		
    		Log.d(TAG, 
        			" Row start ["+ 
        			(int) boundRect.tl().y + "] row end ["+
        			(int) boundRect.br().y+"] Col start ["+
        			(int) boundRect.tl().x+"] Col end ["+
        			(int) boundRect.br().x+"]");
    		
    		int rectHeightThresh = 0;
    		double a = boundRect.br().y - boundRect.tl().y;
    		a = a * 0.7;
    		a = boundRect.tl().y + a;
    		
    		Log.d(TAG, 
        			" A ["+a+"] br y - tl y = ["+(boundRect.br().y - boundRect.tl().y)+"]");
    		
    		//Core.rectangle( mRgba, boundRect.tl(), boundRect.br(), CONTOUR_COLOR, 2, 8, 0 );
    		//Core.rectangle( mRgba, boundRect.tl(), new Point(boundRect.br().x, a), CONTOUR_COLOR, 2, 8, 0 );
    		
    		MatOfPoint2f pointMat = new MatOfPoint2f();
    		Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(boundPos).toArray()), pointMat, 3, true);
    		contours.set(boundPos, new MatOfPoint(pointMat.toArray()));
    		
    		MatOfInt hull = new MatOfInt();
    		MatOfInt4 convexDefect = new MatOfInt4();
    		Imgproc.convexHull(new MatOfPoint(contours.get(boundPos).toArray()), hull);
    		
    		if(hull.toArray().length < 3) return mRgba;
    		
    		Imgproc.convexityDefects(new MatOfPoint(contours.get(boundPos)	.toArray()), hull, convexDefect);

    		List<MatOfPoint> hullPoints = new LinkedList<MatOfPoint>();
    		List<Point> listPo = new LinkedList<Point>();
    		for (int j = 0; j < hull.toList().size(); j++) {
    			listPo.add(contours.get(boundPos).toList().get(hull.toList().get(j)));
    		}

    		MatOfPoint e = new MatOfPoint();
    		e.fromList(listPo);
    		hullPoints.add(e);

    		List<MatOfPoint> defectPoints = new LinkedList<MatOfPoint>();
    		List<Point> listPoDefect = new LinkedList<Point>();
    		for (int j = 0; j < convexDefect.toList().size(); j = j+4) {
    			Point farPoint = contours.get(boundPos).toList().get(convexDefect.toList().get(j+2));
    			Integer depth = convexDefect.toList().get(j+3);
    			/*if(depth > iThreshold && farPoint.y < a){
    				listPoDefect.add(contours.get(boundPos).toList().get(convexDefect.toList().get(j+2)));
    			}*/
    			Log.d(TAG, "defects ["+j+"] " + convexDefect.toList().get(j+3));
    		}

    		MatOfPoint e2 = new MatOfPoint();
    		e2.fromList(listPo);
    		defectPoints.add(e2);

    		Log.d(TAG, "hull: " + hull.toList());
    		Log.d(TAG, "defects: " + convexDefect.toList());

    		Imgproc.drawContours(mRgba, hullPoints, -1, CONTOUR_COLOR, 3);

            /*********************************************************************/
            
           /* Log.e(TAG, "Contours count: " + contours.size());
            Imgproc.drawContours(mRgba, contours, -1, CONTOUR_COLOR,3);*/

            Mat colorLabel = mRgba.submat(4, 68, 4, 68);
            colorLabel.setTo(mBlobColorRgba);

            Mat spectrumLabel = mRgba.submat(4, 4 + mSpectrum.rows(), 70, 70 + mSpectrum.cols());
            mSpectrum.copyTo(spectrumLabel);

        return mRgba;
    }

    private Scalar converScalarHsv2Rgba(Scalar hsvColor) {
        Mat pointMatRgba = new Mat();
        Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4);

        return new Scalar(pointMatRgba.get(0, 0));
    }

	@Override
	public void onInit(int arg0) {
		// TODO Auto-generated method stub
		tts.setLanguage(Locale.US);
		tts.speak("Application has Started", TextToSpeech.QUEUE_FLUSH, null);
	}
}
