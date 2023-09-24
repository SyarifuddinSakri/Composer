
public class Composer {
	public static void main(String[] args) {
		NoteGenerator.sampleRate=44100;
		NoteGenerator.sampleSize=16;
		NoteGenerator.channel=1;//1 = mono , 2 = stereo
		
		NoteGenerator noteA = new NoteGenerator();

		noteA.generateSound();
	}
}
