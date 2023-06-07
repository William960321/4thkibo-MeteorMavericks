package jp.jaxa.iss.kibo.rpc.sampleapk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myErrorLog();
//        area(5,9);
        Rectangle rect = new Rectangle();
        rect.width = 100;
        rect.height = 300;
        int result = rect.area();
        Log.i("Team", String.valueOf(rect.width));
    }
    void myErrorLog(){
        Log.i("MeteorMavericks", "error");
    }

    int area(int width, int height){
        return width*height;
    }
}

