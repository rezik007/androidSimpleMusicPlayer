package com.example.patryk.wasko1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


import static com.example.patryk.wasko1.MainActivity.CHOOSEN_IMG;

/**
 * Created by Patryk on 14.03.2017.
 */

public class DetailsActivity extends Activity implements MediaPlayer.OnCompletionListener {

    private static final int REQUEST_READWRITE_STORAGE = 0;
    public int drawableId;
    private RelativeLayout contentView;
    private MediaPlayer mp;
    private SeekBar volumeSeekbar = null;
    private AudioManager audioManager = null;
    private boolean resumeAllow = false;
    private ImageButton btnPlay;
    private ImageButton btnPouse;
    private SeekBar songProgressBar;
    private SongsManager songManager;
    private int currentSongIndex = 0;
    private Handler mHandler = new Handler();
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        initControls();


        final ImageView img = (ImageView) findViewById(R.id.img_big);
        Button backButton = (Button) findViewById(R.id.button);
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnPouse = (ImageButton) findViewById(R.id.btnPouse);
        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
        contentView = (RelativeLayout) findViewById(R.id.details);

        mp = new MediaPlayer();
        songManager = new SongsManager();


        // Getting all songs list
//        ActivityCompat.requestPermissions(DetailsActivity.class, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, result);
        int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READWRITE_STORAGE);
        }
        songsList = songManager.getPlayList();

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
                mp.stop();
                mp.reset();
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // check for already playing
                if (resumeAllow) {
                    mp.start();
                } else {
                    if (mp != null) {
                        playSong(currentSongIndex);
                    }
                }
            }
        });
        btnPouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.pause();
                    resumeAllow = true;
                }
            }
        });


        // Get the Intent that started this activity and extract the string
        Bundle bundle = getIntent().getExtras();
        drawableId = bundle.getInt(CHOOSEN_IMG);
        currentSongIndex = drawableId;


        img.setImageResource(MainActivity.imageIDs[drawableId]);
        contentView.setOnTouchListener(new OnSwipeTouchListener(DetailsActivity.this) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                if (drawableId > 0) {
                    img.setImageResource(MainActivity.imageIDs[drawableId -= 1]);
                    mp.stop();
                    mp.reset();
                    currentSongIndex = drawableId;
                    resumeAllow = false;
                }

            }

            public void onSwipeLeft() {
                if (drawableId < 11) {
                    img.setImageResource(MainActivity.imageIDs[drawableId += 1]);
                    mp.stop();
                    mp.reset();
                    currentSongIndex = drawableId;
                    resumeAllow = false;
                }
            }

            public void onSwipeBottom() {

            }

        });

    }

    @Override
    public void onBackPressed() {
    }

    /**
     * Function to play a song
     *
     * @param songIndex - index of song
     */

    public void playSong(int songIndex) {
        // Play song
        try {
            mp.reset();
            mp.setDataSource(songsList.get(songIndex).get("songPath"));
            mp.prepare();
            mp.start();

            // Changing Button Image to pause image
            btnPlay.setImageResource(R.drawable.playbtn);


        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    private void initControls() {
        try {
            volumeSeekbar = (SeekBar) findViewById(R.id.songProgressBar);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));


            volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
