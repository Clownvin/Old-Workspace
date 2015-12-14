package util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileReader;
public class Reader {

	private BufferedReader reader = null;
	
	public Reader(String s){
		try{
			reader = new BufferedReader(new FileReader(s));
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public void close(){
		try{
			reader.close();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public void mark(int i){
		try{
			reader.mark(i);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public boolean markSupported(){
		return reader.markSupported();
	}
	
	public String readLine(){
		String line = null;
		try{
			line = reader.readLine();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		return line;
	}
}
