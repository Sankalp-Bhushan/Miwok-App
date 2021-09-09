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

public class PhraseActivity extends AppCompatActivity {

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

       final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("Where are you going?", "minto wuksus",R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?", "tinnә oyaase'nә",R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is...", "oyaaset...",R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?", "michәksәs?",R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I’m feeling good.", "kuchi achit",R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?", "әәnәs'aa?",R.raw.phrase_are_you_coming));
        words.add(new Word("Yes, I’m coming.", "hәә’ әәnәm",R.raw.phrase_yes_im_coming));
        words.add(new Word("I’m coming.", "әәnәm",R.raw.phrase_im_coming));
        words.add(new Word("Let’s go.", "yoowutis",R.raw.phrase_lets_go));
        words.add(new Word("Come here.", "әnni'nem",R.raw.phrase_come_here));

        WordAdapter itemsAdapter = new WordAdapter(this,words,R.color.category_phrases);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = words.get(position);

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
                    mediaPlayer = MediaPlayer.create(PhraseActivity.this, word.getMediaPlayerId());

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

        /* you can also use the given line of codes and there would be no need to change word class
         if (position == 0) {
                mMediaPlayer = MediaPlayer.create(NumbersActivity.this, R.raw.number_one);
            } else if (position == 1) {
                mMediaPlayer = MediaPlayer.create(NumbersActivity.this, R.raw.number_two);
            } else if (position == 2) {
                mMediaPlayer = MediaPlayer.create(NumbersActivity.this, R.raw.number_three);
            } else if (position == 3) {
                mMediaPlayer = MediaPlayer.create(NumbersActivity.this, R.raw.number_four);
            } else if (position == 4) {
                mMediaPlayer = MediaPlayer.create(NumbersActivity.this, R.raw.number_five);
            } else if (position == 5) {
                mMediaPlayer = MediaPlayer.create(NumbersActivity.this, R.raw.number_six);
            } else if (position == 6) {
                mMediaPlayer = MediaPlayer.create(NumbersActivity.this, R.raw.number_seven);
            } else if (position == 7) {
                mMediaPlayer = MediaPlayer.create(NumbersActivity.this, R.raw.number_eight);
            } else if (position == 8) {
                mMediaPlayer = MediaPlayer.create(NumbersActivity.this, R.raw.number_nine);
            } else if (position == 9) {
                mMediaPlayer = MediaPlayer.create(NumbersActivity.this, R.raw.number_ten);
            }
                mMediaPlayer.start();
        }
    });`
         */
    }

    protected void onStop(){
        super.onStop();
        // when activity is stopped, release the mediaPlayer resources because we won't be
        // playing any more sound
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
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