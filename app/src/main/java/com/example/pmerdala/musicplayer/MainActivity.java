package com.example.pmerdala.musicplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean playing;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    int MAX_VOLUME;
    Toast toast;
    private final static int SKIP_RANGE = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListener();
        makeMediaPlaer();
    }

    private void makeMediaPlaer(){
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        MAX_VOLUME = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                messageShow("Done");
            }
        });
    }

    private void addListener() {
        View view = findViewById(R.id.play_button);
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    play(v);
                }
            });
        }
        view = findViewById(R.id.pause_button);
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pause(v);
                }
            });
        }
        view = findViewById(R.id.volume_up_button);
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    volumeUp(v);
                }
            });
        }
        view = findViewById(R.id.volume_down_button);
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    volumeDown(v);
                }
            });
        }
        view = findViewById(R.id.forward_button);
        if (view != null){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    forward(v);
                }
            });
        }
        view = findViewById(R.id.backward_button);
        if (view!=null){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backward(v);
                }
            });
        }

    }

    private void messageShow(String message){
        if (toast!=null){
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    protected void play(View view) {
        if (!mediaPlayer.isPlaying()) {
            messageShow("Play");
            mediaPlayer.start();
        }
    }

    protected void pause(View view) {
        if (mediaPlayer.isPlaying()) {
            messageShow("Pause");
            mediaPlayer.pause();
        }
    }

    private void setVolume(int currVolume, String message) {
        //float volume = (float) (1f - (Math.log(MAX_VOLUME - currVolume) / Math.log(MAX_VOLUME)));
        //mediaPlayer.setVolume(volume, volume);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,currVolume,0);
        messageShow( message+ " " + currVolume + "/" + MAX_VOLUME);
    }

    protected void volumeUp(View view) {
        int current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (current_volume==MAX_VOLUME){
            return;
        }
        current_volume += 1;
        if (current_volume > MAX_VOLUME) {
            current_volume = MAX_VOLUME;
        }
        setVolume(current_volume,"Volume UP");
    }

    protected void volumeDown(View view) {
        int current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (current_volume==0){
            return;
        }
        current_volume -= 1;
        if (current_volume < 0) {
            current_volume = 0;
        }
        setVolume(current_volume,"Volume DOWN");
    }

    private void seekTo(int currentPosition, int duration, String message){
        mediaPlayer.seekTo(currentPosition);
        messageShow(message + currentPosition/1000 + "s/" + duration/1000 + "s.");

    }
    protected void forward(View view){
        int duration = mediaPlayer.getDuration();
        int currentPosition = mediaPlayer.getCurrentPosition();
        currentPosition+=SKIP_RANGE;
        if (currentPosition>duration){
            currentPosition=0;
        }
        seekTo(currentPosition,duration,"Forward to :");
    }

    protected void backward(View view){
        int duration = mediaPlayer.getDuration();
        int currentPosition = mediaPlayer.getCurrentPosition();
        currentPosition-=SKIP_RANGE;
        if (currentPosition<0){
            if (duration>SKIP_RANGE)
                currentPosition=duration-SKIP_RANGE;
            else
                currentPosition=duration;
        }
        seekTo(currentPosition,duration,"Backward to:");
    }
}
