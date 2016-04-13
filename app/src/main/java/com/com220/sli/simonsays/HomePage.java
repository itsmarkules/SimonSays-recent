package com.com220.sli.simonsays;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button play, score, aboutUs;
        final MediaPlayer soundSeven = MediaPlayer.create(this, R.raw.smb_pipe);
        final MediaPlayer soundEight = MediaPlayer.create(this, R.raw.smb_world_clear);
        final MediaPlayer soundNine = MediaPlayer.create(this, R.raw.smb_stage_clear);
        final MediaPlayer soundTen = MediaPlayer.create(this, R.raw.smb_bowserfalls);

        play = (Button)findViewById(R.id.MainActivity);
        score = (Button)findViewById(R.id.ScorePage);
        aboutUs = (Button)findViewById(R.id.AboutPage);

        play.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                soundSeven.start();
                Intent playPage = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(playPage);
            }
        });
        score.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //soundEight.start();
                //soundSeven.start();
                Intent scorePage = new Intent(getApplicationContext(), ScorePage.class);
                startActivity(scorePage);
            }
        });
        aboutUs.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //soundNine.start();
                //soundSeven.start();
                Intent aboutPage = new Intent(getApplicationContext(), AboutPage.class);
                startActivity(aboutPage);
            }
        });
        Button back = (Button)findViewById(R.id.homeClose);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                soundTen.start();
                finish();
            }
        });
    }
}
