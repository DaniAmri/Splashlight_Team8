package id.sch.smktelkom_mlg.project.xirpl608172635.team_splashlight;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton imageButton;
    Camera camera;
    Camera.Parameters parameters;
    boolean isflash = true;
    boolean isOn = true;
    Button one, two, three;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imageButton = (ImageButton) findViewById(R.id.imageButton);
        isflash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        one = (Button) findViewById(R.id.button1);
        two = (Button) findViewById(R.id.button2);
        three = (Button) findViewById(R.id.button3);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        one.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                vibrator.vibrate(7000);

            }
        });

        two.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                long pattern[] = {60, 120, 180, 240, 300, 360, 420, 480};

                vibrator.vibrate(pattern, 1);

            }
        });

        three.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                vibrator.cancel();

            }
        });

        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            camera = Camera.open();
            parameters = camera.getParameters();
            isflash = true;

        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isflash) {
                    if (isOn) {
                        // turn off flash
                        setOff();
                    } else {
                        // turn on flash
                        setOn();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Error....");
                    builder.setMessage("Flashlight is not Available on this device...");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return;
                }
            }
        });
        getCamera();
    }

    private void getCamera()
    {
        if (camera == null) {
            try {
                camera = Camera.open();
                parameters = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
            }
        }
    }


    private void toggleButtonImage(){
        if(isOn){
            imageButton.setImageResource(R.drawable.one);
        }else{
            imageButton.setImageResource(R.drawable.offe);
        }
    }

    private void setOn() {
        if (!isOn) {
            if (camera == null || parameters == null) {
                return;
            }

            parameters = camera.getParameters();
            parameters.setFlashMode(parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.startPreview();
            isOn = true;

            // changing button/switch image
            toggleButtonImage();
        }
    }

    private void setOff() {
        if (isOn) {
            if (camera == null || parameters == null) {
                return;
            }

            parameters = camera.getParameters();
            parameters.setFlashMode(parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.stopPreview();
            isOn = false;

            toggleButtonImage();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        setOff();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isflash) setOn();
    }

    @Override
    protected void onStart() {
        super.onStart();

        getCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}
