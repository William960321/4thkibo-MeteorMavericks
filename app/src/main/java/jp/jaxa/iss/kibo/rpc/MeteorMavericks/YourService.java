package jp.jaxa.iss.kibo.rpc.MeteorMavericks;

import android.util.Log;

import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

import java.util.List;
import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class YourService extends KiboRpcService {
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
}






