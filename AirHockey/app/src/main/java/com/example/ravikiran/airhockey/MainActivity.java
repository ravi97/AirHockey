package com.example.ravikiran.airhockey;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    DrawGame gameView;


    Bitmap ball,player1,player2;

    float ballx,bally;
    float p1x,p1y;
    float vx,vy;
    float p2x,p2y;
    int screenWidth,screenHeight;

    int score1,score2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView=new DrawGame(this);
        gameView.setOnTouchListener(this);
        setContentView(gameView);

        ball= BitmapFactory.decodeResource(getResources(), R.drawable.striker);
        ball=resizeBitmap(ball,50,50);
        player1=BitmapFactory.decodeResource(getResources(),R.drawable.bluecircle);
        player1=resizeBitmap(player1, 100, 100);
        player2=BitmapFactory.decodeResource(getResources(), R.drawable.greencircle);
        player2=resizeBitmap(player2, 100, 100);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;

        ballx=screenWidth/2;
        bally=screenHeight/2;
        p1x=screenWidth/2;
        p1y=3*screenHeight/4;
        p2x=screenWidth/2;
        p2y=screenHeight/4;
        vx=5;
        vy=5;

        score1=0;
        score2=0;



    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pauseGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resumeGame();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int pindex=event.getActionIndex();
        int pid;
        float pxtemp,pytemp;



        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN: {
                        pxtemp = event.getX();
                        pytemp = event.getY();
                        if(pytemp>=(2*v.getHeight()/3)){
                            p1x=pxtemp;
                            p1y=pytemp;
                        }
                        if(pytemp<=(v.getHeight()/3)){
                            p2x=pxtemp;
                            p2y=pytemp;
                        }
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        int pointerCount = event.getPointerCount();
                        for(int i = 0; i < pointerCount; ++i)
                        {
                            int pointerIndex = i;
                            pid = event.getPointerId(pointerIndex);
                            if(pid == 0)
                            {
                                pxtemp = event.getX(pointerIndex);
                                pytemp = event.getY(pointerIndex);
                                if(pytemp>=(2*v.getHeight()/3)){
                                    p1x=pxtemp;
                                    p1y=pytemp;
                                }
                                if(pytemp<=(v.getHeight()/3)){
                                    p2x=pxtemp;
                                    p2y=pytemp;
                                }

                            }
                            if(pid == 1)
                            {
                                pxtemp = event.getX(pointerIndex);
                                pytemp = event.getY(pointerIndex);
                                if(pytemp>=(2*v.getHeight()/3)){
                                    p1x=pxtemp;
                                    p1y=pytemp;
                                }
                                if(pytemp<=(v.getHeight()/3)){
                                    p2x=pxtemp;
                                    p2y=pytemp;
                                }
                            }
                        }
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        pxtemp = event.getX();
                        pytemp = event.getY();
                        if(pytemp>=(2*v.getHeight()/3)){
                            p1x=pxtemp;
                            p1y=pytemp;
                        }
                        if(pytemp<=(v.getHeight()/3)){
                            p2x=pxtemp;
                            p2y=pytemp;
                        }
                        break;
                    }
                    case MotionEvent.ACTION_POINTER_DOWN: {
                        pxtemp = event.getX(pindex);
                        pytemp = event.getY(pindex);
                        if(pytemp>=(2*v.getHeight()/3)){
                            p1x=pxtemp;
                            p1y=pytemp;
                        }
                        if(pytemp<=(v.getHeight()/3)){
                            p2x=pxtemp;
                            p2y=pytemp;
                        }
                        break;
                    }
                    case MotionEvent.ACTION_POINTER_UP: {
                        pxtemp = event.getX(pindex);
                        pytemp = event.getY(pindex);
                        if(pytemp>=(2*v.getHeight()/3)){
                            p1x=pxtemp;
                            p1y=pytemp;
                        }
                        if(pytemp<=(v.getHeight()/3)){
                            p2x=pxtemp;
                            p2y=pytemp;
                        }
                        break;
                    }
                    default:
                }



        return true;
    }

    public Bitmap resizeBitmap(Bitmap bitmapToScale,float newWidth,float newHeight){
        int width=bitmapToScale.getWidth();
        int height=bitmapToScale.getHeight();
        Matrix m = new Matrix();
        m.postScale(((float)newWidth)/width,((float)newHeight)/height);
        Bitmap bnew=Bitmap.createBitmap(bitmapToScale, ((int) ballx), ((int) bally),bitmapToScale.getWidth(),bitmapToScale.getHeight(),m,false);
        return bnew;
    }


    public class DrawGame extends SurfaceView implements Runnable {

        Thread gameThread=null;
        SurfaceHolder holder;
        boolean gameRunning=false;
        Canvas c;
        Paint p1,p2;

        public DrawGame(Context context) {
            super(context);
            holder=getHolder();
            p1=new Paint(Color.BLACK);
            p1.setStyle(Paint.Style.STROKE);
            p2=new Paint(Color.RED);



        }

        @Override
        public void run() {

            while(gameRunning)
            {
                if(holder.getSurface().isValid()){

                    try {
                        gameThread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    c=holder.lockCanvas();
                    c.drawARGB(255, 255, 255, 255);
                    c.drawBitmap(ball, ballx - (ball.getWidth() / 2), bally - (ball.getHeight() / 2), null);
                    c.drawBitmap(player1, p1x - (player1.getWidth() / 2), p1y - (player1.getHeight() / 2), null);
                    c.drawBitmap(player2, p2x - (player2.getWidth() / 2), p2y - (player2.getHeight() / 2), null);

                    p1.setStrokeWidth(5);
                    c.drawLine(0, c.getHeight() / 2, c.getWidth(), c.getHeight() / 2, p1);
                    c.drawCircle(c.getWidth() / 2, c.getHeight() / 2, 150, p1);

                    p2.setTextSize(50);
                    p2.setColor(Color.RED);
                    c.drawText(String.valueOf(score1) + " - " + String.valueOf(score2), c.getWidth() / 2 - 50, c.getHeight() / 2-5, p2);
                    p2.setStrokeWidth(10);
                    c.drawLine(0, 0, c.getWidth(), 0, p2);
                    c.drawLine(0,c.getHeight(),c.getWidth(),c.getHeight(),p2);

                    ballx+=vx;
                    bally+=vy;
                    setVelocity();
                    holder.unlockCanvasAndPost(c);
                }

            }

        }

        public void pauseGame(){

            gameRunning=false;
            while(true){
                try{
                    gameThread.join();

                }catch (InterruptedException e){
                    e.printStackTrace();

                }
                break;
            }
            gameThread=null;

        }

        public void resumeGame(){

            gameRunning=true;
            gameThread=new Thread(this);
            gameThread.start();

        }

        public void setVelocity(){

            int centerballx= (int) (ballx);
            int centerbally= (int) (bally);
            int centerp1x= (int) (p1x);
            int centerp1y= (int) (p1y);
            int centerp2x= (int) (p2x);
            int centerp2y= (int) (p2y);

            if(ballx<=ball.getWidth()/2){
                vx*=-1;
            }
            if(ballx>=(screenWidth-(ball.getWidth()/2))){
                vx*=-1;
            }
            if(bally<=ball.getHeight()/2){
                ballx=c.getWidth()/2;
                bally=c.getHeight()/2;
                score2++;
            }
            if(bally>=(c.getHeight()-(ball.getHeight()/2))){
                ballx=c.getWidth()/2;
                bally=c.getHeight()/2;
                score1++;
            }
            if(Math.sqrt(Math.pow((centerballx - centerp1x), 2) + Math.pow((centerbally - centerp1y), 2))<=((ball.getWidth()/2)+(player1.getWidth()/2))){
                vx*=-1;
                vy*=-1;
            }
            if(Math.sqrt(Math.pow((centerballx-centerp2x),2)+Math.pow((centerbally-centerp2y),2))<=((ball.getWidth()/2)+(player2.getWidth()/2))){
                vx*=-1;
                vy*=-1;
            }
        }





    }





}
