package com.sidescroller.main;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

/**Not my source code
 * 
 * Takes an audio source in and plays it. ONLY SUPPORTS WAVs.
 */
public class Audio {

	private String fileName;
	private Clip audioClip;
	public Audio(String fileName)
	{
		this.fileName = fileName;
		try{
			File audioFile = new File(fileName);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			AudioFormat format = audioStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			audioClip = (Clip) AudioSystem.getLine(info);
			audioClip.open(audioStream);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void play()
	{
		audioClip.start();
	}
	
	public void restart(){
		audioClip.setFramePosition(0);
	}
	
	public void rAndP(){
		audioClip.setFramePosition(0);
		audioClip.start();
	}
	
	public void stop()
	{
		audioClip.close();
	}
	public String getFileName()
	{
		return fileName;
	}
	
	public void loop(int x){
		if(x == 1){
			audioClip.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}
	
	public Clip getClip(){
		return audioClip;
	}
	
}