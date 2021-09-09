package com.example.practiceandroidjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//  No need to make a class you can directly use it

//        NumberClickListener clickListener = new NumberClickListener();
//        TextView numbers = (TextView) findViewById(R.id.text_view1);
//        numbers.setOnClickListener(new NumberClickListener());

        TextView numbers = (TextView) findViewById(R.id.text_view1);
        numbers.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"Open the list of Numbers",Toast.LENGTH_SHORT).show();
                Intent numbersIntent = new Intent(MainActivity.this,NumberActivity.class);
                startActivity(numbersIntent);
            }
        });

        TextView familyMembers = (TextView) findViewById(R.id.text_view2);
        familyMembers.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(),"Open the list of Numbers",Toast.LENGTH_SHORT).show();
                Intent numbersIntent = new Intent(MainActivity.this,FaminlyActivy.class);
                startActivity(numbersIntent);
            }
        });

        TextView colors = (TextView) findViewById(R.id.text_view3);
        colors.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               // Toast.makeText(view.getContext(),"Open the list of Numbers",Toast.LENGTH_SHORT).show();
                Intent numbersIntent = new Intent(MainActivity.this,ColorActivity.class);
                startActivity(numbersIntent);
            }
        });

        TextView phrases = (TextView) findViewById(R.id.text_view4);
        phrases.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(),"Open the list of Numbers",Toast.LENGTH_SHORT).show();
                Intent numbersIntent = new Intent(MainActivity.this,PhraseActivity.class);
                startActivity(numbersIntent);
            }
        });

    }
//     public void openNumbers(View view){
//         Intent i = new Intent(this, NumberActivity.class);
//         startActivity(i);
//     }

//    public void openFamily(View view){
//        Intent i = new Intent(this, FaminlyActivy.class);
//        startActivity(i);
//    }
//
//    public void openPhrases(View view){
//        Intent i = new Intent(this, PhraseActivity.class);
//        startActivity(i);
//    }
//
//    public void openColor(View view){
//        Intent i = new Intent(this, ColorActivity.class);
//        startActivity(i);
//    }

}