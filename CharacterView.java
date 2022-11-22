package fishbowl.appone.bounce;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.example.bounce.R;


@RequiresApi(api = Build.VERSION_CODES.O)
public class CharacterView extends View {
    private static final int FADE_MILLISECONDS = 3000;
    private static final int FADE_STEP = 120;
    private static final int ALPHA_STEP = 255 / (FADE_MILLISECONDS / FADE_STEP);
    private Paint alphaPaint = new Paint();
    private int currentAlpha = 255;
    private Bitmap fish[] = new Bitmap[2];
    private Bitmap ledge[] = new Bitmap[2];
    private Bitmap ins;
    private Bitmap scaledfish[] = new Bitmap[2];
    private Bitmap scaledledge[] = new Bitmap[2];
    private Bitmap scaledins;
    private double mWidthPixels;
    private double mHeightPixels;
    private double ogscreenwidth;
    private double ogscreenheight;
    private double screenwidth;
    private double screenheight;
    private int glitchpro = 0;
    private int fishX;
    private int fishY;
    private int xscale;
    private int yscale;
    private int spritecount = 1;
    private int pointhold = 0;
    private int directioncount = 1;
    private int directionfactor = 1;
    private int touchcount = 0;
    private int wall1Y;
    private int wall2Y;
    private int wall3Y = -1;
    private int wall4Y;
    private Typeface customtypeface = Typeface.createFromAsset(getContext().getAssets(), "chalk.ttf");
    private int wallLengthY;
    private int scorecount;
    private int wallLengthX;
    private int wall1X = -1;
    private int wall2X;
    private int wall3X;
    private int wall4X;
    private int wallSpeed1Y = 0;
    private int wallSpeed2Y = 0;
    private int wallSpeed3X = 0;
    MediaPlayer point = MediaPlayer.create(getContext(), R.raw.chalk);
    private int wallSpeed4X = 0;
    private int fishSpeed;
    private int fishXSpeed;
    private int walldirection1Y = 1;
    private int walldirection2Y = 1;
    private int wallcounter1 = 0;
    private int wallcounter2 = 0;
    private int walldirection3X = 1;
    private int walldirection4X = 1;
    public int canvasWidth, canvasHeight;
    private Bitmap backgroundImage;
    private Paint scorePaint = new Paint();
    private Paint wallPaint = new Paint();
    private Paint wallPaintX = new Paint();
    private Paint insPaint = new Paint();
    private Paint titlePaint = new Paint();

    private boolean touch = false;
    private int ledgeX, ledgeY;
    private int score, lifecounter;

    public CharacterView(Context context) {
        super(context);


        fish[0] = BitmapFactory.decodeResource(getResources(), R.drawable.charactertwo);
        fish[1] = BitmapFactory.decodeResource(getResources(), R.drawable.charactertwo);
        ledge[0] = BitmapFactory.decodeResource(getResources(), R.drawable.ledgeclosed);
        ledge[1] = BitmapFactory.decodeResource(getResources(), R.drawable.ledgeopened);
        backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.background3);
        ins = BitmapFactory.decodeResource(getResources(), R.drawable.rules);




        insPaint.setColor(Color.LTGRAY);
        insPaint.setAntiAlias(true);

        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(120);
        scorePaint.setTypeface(customtypeface);
        scorePaint.setAntiAlias(true);


        wallPaint.setColor(Color.WHITE);
        wallPaint.setAntiAlias(true);
        wallPaintX.setColor(Color.BLACK);
        wallPaintX.setAntiAlias(true);

        wall3X = -1;
        wall4X = -1;
        score = 0;
        lifecounter = 1;
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int mHeightPixels = dm.heightPixels;
        int mWidthPixels = dm.widthPixels;
        double screenwidth = mWidthPixels/dm.xdpi;
        double screenheight = mHeightPixels/dm.ydpi;
        double ogscreenwidth = 2.4409508;
        double ogscreenheight = 4.17324161;

        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();
        double scoresize = (canvasHeight *.120);
        scorePaint.setTextSize((float) scoresize);
        wall2X = canvasWidth - (canvasWidth/36);
        wall4Y = canvasHeight-10;

        xscale = (int) (screenwidth/ogscreenwidth);
        yscale = (int) (screenheight/ogscreenheight);

        scaledfish[0] = Bitmap.createScaledBitmap(fish[0], (xscale * fish[0].getWidth()), (yscale * fish[0].getHeight()), false);
        scaledfish[1] = Bitmap.createScaledBitmap(fish[1], (xscale*fish[0].getWidth()), (yscale*fish[0].getHeight()), false);
        scaledins = Bitmap.createScaledBitmap(ins,   xscale*ins.getWidth(), yscale*ins.getHeight(), false);
        scaledledge[0] = Bitmap.createScaledBitmap(ledge[0], xscale*ledge[0].getWidth(), yscale*ledge[0].getHeight(), false);
        scaledledge[1] = Bitmap.createScaledBitmap(ledge[1], xscale*ledge[1].getWidth(), yscale*ledge[1].getHeight(), false);


        canvas.drawBitmap(backgroundImage, 0, 0, null);

        int min = scaledfish[0].getHeight() * 2;
        int max = canvasHeight - scaledfish[0].getHeight() * 2;
        int maxwallY = canvasHeight - wallLengthY;
        int minwallY = 100;
        int maxwallX = canvasWidth-wallLengthX;
        int minwallX = 0;
        int minfishY = 0;
        int maxfishY = canvasHeight - scaledfish[0].getHeight();
        int minfishX = 50;
        double maxfishX = canvasWidth - scaledfish[0].getWidth() * 2.5;

        if(touchcount == 1) {
            if (currentAlpha > 0) {
                alphaPaint.setAlpha(currentAlpha);
                canvas.drawBitmap(scaledins, ((canvasWidth/2)-(ins.getWidth()/2)), (canvasHeight/5), alphaPaint);
                currentAlpha -= ALPHA_STEP;
                postInvalidateDelayed(FADE_STEP, 50, 250, 50 + ins.getWidth(), 250 + ins.getHeight());
            }
        }

        if (touchcount <= 1)
        {
            fishX = (canvasWidth/3);
            fishY =  ((canvasHeight/2)- (scaledfish[0].getHeight()/2));
            wallLengthY = (int) (canvasHeight/1.54);
            wallLengthX = canvasWidth;
            wall1Y = (int) (canvasHeight/12.32);
            wall2Y = (int) (canvasHeight/12.32);
            wallSpeed2Y = canvasHeight/205;
            wallSpeed1Y = canvasHeight/205;
            ledgeX = (int) (canvasWidth/1.6);
            ledgeY = (int) (canvasHeight/1.76);
        }
        else
        {
            fishY = fishY + fishSpeed;
            fishX = fishX + fishXSpeed;

        }

        if (touchcount == 0){
            canvas.drawBitmap(scaledins, ((canvasWidth/2)-(scaledins.getWidth()/2)), (canvasHeight/5), alphaPaint);
            canvas.drawText("" + score, (float) (canvasWidth/2.25), (canvasHeight/6), scorePaint);
            canvas.drawRect(wall1X + (canvasWidth/72), wall1Y, wall1X + (canvasWidth/34), wall1Y + wallLengthY, wallPaint);
            canvas.drawRect(wall2X, wall2Y, (float) (wall2X + (canvasWidth/65.5)), wall2Y + wallLengthY, wallPaint);
        }
        else {
            if (score >= 50) {
                wall1Y = wall1Y + wallSpeed1Y;
                wall2Y = wall2Y + wallSpeed2Y;

                if ((wall1Y > maxwallY) || (wall1Y < 0)) {
                    wallSpeed1Y = wallSpeed1Y*-1;
                }
                if ((wall2Y > maxwallY) || (wall2Y < 0)) {
                    wallSpeed2Y = wallSpeed2Y*-1;
                }

                if (wallcounter2 < 1 && wallcounter2 < 1)
                {
                    if ((score % 10) == 0 && (wallSpeed1Y < 0))
                    {
                        wallSpeed1Y = wallSpeed1Y- (canvasHeight/410);
                        scorecount = score;
                        wallcounter1++;
                    }else if ((score % 10) == 0 && (wallSpeed1Y > 0))
                    {
                        wallSpeed1Y = wallSpeed1Y + (canvasHeight/410);
                        scorecount = score;
                        wallcounter1++;
                    }

                    if ((score % 10) == 0 && (wallSpeed2Y < 0))
                    {
                        wallSpeed2Y = wallSpeed2Y- (canvasHeight/410);
                        scorecount = score;
                        wallcounter2++;
                    } else if ((score % 10) == 0 && wallSpeed2Y > 0)
                    {
                        wallSpeed2Y = wallSpeed2Y+ (canvasHeight/410);
                        scorecount = score;
                        wallcounter2++;
                    }}
                if (score > scorecount)
                {
                    wallcounter2 = 0;
                    wallcounter1 = 0;
                }
            }


            if ((fishY + fish[0].getHeight()) < minfishY) {
                lifecounter--;
            }
            if (fishY >= canvasHeight) {
                fishSpeed = 0;
                lifecounter--;
            }
            fishSpeed = fishSpeed + (canvasHeight/308);


            if (fishX >= (canvasWidth) || (fishX + fish[0].getWidth()) <= 0) {
                lifecounter--;
            }


            if (hitLedgeChecker(ledgeX, ledgeY)) {
                if (score < 50) {
                    wallLengthY -= (canvasHeight / 123.2);
                }
                MediaPlayer jump = MediaPlayer.create(getContext(), R.raw.pencilsnap);
                jump.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer jump) {
                        jump.release();
                        jump = null;
                    }
                });
                jump.start();
                score++;
                canvas.drawBitmap(scaledledge[1], ledgeX, ledgeY, null);
                ledgeY = (int) Math.floor(Math.random() * (max - min)) + min;
                ledgeX = (int) Math.floor(Math.random() * (maxfishX - minfishX)) + minfishX;
            } else {
                canvas.drawBitmap(scaledledge[0], ledgeX, ledgeY, null);
            }

            if (hitWall1Checker(wall1X, wall1Y)) {
                directioncount = directioncount + 1;
                glitchpro+= 3;

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(100);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (wallLengthY <= 300) {
                                wallLengthY = 300;
                            }
                            if (score < 50)
                                wall1Y = (int) Math.floor(Math.random() * (maxwallY - minwallY)) + minwallY;
                        }
                    }
                };
                thread.start();
            }

            if (hitWall2Checker(wall2X, wall2Y)) {
                directioncount = directioncount + 1;
                glitchpro+= 3;

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(100);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {

                            if (wallLengthY <= 300) {
                                wallLengthY = 300;
                            }
                            if (score < 50)
                                wall2Y = (int) Math.floor(Math.random() * (maxwallY - minwallY)) + minwallY;
                        }
                    }
                };
                thread.start();
            }

            if ((directioncount % 2) == 0) {
                canvas.drawBitmap(scaledfish[1], fishX, fishY, null);
                fishXSpeed = -(canvasWidth/48);
            } else if ((directioncount % 2) != 0) {
                canvas.drawBitmap(scaledfish[0], fishX, fishY, null);
                fishXSpeed = (canvasWidth/48);
            }

            canvas.drawRect(wall1X + (canvasWidth/72), wall1Y, wall1X + (canvasWidth/34), wall1Y + wallLengthY, wallPaint);
            canvas.drawRect(wall2X, wall2Y, (float) (wall2X + (canvasWidth/65.5)), wall2Y + wallLengthY, wallPaint);
            canvas.drawRect(wall3X, wall3Y, wall3X + wallLengthX, wall3Y + 11, wallPaintX);
            canvas.drawRect(wall4X, wall4Y, wall4X + wallLengthX, wall4Y + 11, wallPaintX);
            if (score >= 10)
                canvas.drawText("" + score, (float) (canvasWidth/2.5), (canvasHeight/6), scorePaint);
            else if (score < 10)
                canvas.drawText("" + score, (float) (canvasWidth/2.25), (canvasHeight/6), scorePaint);

            if (lifecounter == 0) {
                Intent gameOverIntent = new Intent(getContext(), GameOverActivity.class);
                gameOverIntent.putExtra("SCORE", score);
                getContext().startActivity(gameOverIntent);
                MediaPlayer lose = MediaPlayer.create(getContext(), R.raw.lose);
                lose.start();
            }
        }
    }


    public boolean hitLedgeChecker (int x, int y)
    {
        if ((fishX > ledgeX - (canvasWidth/30) && (fishX + (fish[0].getWidth()) < (ledgeX + ledge[0].getWidth() + (canvasWidth/30)) && (fishY + fish[0].getHeight()) >= ledgeY && (fishY + fish[0].getHeight()) <= (ledgeY + ledge[0].getHeight()) && fishSpeed > 20)))
        {
            return true;
        }
        return false;
    }

    public boolean hitWall1Checker (int x, int y)
    {
        if ((fishX < (wall1X + (canvasWidth/34)) && fishX > wall1X) && fishY <= (wall1Y + wallLengthY) && fishY >= (wall1Y - (canvasHeight/12.32)) && (glitchpro%2 != 0))
        {
            return true;
        }
        return false;
    }

    public boolean hitWall2Checker (int x, int y)
    {
        if (((fishX + fish[0].getWidth()) > wall2X && (fishX + fish[0].getWidth()) < wall2X + (canvasWidth/34)) && fishY <= (wall2Y + wallLengthY) && fishY >= (wall2Y - (canvasHeight/12))&& (glitchpro%2 == 0))
        {
            return true;
        }
        return false;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (fishY > canvasHeight)
                touch = false;
            else {
                touch = true;
                fishSpeed = -(canvasHeight/41);
            }
            MediaPlayer point = MediaPlayer.create(getContext(), R.raw.chalk);
            point.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer point) {
                    point.release();
                    point = null;
                }
            });
            if (touchcount > 0)
                point.start();

            touchcount++;

        }
        return true;
    }
}





