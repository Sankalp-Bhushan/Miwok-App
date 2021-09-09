package com.example.practiceandroidjava;


import android.media.MediaPlayer;

public class Word{

    private int mediaPlayerId;
    private String miwokTranslation;
    private String defaultTranslation;
    private int imageResourseId = HAS_IMAGE;
    private static final int HAS_IMAGE = -1;

    public String getMiwokTranslation(){
        return miwokTranslation;
    }

    public void setMiwokTranslation(String miwok){
        miwokTranslation = miwok;
    }

    public String getDefaultTranslation(){
        return defaultTranslation;
    }

    public void setDefaultTranslation(String mdefault){
        defaultTranslation = mdefault;
    }

    public int getImageResourseId(){
        return imageResourseId;
    }

    public void setImageResourseId(int imageId){
        imageResourseId = imageId;
    }

    public int getMediaPlayerId(){ return mediaPlayerId; }

    public void setMediaPlayerId(int mediaPlayerId){ this.mediaPlayerId = mediaPlayerId; }

    public Word(String text1, String text2,int mediaPlayerId){
        miwokTranslation = text2;
        defaultTranslation = text1;
        this.mediaPlayerId = mediaPlayerId;
    }

    public Word(String text1, String text2,int imageResourseId,int mediaPlayerId){
        miwokTranslation = text2;
        defaultTranslation = text1;
        this.imageResourseId = imageResourseId;
        this.mediaPlayerId = mediaPlayerId;
    }

    /*
    *
    * to check whether the object has image or not
    *
     */

    public boolean hasImage(){
        if(imageResourseId==HAS_IMAGE) return false;
        return true;
    }

}
