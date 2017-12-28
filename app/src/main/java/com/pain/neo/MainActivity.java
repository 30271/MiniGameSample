package com.pain.neo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Random;

public class MainActivity extends Activity {

    private ImageButton one, two, three, four, five, six, seven, eight, nine;
    private TextView txtTime, txtScore;
    HashMap<Integer, ImageButton> nextMap;
    HashMap<ImageButton, Integer> battleMap;
    Random random = new Random();
    int next;
    int time = 10, score = 0;
    PhotoRun photoRun;
    AlertDialog.Builder ad;
    //Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initOnClick();
        initNextMap();
        initBattleMap();
        ad = new AlertDialog.Builder(this);
        photoRun = new PhotoRun();
        txtTime.post(runnable);
        photoRun.execute();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(time >= 0){
                txtTime.setText(String.valueOf(time));
                time--;
                txtTime.postDelayed(runnable, 1000);
            }

        }
    };

    class PhotoRun extends AsyncTask<Void, Integer, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txtScore.setText(String.valueOf(score));
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (time >= 0 && !isCancelled()) {
                next = random.nextInt(9) + 1;
                publishProgress(next);
                try {
                    Thread.sleep(next*100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
                return null;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            buttonBg();
            ImageButton imgBtn = nextMap.get(values[0]);
            imgBtn.setClickable(true);
            imgBtn.setBackgroundResource(R.drawable.out);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            txtTime.removeCallbacks(runnable);
            ad.setTitle("Game Over");
            ad.setMessage(String.valueOf(score));
            ad.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                        time = 10;
                        score = 0;
                        photoRun = new PhotoRun();
                        txtTime.post(runnable);
                        photoRun.execute();
                }
            });
            ad.setCancelable(false);
            if(!isFinishing())
                ad.show();
        }
    }

    class MyClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            Boolean isScore = false;

            int battle = battleMap.get(v);

            if (battle == next) {
                isScore = true;
            }

            if(isScore && time >= 0){
                score += 10;
                txtScore.setText(String.valueOf(score));
                nextMap.get(battle).setClickable(false);
                nextMap.get(battle).setBackgroundResource(R.drawable.in);
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (photoRun != null && photoRun.getStatus() == AsyncTask.Status.RUNNING)
        {
            photoRun.cancel(true);
            photoRun = null;
        }
    }

    private void initView() {
        one = (ImageButton) findViewById(R.id.imgBtn1);
        two = (ImageButton) findViewById(R.id.imgBtn2);
        three = (ImageButton) findViewById(R.id.imgBtn3);
        four = (ImageButton) findViewById(R.id.imgBtn4);
        five = (ImageButton) findViewById(R.id.imgBtn5);
        six = (ImageButton) findViewById(R.id.imgBtn6);
        seven = (ImageButton) findViewById(R.id.imgBtn7);
        eight = (ImageButton) findViewById(R.id.imgBtn8);
        nine = (ImageButton) findViewById(R.id.imgBtn9);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtScore = (TextView) findViewById(R.id.txtScore);
    }

    private void initOnClick() {
        MyClick click = new MyClick();
        one.setOnClickListener(click);
        two.setOnClickListener(click);
        three.setOnClickListener(click);
        four.setOnClickListener(click);
        five.setOnClickListener(click);
        six.setOnClickListener(click);
        seven.setOnClickListener(click);
        eight.setOnClickListener(click);
        nine.setOnClickListener(click);
    }

    private void buttonBg() {
        one.setBackgroundResource(R.drawable.in);
        two.setBackgroundResource(R.drawable.in);
        three.setBackgroundResource(R.drawable.in);
        four.setBackgroundResource(R.drawable.in);
        five.setBackgroundResource(R.drawable.in);
        six.setBackgroundResource(R.drawable.in);
        seven.setBackgroundResource(R.drawable.in);
        eight.setBackgroundResource(R.drawable.in);
        nine.setBackgroundResource(R.drawable.in);
    }

    private void initNextMap() {
        nextMap = new HashMap<Integer, ImageButton>();
        nextMap.put(1, one);
        nextMap.put(2, two);
        nextMap.put(3, three);
        nextMap.put(4, four);
        nextMap.put(5, five);
        nextMap.put(6, six);
        nextMap.put(7, seven);
        nextMap.put(8, eight);
        nextMap.put(9, nine);
    }

    private void initBattleMap() {
        battleMap = new HashMap<ImageButton, Integer>();
        battleMap.put(one, 1);
        battleMap.put(two, 2);
        battleMap.put(three, 3);
        battleMap.put(four, 4);
        battleMap.put(five, 5);
        battleMap.put(six, 6);
        battleMap.put(seven, 7);
        battleMap.put(eight, 8);
        battleMap.put(nine, 9);
    }
}
