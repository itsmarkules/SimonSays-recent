package com.com220.sli.simonsays;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity
{
    Button topLeft;
    Button topRight;
    Button bottomLeft;
    Button bottomRight;
    Button start;

    TextView score;
    TextView textView;
    TextView hiScore;

    ArrayList<Integer> randomSequence = new ArrayList<Integer>(); //STORES THE RANDOMLY GENERATED NUMBERS; DETERMINES THE ORDER OF THE ANIMATION OF THE BUTTONS
    ArrayList<Integer> userSequence = new ArrayList<Integer>(); //STORES THE USER'S INPUTS FOR COMPARISON TO THE ARRAYLIST 'SEQUENCE'

    StringBuilder randomSequenceString = new StringBuilder();   //STORES EACH RANDOMLY GENERATED NUMBER IN THIS STRINGBUILDER 'RANDOMSEQUENCESTRING'
    StringBuilder userSequenceString = new StringBuilder(); //STORES EACH USER INPUT IN THIS STRINGBUILDER 'USERSEQUENCESTRING'

    int numUserInputs = 0;  //COUNTS EACH TIME THE USER SUCCESSFULLY INPUTS A NUMBER
    int numOfItems = 4; //LENGTH OF THE MEMORY SEQUENCE. WILL GROW BY 1 WITH EACH SUCCESSFUL 'WIN' BY THE USER

    Boolean gameStart = false;  //CONTROLS WHETHER THE USER CAN START INPUTTING (STARTS THE GAME)
    Boolean keepGoing = true;
    Boolean sound = true;

    int scoreLevel = 0;
    int puzzlePiece = 0;
    int tempUS = 0;
    int tempRS = 0;
    int currentScore = 0;
    int finalScore = 0;

    public static void saveData(String key, String value, Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static String getData(String key, Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        topLeft = (Button) findViewById(R.id.topleft_button);
        topRight = (Button) findViewById(R.id.topright_button);
        bottomRight = (Button) findViewById(R.id.bottomright_button);
        bottomLeft = (Button) findViewById(R.id.bottomleft_button);
        start = (Button) findViewById(R.id.start);

        score = (TextView) findViewById(R.id.score);
        textView = (TextView)findViewById(R.id.textView);
        hiScore = (TextView)findViewById(R.id.newHighScore);

        final Animation blink = new AlphaAnimation(1,0);    //ANIMATION
        blink.setDuration(800); //ANIMATION DURATION FOR EACH BUTTON
        blink.setInterpolator(new LinearInterpolator());    //???
        //blink.setRepeatCount(Animation.ABSOLUTE); //or INFINITE
        //blink.setRepeatMode(Animation.REVERSE);

        final MediaPlayer soundOne = MediaPlayer.create(this, R.raw.smb_coin);
        final MediaPlayer soundTwo = MediaPlayer.create(this, R.raw.smb_fireball);
        final MediaPlayer soundThree = MediaPlayer.create(this, R.raw.smb_stomp);
        final MediaPlayer soundFour = MediaPlayer.create(this, R.raw.smb_kick);

        start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                randomSequence.clear();   //RESETS THE ARRAYLIST SEQUENCE'S CONTENTS
                randomSequenceString.setLength(0); //RESETS THE STRINGBUILDER'S STRINGADD CONTENTS
                score.setText(0 + "");
                userSequence.clear();
                userSequenceString.setLength(0);
                textView.setText("Score: ");
                start.setText("Start");
                keepGoing = true;

                long runtimeSequence = (numOfItems * 1000) + 500; //DYNAMIC RUNTIME LENGTH BASED ON THE NUMBER OF ITEMS
                new CountDownTimer(runtimeSequence, 1000)
                {
                    public void onTick(long mill)   //
                    {
                        Random rdm = new Random();  //RANDOM
                        int randomNumber = rdm.nextInt(4);  //GENERATES A RANDOM NUMBER BETWEEN 0 AND 3
                        String aS = randomNumber + "";  //CONVERTS THE NUMBER TO A STRING SO THAT IT CAN BE STORED IN THE STRINGBUILDER RANDOMSEQUENCE STRING (FOR LATER OUTPUT)
                        randomSequenceString.append(aS);
                        randomSequence.add(randomNumber);

                        if(randomNumber == 0)
                        {
                            topLeft.startAnimation(blink);
                            soundOne.start();
                        }
                        else if(randomNumber == 1)
                        {
                            topRight.startAnimation(blink);
                            soundTwo.start();
                        }
                        else if(randomNumber == 2)
                        {
                            bottomLeft.startAnimation(blink);
                            soundThree.start();
                        }
                        else if(randomNumber == 3)
                        {
                            bottomRight.startAnimation(blink);
                            soundFour.start();
                        }
                        else    //FOR REDUNDANCY'S SAKE
                        {
                            start.setText("ERROR: " + randomNumber); //This message should NEVER appear but if it does, something is definitely wrong
                        }
                    }
                    @Override
                    public void onFinish()  //WHEN THE SEQUENCE IS DONE ANIMATING
                    {
                        score.setText(randomSequenceString.toString()); //OUTPUTS THE SEQUENCE OF NUMBERS GENERATED TO THE TEXTVIEW SCORE
                        gameStart = true;   //ENABLES THE USER TO START INPUTTING VALUES
                    }
                }.start();
            }
        });
        topLeft.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                topLeft.startAnimation(blink);  //ANIMATES THE BUTTON WHEN PRESSED
                topRight.clearAnimation();
                bottomLeft.clearAnimation();
                bottomRight.clearAnimation();
                int buttonNumber = 0;
                try
                {
                    soundOne.start();
                    compareSequences(buttonNumber);
                    hiScore.setText(String.valueOf(currentScore));
                }
                catch(IndexOutOfBoundsException e)
                {
                    start.setText("topLeft Error");
                }
            }
        });
        topRight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                topRight.startAnimation(blink);//ANIMATES THE BUTTON WHEN PRESSED
                topLeft.clearAnimation();
                bottomLeft.clearAnimation();
                bottomRight.clearAnimation();
                int buttonNumber = 1;
                try {
                    soundTwo.start();
                    compareSequences(buttonNumber);
                    hiScore.setText(String.valueOf(currentScore));
                }
                catch(IndexOutOfBoundsException e)
                {
                    start.setText("topRight Error");
                }
            }
        });
        bottomLeft.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                bottomLeft.startAnimation(blink);//ANIMATES THE BUTTON WHEN PRESSED
                topLeft.clearAnimation();
                topRight.clearAnimation();
                bottomRight.clearAnimation();
                int buttonNumber = 2;
                try {
                    soundThree.start();
                    compareSequences(buttonNumber);
                    hiScore.setText(String.valueOf(currentScore));
                }
                catch(IndexOutOfBoundsException e)
                {
                    start.setText("bottomLeft Error");
                }
            }
        });
        bottomRight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                bottomRight.startAnimation(blink);//ANIMATES THE BUTTON WHEN PRESSED
                topLeft.clearAnimation();
                topRight.clearAnimation();
                bottomLeft.clearAnimation();
                int buttonNumber = 3;
                try
                {
                    soundFour.start();
                    compareSequences(buttonNumber);
                    hiScore.setText(String.valueOf(currentScore));
                }
                catch(IndexOutOfBoundsException e)
                {
                    start.setText("bottomRight Error");
                }
            }
        });
        Button back = (Button)findViewById(R.id.mainClose);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    public void compareSequences(int buttonNumber)
    {
        final MediaPlayer soundFive = MediaPlayer.create(this, R.raw.smb_powerup);
        final MediaPlayer soundSix = MediaPlayer.create(this, R.raw.smb_mariodie);
        if(gameStart == true)
        {
            userSequence.add(buttonNumber);
            if(randomSequence.get(numUserInputs).intValue() != buttonNumber)
            {
                puzzlePiece = 0;
                randomSequence.clear();
                randomSequenceString.setLength(0);
                userSequence.clear();
                userSequenceString.setLength(0);
                numOfItems = 4;
                numUserInputs = 0;
                currentScore = 0;
                gameStart = false;
                scoreLevel = 0;
                start.setText("Start New Game");
                score.setText(0 + "");
                textView.setText("Score: ");
                soundSix.start();
                keepGoing = false;
            }
            if(keepGoing == true)
            {
                currentScore++;
                userSequenceString.append(buttonNumber + "");
                numUserInputs++;
                textView.setText(userSequenceString.toString());
            }
        }
        else    //User can click the buttons but no input occurs
        {
            //start.setText("Play the game!");
        }
        if(numUserInputs >= numOfItems)
        {
            for(int i = 0; i < numOfItems; i++)
            {
                tempUS = userSequence.get(i).intValue();
                tempRS = randomSequence.get(i).intValue();
                try
                {
                    if(tempUS == tempRS)
                    {
                        puzzlePiece++;
                        if(puzzlePiece == numOfItems)
                        {
                            puzzlePiece = 0;
                            scoreLevel++;
                            saveData("saveFile", String.valueOf(currentScore), getApplicationContext());
                            hiScore.setText(String.valueOf(currentScore));
                            randomSequence.clear();
                            randomSequenceString.setLength(0);
                            userSequence.clear();
                            userSequenceString.setLength(0);
                            numUserInputs = 0;
                            numOfItems++;
                            gameStart = false;
                            start.setText("Start Level: " + scoreLevel);
                            score.setText(0 + "");
                            textView.setText("Score: ");
                            soundFive.start();
                            break;
                        }
                    }
                    else
                    {
                        puzzlePiece = 0;
                        randomSequence.clear();
                        randomSequenceString.setLength(0);
                        userSequence.clear();
                        userSequenceString.setLength(0);
                        numOfItems = 4;
                        numUserInputs = 0;
                        gameStart = false;
                        scoreLevel = 0;
                        start.setText("Start New Game");
                        score.setText(0 + "");
                        textView.setText("Score: ");
                        soundSix.start();
                        break;
                    }
                }
                catch(IndexOutOfBoundsException e)
                {
                    start.setText("Exception Occurred");
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        AudioManager audioManager = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            if(item.isChecked() == true)
            {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                item.setChecked(false);
                sound = false;
            }
            else if(item.isChecked() == false)
            {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                item.setChecked(true);
                sound = true;
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
//DISCARDED/SAVED CODE
/* WHILE LOOP. DOES NOT SEEM TO WORK IN ANDROID JAVA
while(keepGoing == true) //NO GOOD, DO NOT USE WHILE LOOPS IN THE UI. USE IN A THREAD INSTEAD
                {
                    new CountDownTimer(10000, 1000)
                    {
                        public void onTick(long mill)
                        {
                            if(numUserInputs == 5)
                            {
                                keepGoing = false;
                            }
                            if(swap == true)
                            {
                                start.setBackgroundColor(Color.BLUE); //No good. Cannot call on something created in the UI
                                numUserInputs++;
                            }
                            else
                            {
                                start.setBackgroundColor(Color.GREEN); //No good. Cannot call on something created in the UI
                                numUserInputs++;
                            }
                        }
                        @Override
                        public void onFinish()
                        {
                            start.setBackgroundColor(Color.RED); //No good. Cannot call on something created in the UI
                        }
                    }.start();
                }
 */

/* THREADS USING RUNNABLES
Boolean swap = true;
Runnable runnableOne = new Runnable() //PROBLEMS WITH THIS RUNNABLE, CAUSES A CRASH WHEN INVOKED: "java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()"
    {
        @Override
        public void run()
        {
            Looper.prepare();
            //while(keepGoing == true) //NO GOOD, DO NOT USE WHILE LOOPS IN THE UI. USE IN A THREAD INSTEAD
            do
            {
                new CountDownTimer(10000, 1000)
                {
                    public void onTick(long mill)
                    {
                        if(numUserInputs == 5)
                        {
                            keepGoing = false;
                        }
                        if(swap == true)
                        {
                            start.setBackgroundColor(Color.BLUE); //No good. Cannot call on something created in the UI
                            numUserInputs++;
                            swap = false;
                        }
                        else
                        {
                            start.setBackgroundColor(Color.GREEN); //No good. Cannot call on something created in the UI
                            numUserInputs++;
                            swap = true;
                        }
                    }
                    @Override
                    public void onFinish()
                    {
                        start.setText("Start"); //No good. Cannot call on something created in the UI

                    }
                }.start();
            }while(keepGoing == true);
        };
    };
    //Thread threadOne = new Thread(runnableOne); //USE WITH THE RUNNABLE ABOVE (THE RUNNABLE WHICH DOESN'T AS OF YET WORK)

 */

/* THREADS USING ASYNCTASK
//UserInputThread uit = new UserInputThread();
                //uit.execute();
public class UserInputThread extends AsyncTask<Boolean,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Boolean... params)
        {
            while(keepGoing == true) //NO GOOD, DO NOT USE WHILE LOOPS IN THE UI. USE IN A THREAD INSTEAD
            {
                new CountDownTimer(10000, 1000)
                {
                    public void onTick(long mill)
                    {
                        start = (Button)findViewById(R.id.start);
                        if(numUserInputs == 5)
                        {
                            keepGoing = false;
                        }
                        if(swap == true)
                        {
                            start.setBackgroundColor(Color.BLUE);
                            numUserInputs++;
                            swap = false;
                        }
                        else
                        {
                            start.setBackgroundColor(Color.GREEN);
                            numUserInputs++;
                            swap = true;
                        }
                    }
                    @Override
                    public void onFinish()
                    {
                        start.setBackgroundColor(Color.RED);
                    }
                }.start();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values)
        {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }

    //Thread threadOne = new Thread(runnableOne); //INVOKING CAUSES A PROGRAM CRASH
                //threadOne.start(); //INVOKING CAUSES A PROGRAM CRASH
 */