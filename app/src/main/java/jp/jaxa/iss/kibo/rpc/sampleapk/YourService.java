package jp.jaxa.iss.kibo.rpc.sampleapk;

import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.List;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

import static org.opencv.android.Utils.matToBitmap;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class YourService extends KiboRpcService {

    int NAV_MAX_COL = 1280;
    int NAV_MAX_ROW =  960;
    @Override
    protected void runPlan1(){
        // the mission starts
        api.startMission();
        int counter = 0;
        Point[] point = new Point[8];
        point[0] = new Point(11.2077,-10.1,5.4735);//change x and z;//ok
        point[1] = new Point(10.483384,-9.16,4.38);//ok
        point[2] = new Point(10.715,-7.765,4.48);// -7.59897//ok
        point[3] = new Point(10.46,-6.6145,5.2044);//change y and z;//ok
        point[4] = new Point(11.064,-7.919,5.3393);//-8//ok
        point[5] = new Point(11.355 ,-9.0449 ,4.9368);//4.8618//ok
        point[6] = new Point(11.369 ,-8.5518, 4.48);
        point[7] = new Point(10.6,-8.38,5.2);

        Quaternion[] quaternion = new Quaternion[8];
        quaternion[0] = new Quaternion(0f, 0f, -0.707f ,0.707f);
        quaternion[1] = new Quaternion(0.5f, 0.5f, -0.5f, 0.5f);
        quaternion[2] = new Quaternion(0f, 0.707f, 0f, 0.707f);
        quaternion[3] = new Quaternion(0, 0, -1 ,0);
        quaternion[4] = new Quaternion(-0.5f, -0.5f, -0.5f, 0.5f);
        quaternion[5] = new Quaternion(0, 0 ,0, 1);
        quaternion[6] = new Quaternion(0f, 0.707f, 0f, 0.707f);
        quaternion[7] = new Quaternion(0f, 0f, 0f, 1f);

        while (true){
            // get the list of active target id
            List<Integer> list = api.getActiveTargets();

            // move to a point
//            Point point = new Point(10.4d, -10.1d, 4.47d);
//            Quaternion quaternion = new Quaternion(0f, 0f, 0f, 1f);
//            api.moveTo(point, quaternion, false);
            MoveTo(point[0], quaternion[0]);
            Log.i("target", "1 ");
            MoveTo(point[1], quaternion[1]);
            Log.i("target", "2 ");
            MoveTo(point[2], quaternion[2]);
            Log.i("target", "3 ");
            MoveTo(point[3], quaternion[3] );
            Log.i("target", "4 ");
            MoveTo(point[4], quaternion[4] );
            Log.i("target", "5 ");
            MoveTo(point[5], quaternion[5] );
            Log.i("target", "6 ");
            MoveTo(point[6], quaternion[6]);
            Log.i("target", "7 ");

            // get a camera image
//            Mat image = api.getMatNavCam();
            // irradiate the laser


            // take active target snapshots
//            int target_id = 1;
//            api.takeTargetSnapshot(target);

            /* ************************************************ */
            /* write your own code and repair the ammonia leak! */
            /* ************************************************ */

            // get remaining active time and mission time
            List<Long> timeRemaining = api.getTimeRemaining();

            // check the remaining milliseconds of mission time
            if (timeRemaining.get(1) < 60000){
                break;
            }

//            loop_counter++;
//            if (loop_counter == 2){
//                break;
//            }
        }
        // turn on the front flash light
        api.flashlightControlFront(0.05f);

        // get QR code content
        String mQrContent = yourMethod();

        // turn off the front flash light
        api.flashlightControlFront(0.00f);

        // notify that astrobee is heading to the goal
        api.notifyGoingToGoal();

        /* ********************************************************** */
        /* write your own code to move Astrobee to the goal positiion */
        /* ********************************************************** */

        // send mission completion
        api.reportMissionCompletion(mQrContent);
    }

    @Override
    protected void runPlan2(){
        // write your plan 2 here
    }

    @Override
    protected void runPlan3(){
        // write your plan 3 here
    }

    // You can add your method
    private String yourMethod(){
        return "your method";
    }

    private void MoveTo(Point point, Quaternion quaternion){
        Result result;
        int count = 0, max_count = 4;

        do
        {
            result = api.moveTo(point, quaternion, true);
            count++;
        }
        while (!result.hasSucceeded() && count < max_count);
        api.laserControl(true);
    }

    public double[] QR_event(Point point, Quaternion quaternion, int count_max, int no)
    {
        String contents = null;
        int count = 0;
        double final_x = 0, final_y = 0, final_z = 0, final_w = 0;

        while (contents == null && count < count_max)
        {
            Log.d("QR[status]:", " start");
            long start_time = SystemClock.elapsedRealtime();
            //                                            //
            MoveTo(point,quaternion);
            Log.d("QR[NO]:"," "+no);

            flash_control(true);
            Mat src_mat = new Mat(undistord(api.getMatNavCam()), cropImage(40));
            Bitmap bMap = resizeImage(src_mat, 2000, 1500);



            //////////////////////////////////////////////////////////////////////////////////////////////////////
            int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];
            bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

            LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            try
            {
                com.google.zxing.Result result = new QRCodeReader().decode(bitmap);
                contents = result.getText();
                Log.d("QR[status]:", " Detected");

                String[] multi_contents = contents.split(", ");
                final_x = Double.parseDouble(multi_contents[1]);
                final_y = Double.parseDouble(multi_contents[3]);
                final_z = Double.parseDouble(multi_contents[5]);
                if(no == 1) final_w = Math.sqrt(1 - final_x*final_x - final_y*final_y - final_z*final_z);
            }
            catch (Exception e)
            {
                Log.d("QR[status]:", " Not detected");
            }
            //////////////////////////////////////////////////////////////////////////////////////////////////////
            Log.d("QR[status]:", " stop");
            long stop_time = SystemClock.elapsedRealtime();



            Log.d("QR[count]:", " " + count);
            Log.d("QR[total_time]:"," "+ (stop_time-start_time)/1000);
            count++;
        }


        flash_control(false);
        // api.judgeSendDiscoveredQR(no, contents);
        return new double[] {final_x, final_y, final_z, final_w};

    }

    public Mat undistord(Mat src)
    {
        Mat dst = new Mat(1280, 960, CvType.CV_8UC1);
        Mat cameraMatrix = new Mat(3, 3, CvType.CV_32FC1);
        Mat distCoeffs = new Mat(1, 5, CvType.CV_32FC1);

        int row = 0, col = 0;

        double cameraMatrix_sim[] =
                {
                        344.173397, 0.000000, 630.793795,
                        0.000000, 344.277922, 487.033834,
                        0.000000, 0.000000, 1.000000
                };
        double distCoeffs_sim[] = {-0.152963, 0.017530, -0.001107, -0.000210, 0.000000};

        double cameraMatrix_orbit[] =
                {
                        692.827528, 0.000000, 571.399891,
                        0.000000, 691.919547, 504.956891,
                        0.000000, 0.000000, 1.000000
                };
        double distCoeffs_orbit[] = {-0.312191, 0.073843, -0.000918, 0.001890, 0.000000};

        if(MODE == "sim")
        {
            cameraMatrix.put(row, col, cameraMatrix_sim);
            distCoeffs.put(row, col, distCoeffs_sim);
            Log.d("Mode[camera]:"," sim");
        }
        else if(MODE == "iss")
        {
            cameraMatrix.put(row, col, cameraMatrix_orbit);
            distCoeffs.put(row, col, distCoeffs_orbit);
            Log.d("Mode[camera]:"," iss");
        }

        cameraMatrix.put(row, col, cameraMatrix_orbit);
        distCoeffs.put(row, col, distCoeffs_orbit);

        Imgproc.undistort(src, dst, cameraMatrix, distCoeffs);
        return dst;
    }

    public Rect cropImage(int percent_crop)
    {
        double ratio = NAV_MAX_COL / NAV_MAX_ROW;

        double percent_row = percent_crop/2;
        double percent_col = percent_row * ratio;

        int offset_row = (int) percent_row * NAV_MAX_ROW / 100;
        int offset_col = (int) percent_col * NAV_MAX_COL / 100;
        double rows = NAV_MAX_ROW - (offset_row * 2);
        double cols = NAV_MAX_COL - (offset_col * 2);

        return new Rect(offset_col, offset_row, (int) cols, (int) rows);
    }

    public Bitmap resizeImage(Mat src, int width, int height)
    {
        Size size = new Size(width, height);
        Imgproc.resize(src, src, size);

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        matToBitmap(src, bitmap, false);
        return bitmap;
    }

    public void flash_control(boolean status)
    {
        if(status)
        {
            api.flashlightControlFront(0.025f);

            try
            {
                Thread.sleep(1000); // wait a few seconds
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        else api.flashlightControlFront(0);
    }
}






