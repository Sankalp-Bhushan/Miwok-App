package com.example.practiceandroidjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static android.media.AudioManager.AUDIOFOCUS_LOSS;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;

public class ColorActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    private AudioManager audioManager;

    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if(focusChange == AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                // pause playback
                // the AUDIOFOCUS_LOSS_TRANSIENT means  that we have lost asio focus for a short amount of time.
                // the AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK means that our app is allowed to continue playing sound but at a lower volume.
                // we will treat both case same way because our app is playing sort sound files.
                //
                //pause playback and reset the player to the start of the file.That way , we can play the
                // word from the beginning when we resume playback.
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            } else if( focusChange == AudioManager.AUDIOFOCUS_GAIN){
                // the AudioManager.AUDIOFOCUS_GAIN means that we have regained focus and
                //we can resume playback
                mediaPlayer.start();
            } else if( focusChange == AUDIOFOCUS_LOSS){
                // the AUDIOFOCUS_LOSS means we have lost audio focus and stop
                // playback and clean resources

                releaseMediaPlayer();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.words_view);

        // create and setup the {@Link AudioManager} to request audio focus
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // listView setOnClickListener can use this only if this is final or global
        final ArrayList<Word> colors = new ArrayList<>();
        colors.add(new Word("red", "weṭeṭṭi", R.mipmap.color_red,R.raw.color_red));
        colors.add(new Word("mustard yellow", "chiwiiṭә", R.mipmap.color_mustard_yellow,R.raw.color_mustard_yellow));
        colors.add(new Word("dusty yellow", "ṭopiisә", R.mipmap.color_dusty_yellow,R.raw.color_dusty_yellow));
        colors.add(new Word("green", "chokokki", R.mipmap.color_green,R.raw.color_green));
        colors.add(new Word("brown", "ṭakaakki", R.mipmap.color_brown,R.raw.color_brown));
        colors.add(new Word("gray", "ṭopoppi", R.mipmap.color_gray,R.raw.color_gray));
        colors.add(new Word("black", "kululli", R.mipmap.color_black,R.raw.color_black));
        colors.add(new Word("white", "kelelli", R.mipmap.color_white,R.raw.color_white));

        WordAdapter itemsAdapter = new WordAdapter(this,colors,R.color.category_colors);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = colors.get(position);

                // release the the media player because it might be in the memory or may be playing
                releaseMediaPlayer();

                // request audio focus for playback
                int result = audioManager.requestAudioFocus(onAudioFocusChangeListener,
                        // use the music stream
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if( result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // we have a audio focus now,

                    // create and setup the {@ Link MediaPlayer }  for the audio resources associated with the
                    // current number
                    mediaPlayer = MediaPlayer.create(ColorActivity.this, word.getMediaPlayerId());

                    // start the audio file
                    mediaPlayer.start();

                    /**
                     * setup a listener on the mediaPlayer, so that we can stop and release the
                     * mediaPlayer once the sound is finished playing
                     */
                    mediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });
    }

    protected void onStop(){
        super.onStop();
        // when activity is stopped, release the mediaPlayer resources because we won't be
        // playing any more sound
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;

            // Regardless of whether or not we were granted audio, focus, abandon it. this also
            // unregister the AudioFocusChangeListener so we don't get anymore callbacks.
            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }
}