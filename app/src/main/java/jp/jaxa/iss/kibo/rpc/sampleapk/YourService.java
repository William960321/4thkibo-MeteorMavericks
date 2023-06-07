package jp.jaxa.iss.kibo.rpc.sampleapk;

import android.util.Log;

import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

import java.util.List;
import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.android.gs.MessageType;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;

import org.opencv.core.Mat;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class YourService extends KiboRpcService {
    // initial value setting
    final String teamname = "MeteorMavericks";
    Point point1 = new Point(11.27f, -9.92f,5.3f);
    Point point2 = new Point(10.61f, -9.07f,4.48f);
    Point point3 = new Point(10.71f, -7.7f,4.48f);
    Point point4 = new Point(10.51f, -6.72f,5.18f);
    Point point5 = new Point(11.11f, -7.98f,5.34f);
    Point point6 = new Point(11.36f, -8.99f,4.78f);
    Point point7 = new Point(11.37f, -8.55f,4.48f);
    //    String mode =

    @Override
    protected void runPlan1(){
        // the mission starts
        api.startMission();
        Log.i(teamname, "start mission");
        // move to a point

        Quaternion quaternion = new Quaternion(0f, 0f, 0f, 1f);
        api.moveTo(point1, quaternion, false);
        Log.i(teamname, "move to 10.71 - 7.5 - 4.48");

        // report point1 arrival
//        api.reportPoiny1Arrival();

        // get a camera image
//        Mat image = api.getMatNavCam();

        // irradiate the laser
        api.laserControl(true);

        // take target1 snapshots
//        api.takeTarget1Snapshot();

        // turn the laser off
        api.laserControl(false);
      int loop_counter = 0;

        while (true){
            // get the list of active target id
            List<Integer> list = api.getActiveTargets();

            // move to a point
            Point point = new Point(10.4d, -10.1d, 4.47d);
            Quaternion quaternions = new Quaternion(0f, 0f, 0f, 1f);
            api.moveTo(point, quaternions, false);

            // get a camera image
            Mat image = api.getMatNavCam();

            // irradiate the laser
            laserControl(true);

            // take active target snapshots
            int target_id = 1;
            api.takeTargetSnapshot(target_id);

            /* ************************************************ */
            /* write your own code and repair the ammonia leak! */
            /* ************************************************ */

            // get remaining active time and mission time
            List<Long> timeRemaining = api.getTimeRemaining();

            // check the remaining milliseconds of mission time
            if (timeRemaining.get(1) < 60000){
                break;
            }

            loop_counter++;
            if (loop_counter == 2){
                break;
            }
        }
        // turn on the front flash light
        api.flashlightControlFront(0.05f);
        
        // get QR code content
//        String mQrContent = yourMethod();

        // turn off the front flash light
        api.flashlightControlFront(0.00f);

        // notify that astrobee is heading to the goal
        api.notifyGoingToGoal();

        /* ********************************************************** */
        /* write your own code to move Astrobee to the goal positiion */
        /* ********************************************************** */

        // send mission completion
//        api.reportMissionCompletion(mQrContent);
    }

    @Override
    protected void runPlan2(){
       // write your plan 2 here
    }

    @Override
    protected void runPlan3(){
        // write your plan 3 here
    }

    private void laserControl(boolean on) {
        api.laserControl(on);
        return;
    }
    // You can add your method
    private void moveToPoint(double pos_x, double pos_y, double pos_z,
                             double qua_x, double qua_y, double qua_z, double qua_w) {
        final Point point = new Point(pos_x, pos_y, pos_z);
        final Quaternion quaternion = new Quaternion((float)qua_x, (float)qua_y, (float)qua_z, (float)qua_w);
        api.moveTo(point, quaternion, true);
        return;
    }
}
