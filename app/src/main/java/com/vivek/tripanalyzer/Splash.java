package com.vivek.tripanalyzer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);




//            covid =findViewById(R.id.covid);
//            YoYo.with(Techniques.BounceIn)
//                    .duration(1000)
//                    .repeat(2)
//                    .playOn(covid);
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    /* Create an Intent that will start the Menu-Activity. */

                    Intent mainIntent = new Intent(Splash.this,StartActivity.class);
                    Splash.this.startActivity(mainIntent);
                    Splash.this.finish();


                }
            }, 4000);
    }
}
