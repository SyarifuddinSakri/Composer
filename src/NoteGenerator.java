import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

public class NoteGenerator {
	
	public static float sampleRate;
	public static int sampleSize;
	public static int channel;
	public static List<byte[]> noteArray = new ArrayList<>();
	Mixer mixer;
	AudioFormat format;
	public NoteGenerator() {
		//get the default audio mixer
		mixer = AudioSystem.getMixer(null);
		//Set up audio Format
		format = new AudioFormat(sampleRate, sampleSize, channel, true, false);
	}
	public synchronized void generateSound(){
	    try {
	        // Create a data line info for the source data line
	        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
	
	        // Open the source data line
	        SourceDataLine line = (SourceDataLine) mixer.getLine(info);
	        line.open(format);
	        line.start();
	        
	        noteArray.add(generateSoundNote(440,1000));
	        noteArray.add(generateSoundNote(293.66,1000));
	        noteArray.add(generateSoundNote(200,1000));
	        
	        
	        // Generate the sound note
	        byte[] note = byteListToArray(noteArray);
	
	        // Play the sound note
	        line.write(note, 0, note.length);
	        line.drain();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	// Generate a sound note with the given frequency and duration
	private static byte[] generateSoundNote(double frequency, int duration) {
	    int sampleRate = 44100; // Samples per second
	    double t = 0.0; // Time in seconds
	    double increment = (2.0 * Math.PI * frequency) / sampleRate;
	    int numSamples = (int) (sampleRate * duration / 1000.0);
	    byte[] note = new byte[2 * numSamples];
	
	    for (int i = 0; i < numSamples; i++) {
	        short sample = (short) (Short.MAX_VALUE * Math.sin(t));
	        note[2 * i] = (byte) (sample & 0xFF);
	        note[2 * i + 1] = (byte) ((sample >> 8) & 0xFF);
	        t += increment;
	    }
	
	    return note;
	}
	
	private byte[] byteListToArray(List<byte[]> byteArrayList) {
		int totalLength = byteArrayList.stream().mapToInt(arr -> arr.length).sum();
		byte[] concatenatedByteArray = new byte[totalLength];
		int currentIndex = 0;
        for (byte[] byteArray : byteArrayList) {
            System.arraycopy(byteArray, 0, concatenatedByteArray, currentIndex, byteArray.length);
            currentIndex += byteArray.length;
        }
        return concatenatedByteArray;
	}
}

