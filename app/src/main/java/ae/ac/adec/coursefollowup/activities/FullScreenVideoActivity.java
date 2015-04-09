package ae.ac.adec.coursefollowup.activities;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.gc.materialdesign.widgets.ProgressDialog;

import ae.ac.adec.coursefollowup.R;

/**
 * Created by MyLabtop on 4/5/2015.
 */
public class FullScreenVideoActivity extends Activity {

    private int dC = 0;
    private String typeView   = null;
    private String  videoPath = null;
    private int position = 0;

    private VideoView myVideoView;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.fragment_note_video_full_screen);

        if (mediaControls == null) {
            mediaControls = new MediaController(this);
        }

        myVideoView = (VideoView) findViewById(R.id.video_view);

        loadData();

        progressDialog = new ProgressDialog(this,"Loading....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        try {
            //set the media controller in the VideoView
            myVideoView.setMediaController(mediaControls);

            //set the uri of the video to be played
            myVideoView.setVideoURI(Uri.parse(videoPath));

        } catch (Exception e) {

            e.printStackTrace();
        }


        myVideoView.requestFocus();
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                progressDialog.dismiss();
                myVideoView.seekTo(position);
                    myVideoView.start();
            }
        });
        myVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                dC++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        dC = 0;
                    }
                };

                if (dC == 1) {
                    //Single click
                    handler.postDelayed(r, 250);
                } else if (dC == 2) {
                    //Double click
                    dC = 0;

                  finish();

                }

                return false;
            }
        });

    }
private void loadData(){

    typeView   = getIntent().getExtras().getString("TypeView");
    videoPath  = getIntent().getExtras().getString("videoPath");
    position   = getIntent().getExtras().getInt("position");


}
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //we use onSaveInstanceState in order to store the video playback position for orientation change
        savedInstanceState.putInt("Position", myVideoView.getCurrentPosition());
        myVideoView.pause();
    }


}