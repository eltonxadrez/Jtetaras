package jtetas.sound;

import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundEffect {
	
    public Long currentFrame; 
    public Clip clip; 
    public AudioInputStream audioInputStream; 
    public String status; 
    public String filePath; 
  
    public SoundEffect(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException { 
        this.audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(filePath)); 
        this.filePath = filePath;
        this.clip = AudioSystem.getClip(); 
        this.clip.open(audioInputStream); 
        this.clip.loop(Clip.LOOP_CONTINUOUSLY); 
    } 

	public void gotoChoice(int c) throws IOException, LineUnavailableException, UnsupportedAudioFileException { 
        switch (c) { 
            case 1: 
                pause(); 
                break; 
            case 2: 
                resumeAudio(); 
                break; 
            case 3: 
                restart(); 
                break; 
            case 4: 
                stop(); 
                break; 
            case 5: 
                System.out.println("Enter time (" + 0 + ", " + this.clip.getMicrosecondLength() + ")");
                Scanner sc = new Scanner(System.in);
                long c1 = sc.nextLong();
                jump(c1);
                break;
        } 
    } 
      
    public void play() { 
        this.clip.start(); 
        this.status = "play"; 
    } 
    
    public void pause() { 
        if (this.status.equals("paused")) { 
            System.out.println("audio is already paused"); 
            return; 
        }
        this.currentFrame =  
        this.clip.getMicrosecondPosition(); 
        this.clip.stop(); 
        this.status = "paused"; 
    } 
      
    public void resumeAudio() throws UnsupportedAudioFileException, IOException, LineUnavailableException { 
        if (this.status.equals("play")) { 
            System.out.println("Audio is already being played"); 
            return; 
        } 
        this.clip.close();
        resetAudioStream();
        this.clip.setMicrosecondPosition(this.currentFrame);
        this.play();
    } 
      
    public void restart() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
    	this.clip.stop();
    	this.clip.close();
        resetAudioStream();
        this.currentFrame = 0L;
        this.clip.setMicrosecondPosition(0);
        this.play();
    } 
      
    public void stop() throws UnsupportedAudioFileException, IOException, LineUnavailableException { 
        this.currentFrame = 0L; 
        this.clip.stop(); 
        this.clip.close(); 
    } 
      
    public void jump(long c) throws UnsupportedAudioFileException, IOException, LineUnavailableException { 
        if (c > 0 && c < this.clip.getMicrosecondLength()) { 
            this.clip.stop(); 
            this.clip.close(); 
            resetAudioStream(); 
            this.currentFrame = c; 
            this.clip.setMicrosecondPosition(c); 
            this.play(); 
        } 
    } 
      
    public void resetAudioStream() throws UnsupportedAudioFileException, IOException, LineUnavailableException { 
    	this.audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(this.filePath)); 
    	this.clip.open(this.audioInputStream); 
    	this.clip.loop(Clip.LOOP_CONTINUOUSLY); 
    } 
}
