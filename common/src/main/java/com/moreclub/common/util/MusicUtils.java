package com.moreclub.common.util;

import android.icu.text.RuleBasedCollator;
import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * Created by Captain on 2017/3/3.
 */

public class MusicUtils implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener {

    public MediaPlayer mediaPlayer;

    private String playerUrl;

    private MusicPlayCompletionListener listener;

    public  MusicUtils(){
        try{
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void pause(){

        mediaPlayer.pause();
    }

    public void stop(){
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }


    public void playUrl(){
        new Thread() {
            public void run() {
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(playerUrl);
                    mediaPlayer.prepare();
                } catch (Exception e) {
                }
            }
        }.start();
    }

    public boolean isPlayerStatus(){
        return mediaPlayer.isPlaying();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        if (listener!=null){
            listener.musicPlayCompletion();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    public String getPlayerUrl() {
        return playerUrl;
    }

    public void setPlayerUrl(String playerUrl) {
        this.playerUrl = playerUrl;
    }

    public interface MusicPlayCompletionListener{

        void musicPlayCompletion();
    }

    public void setListener(MusicPlayCompletionListener listener) {
        this.listener = listener;
    }

}
