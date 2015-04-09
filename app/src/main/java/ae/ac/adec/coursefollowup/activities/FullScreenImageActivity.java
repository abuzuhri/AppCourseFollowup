package ae.ac.adec.coursefollowup.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.gc.materialdesign.widgets.ProgressDialog;

import java.io.File;

import ae.ac.adec.coursefollowup.R;

/**
 * Created by MyLabtop on 4/5/2015.
 */
public class FullScreenImageActivity extends Activity {

    File imgFile =null;

    private int dC = 0;
    private String  imagePath  = null;
    ImageView txtNoteImageView = null;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.fragment_note_image_full_screen);



        txtNoteImageView = (ImageView) findViewById(R.id.txtNoteImageView);
        loadData();

        imgFile = new File(imagePath);
        if(imgFile.exists()) {
           final BitmapFactory.Options options = new BitmapFactory.Options();

           options.inSampleSize = 8;

           Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);

           txtNoteImageView.setAdjustViewBounds(false);
           txtNoteImageView.setScaleType(ImageView.ScaleType.FIT_XY);
           txtNoteImageView.setImageBitmap(myBitmap);

        }
        txtNoteImageView.setOnTouchListener(new View.OnTouchListener() {
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

                    handler.postDelayed(r, 250);
                } else if (dC == 2) {

                    dC = 0;

                    finish();
                }

                return false;
            }
        });

    }
    private void loadData(){

        imagePath  = getIntent().getExtras().getString("imagePath");
    }


}