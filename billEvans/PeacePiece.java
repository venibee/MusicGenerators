package billEvans;

import java.util.ArrayList;
import java.util.Random;

import javax.sound.midi.*;
import static javax.sound.midi.ShortMessage.*;

public class PeacePiece {
	private ArrayList<Integer> listOfNotes=new ArrayList<Integer>(); 
	int[] notes ={0,67,0,74,0,79,0,84,83,81,76,69,62,64,60,55};
	private Sequencer sequencer;
	private Sequence sequence;
	private Track track;
	private int numOfBars=16;
	private int barCount;
	
//	private void fillArraList() {	
//			for(int a:notes) {
//				listOfNotes.add(a);
//			}
//		
//	}
	
	private void setUpMidi() {
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequence = new Sequence(Sequence.PPQ,2);
			track = sequence.createTrack();
			sequencer.setTempoInBPM(50);
		}catch(Exception e) {
			e.printStackTrace();
		}
	
	}
	
	private void buildTrackAndStart() {
		for (int i = 0; i< numOfBars;i++) {
			buildBass();
			listOfNotes.add(0);
			listOfNotes.add(notes[barCount]);
			if(barCount>3) {
			makeBar();
			}
			barCount++;
		}
		try {
			sequencer.setSequence(sequence);
			sequencer.setTempoInBPM(50);
			sequencer.start();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void buildBass() {
		int[][]bassNotes = {{31},{24},{48,52},{43,45,48,53}};
			for(int i=0;i<4;i++) {
				for(int j = 0;j<bassNotes[i].length;j++) {
					track.add(makeEvent(NOTE_ON,1,bassNotes[i][j],80,8*barCount+i*2));
					track.add(makeEvent(NOTE_OFF,1,bassNotes[i][j],80,8*barCount+i*2+2));
				}
			}
		
	}
	
	private void makeBar() {
		for(int i=0;i<8;i++) {
			Random random = new Random();
			int x = random.nextInt(listOfNotes.size());		
			int note = listOfNotes.get(x);
			
			if(note != 0) {
				track.add(makeEvent(NOTE_ON,1,note,100,8*barCount+i));
				track.add(makeEvent(NOTE_OFF,1,note,100,8*barCount+i+2));
			}
		}
	}
	public static MidiEvent makeEvent(int cmd, int chnl, int one, int two, int tick) {
		MidiEvent event = null;
		try {
			ShortMessage msg = new ShortMessage();
			msg.setMessage(cmd, chnl, one, two);
			event = new MidiEvent(msg, tick);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return event;
	}
	public static void main(String[] args) {
		PeacePiece test = new PeacePiece();

		test.setUpMidi();
		test.buildTrackAndStart();
	}
	


}
