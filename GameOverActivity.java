package fishbowl.appone.bounce;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bounce.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class GameOverActivity extends AppCompatActivity {

    private ImageButton StartGameAgain;
    private TextView scoreView;
    private TextView highScoreView;
    private int score;
    private AdView mAdView;
    private AdView dAdView;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        MobileAds.initialize(this, "ca-app-pub-3105441675332417~2239159104");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3105441675332417/7266105525");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                Intent mainIntent = new Intent(GameOverActivity.this, MainActivity.class);
                startActivity(mainIntent);
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        mAdView = findViewById(R.id.adView);
        dAdView = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        dAdView.loadAd(adRequest);
        mAdView.loadAd(adRequest);

        StartGameAgain = (ImageButton) findViewById(R.id.play_again_btn);
        scoreView = (TextView) findViewById(R.id.scoreView);
        highScoreView = (TextView) findViewById(R.id.highScoreView);

        score = getIntent().getIntExtra("SCORE",  0);
        scoreView.setText("Score: " + score);

        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highscore = settings.getInt("HIGH_SCORE", 0);

        if (score > highscore)
        {
            highScoreView.setText("High Score: " + score);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.commit();
        }
        else
        {
            highScoreView.setText("High Score: " + highscore);
        }

        StartGameAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer btn = MediaPlayer.create(GameOverActivity.this, R.raw.button);
                btn.start();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Intent mainIntent = new Intent(GameOverActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                }
            }
        });

        scoreView.setText("Score: " + score);
    }
}