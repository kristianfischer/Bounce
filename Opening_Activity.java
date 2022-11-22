package fishbowl.appone.bounce;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bounce.R;


public class Opening_Activity extends AppCompatActivity {


private TextView txtCRF;
private ImageView canuck;
private ImageView titlescreen;
private ViewGroup rootView;
public static final int VISIBLE = 0x00000000;
public static final int INVISIBLE = 0x00000004;
public static final int GONE = 0x00000008;
int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_);

        rootView = (ViewGroup) findViewById(R.id.rootView);
        txtCRF = (TextView) findViewById(R.id.txtCRF);
        canuck = (ImageView) findViewById(R.id.canuck);
        titlescreen = (ImageView) findViewById(R.id.titlescreen);

      // Transition transition = new Fade();
       // transition.setDuration(400);
       // transition.addTarget(R.id.txtCRF);

        //TransitionManager.beginDelayedTransition(rootView, transition);
        txtCRF.animate().alpha(1.0f).setDuration(1000);
        canuck.animate().alpha(1.0f).setDuration(1000);
        titlescreen.animate().alpha(0.0f).setDuration(1000);
        txtCRF.setVisibility(View.VISIBLE);
        canuck.setVisibility(View.VISIBLE);
        titlescreen.setVisibility(View.GONE);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Intent mainIntent = new Intent(Opening_Activity.this, MainActivity.class);
                    startActivity(mainIntent);
                }
            }
        };
        thread.start();

        Thread thread2 = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                    txtCRF.animate().alpha(0.0f).setDuration(1000);
                    canuck.animate().alpha(0.0f).setDuration(1000);
                    titlescreen.animate().alpha(1.0f).setDuration(1000);
                    txtCRF.setVisibility(View.GONE);
                    canuck.setVisibility(View.GONE);
                    titlescreen.setVisibility(View.VISIBLE);
                    MediaPlayer opening = MediaPlayer.create(Opening_Activity.this, R.raw.openingsound);
                    opening.start();
                        }
                    });
                }
                }


        };
        thread2.start();

    }

    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }
}
