package com.example.practiceandroidjava;
import android.view.View;
import android.widget.Toast;

public class NumberClickListener implements View.OnClickListener{
    public void onClick(View view){
        Toast.makeText(view.getContext(),"Open the list of Numbers",Toast.LENGTH_SHORT).show();
    }
}
