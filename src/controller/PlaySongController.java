package controller;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

/**
 * With this class you can play a song or ringtone or something like that
 * Java supports only WAV and AU files
 * 
 * @author danielgeerts7
 * @version 30-01-2018
 */
public class PlaySongController extends Thread implements LineListener {
	
	private boolean playCompleted;
	private static boolean isPlaying;
	private static boolean canPlay = false;
    
    private String audioFilePath;
    
    /**
     * Sets the path to the song that you want to play
     * 
     * @param audioPath
     */
    public PlaySongController(String audioPath) {
    	this.audioFilePath = audioPath;
    	isPlaying = false;
    }
    
    /**
     * With the extra parameter you can choose is the application plays the sound where the path points to
	 *
     * @param audioPath
     * @param canPlay
     */
    public PlaySongController(String audioPath, boolean canPlay) {
    	this(audioPath);
    	this.canPlay = canPlay;
    }
    

	@Override
	public void run() {
		if (canPlay) {
			if (!isPlaying) {
				try {
					play();
				} catch (Exception e) {
					System.out.println(e.toString());
				}
				// Done playing
				isPlaying = false;
				System.out.println("Closing music thread!");
				Thread.currentThread().interrupt();
			} else {
				System.out.println("Already playing music!");
			}
		}
	}
     
    /**
     * Play a given audio file.
     * @param audioFilePath Path of the audio file.
     */
    private void play() {
    	isPlaying = true;
    	
        File audioFile = new File(audioFilePath);
 
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
 
            AudioFormat format = audioStream.getFormat();
 
            DataLine.Info info = new DataLine.Info(Clip.class, format);
 
            Clip audioClip = (Clip) AudioSystem.getLine(info);
 
            audioClip.addLineListener(this);
 
            audioClip.open(audioStream);
             
            audioClip.start();
             
            while (!playCompleted) {
                // wait for the playback completes
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
             
            audioClip.close();
             
        } catch (UnsupportedAudioFileException ex) {
            System.out.println("The specified audio file is not supported.");
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }
         
    }

	@Override
	public void update(LineEvent event) {
		LineEvent.Type type = event.getType();
        
        if (type == LineEvent.Type.START) {
            System.out.println("Playback started.");
             
        } else if (type == LineEvent.Type.STOP) {
            playCompleted = true;
            System.out.println("Playback completed.");
        }
	}
	
	/**
	 * @return if the sound is playing or not
	 */
	public boolean isPlaying() {
		return isPlaying;
	}
	
	/**
	 * @return if the sound may play or not
	 */
	public boolean canPlay() {
		return canPlay;
	}
}
