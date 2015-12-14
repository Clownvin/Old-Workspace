package sound;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import javazoom.jl.player.Player;

public class MP3 {
  private String filename;
  private Player player;
  private boolean closeRequested = false;
  
  public MP3(final String filename) {
    this.filename = filename;
  }
  
  public boolean isComplete(){
	  return this.player.isComplete();
  }
  
  private synchronized boolean closeRequested(){
	  return this.closeRequested;
  }
  
  public synchronized void requestClose(final boolean state){
	  if(this.closeRequested!=state){
		  this.closeRequested = state;
	  }
  }
  
  public synchronized void playLoop(){
	  play();
	  new Thread(){
		  public void run(){
	  while(!closeRequested()&&player!=null){
		  if(player.isComplete()){
			  close();
			  play();
		  }
	  }
	  if(player!=null)
		  player.close();
		  }
	  }.start();
  }
  
  public void play() {
	  if(this.closeRequested)
		  this.closeRequested = false;
    try {
      FileInputStream fis = new FileInputStream(this.filename);
      BufferedInputStream bis = new BufferedInputStream(fis);

      this.player = new Player(bis);
    } catch (Exception e) {
        System.err.printf("%s\n", e.getMessage());
    }

    new Thread() {
      @Override
      public void run() {
        try {
          player.play();
        } catch (Exception e) {
            System.err.printf("%s\n", e.getMessage());
        }
      }
    }.start();
  }

  /**
   * Closes the Player
   */
  public void close() {
    if (this.player != null) {
      this.player.close();
    }
  }

  /////////////////////////

  /**
   * Plays '01 Maenam.mp3' in an infinite loop
   */
  public static void playMaenam() {
    MP3 mp3 = new MP3("./01 Maenam.mp3");

    mp3.play();

    while (true) {
      if (mp3.player.isComplete()) {
        mp3.close();
        mp3.play();
      }
    }
  }
}