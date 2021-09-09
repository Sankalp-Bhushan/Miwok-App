package com.example.practiceandroidjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.autofill.AutofillManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.media.AudioManager.AUDIOFOCUS_LOSS;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;

public class NumberActivity extends AppCompatActivity{

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

        final ArrayList<Word> numbers = new ArrayList<>();
        numbers.add(new Word("one","lutti",R.mipmap.number_one, R.raw.number_one));
        numbers.add(new Word("two","otiiko",R.mipmap.number_two,R.raw.number_two));
        numbers.add(new Word("three","tolookosu",R.mipmap.number_three,R.raw.number_three));
        numbers.add(new Word("four","oyyisa",R.mipmap.number_four,R.raw.number_four));
        numbers.add(new Word("five","massokka",R.mipmap.number_five,R.raw.number_five));
        numbers.add(new Word("six","temmokka",R.mipmap.number_six,R.raw.number_six));
        numbers.add(new Word("seven","kenekaku",R.mipmap.number_seven,R.raw.number_seven));
        numbers.add(new Word("eight","kawinta",R.mipmap.number_eight,R.raw.number_eight));
        numbers.add(new Word("nine","wo'e",R.mipmap.number_nine,R.raw.number_nine));
        numbers.add(new Word("ten","na'aacha",R.mipmap.number_ten,R.raw.number_ten));

       // ArrayAdapter<Word> itemsAdapter = new ArrayAdapter<Word>(this, android.R.layout.simple_list_item_1, numbers);

        WordAdapter itemsAdapter = new WordAdapter(this,numbers,R.color.category_numbers);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(itemsAdapter);
        //listView.setOnItemClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = numbers.get(position);

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
                    mediaPlayer = MediaPlayer.create(NumberActivity.this, word.getMediaPlayerId());

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

//    this is defined only for ArrayAdapter with one text
//    @Override
//    public void onItemClick (AdapterView < ? > parent, View view,int position, long id){
//        String number = ((TextView) view).getText().toString();
//        Toast.makeText(getApplicationContext(),"clicked "+number,Toast.LENGTH_SHORT).show();
//    }
}