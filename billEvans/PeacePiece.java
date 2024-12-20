package billEvans;

import java.util.ArrayList;
import java.util.Random;

import javax.sound.midi.*;
import static javax.sound.midi.ShortMessage.*;

public class PeacePiece {
	private ArrayList<Integer> listOfNotes = new ArrayList<Integer>(); // holding the actually available notes for the
																		// randomized melody
	int[] notes = {0,0,71,72,74,67,65,62,60,59,69,77};  // all possible notes for the melody (
															// will be fed to the ArrayList
															// gradually)
	
//	{ 0,67, 62, 59,55, 72, 71, 69, 57, 74, 48,60,65,53 }
	private Sequencer sequencer;
	private Sequence sequence;
	private Track track;
	private int numOfBars = 16;
	private int barCount;
	

	private void setUpMidi() {
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequence = new Sequence(Sequence.PPQ, 2);
			track = sequence.createTrack();
			
			sequencer.setTempoInBPM(50);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void buildTrackAndStart() {
		for (int i = 0; i < numOfBars; i++) {
			buildBass();
			fillMelodyArray();
//			listOfNotes.add(0);
//			listOfNotes.add(notes[barCount]);
			
			makeBar();
			barCount++;
		}
			track.add(makeEvent(NOTE_ON, 1, 24, 70, 8 * barCount + 1));//add final note
			track.add(makeEvent(NOTE_OFF, 1, 24, 70, 8 * barCount + 4));
		try {
			sequencer.setSequence(sequence);
			sequencer.setTempoInBPM(50);
			sequencer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void fillMelodyArray() {
		if(barCount<notes.length) {
			
			listOfNotes.add(notes[barCount]);
			if(barCount%3==0) {
				listOfNotes.add(0);
			}
		}	
	}

	private void buildBass() {
		int[][] bassNotes = { { 31 }, { 24 }, { 48, 52 }, { 43, 45, 48, 53 } };
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < bassNotes[i].length; j++) {

				track.add(makeEvent(NOTE_ON, 1, bassNotes[i][j], 
									110 - (bassNotes[i].length * 12), //lower the volume of individual note sounds in chords
									8 * barCount + i * 2));
				track.add(makeEvent(NOTE_OFF, 1, bassNotes[i][j], 
									110 - (bassNotes[i].length * 12),
									8 * barCount + i * 2 + 2));

			}
		}
	}

	private void makeBar() {
		int tempNote =0;
		for (int i = 0; i < 8; i++) {
			Random random = new Random();
			int x = random.nextInt(listOfNotes.size());
			int note = listOfNotes.get(x);

			if (note != 0 && note != tempNote) {
				track.add(makeEvent(NOTE_ON, 1, note, 100, 8 * barCount + i));
				track.add(makeEvent(NOTE_OFF, 1, note, 100, 8 * barCount + i + 2));
			}
			tempNote = note;
		}
	}

	public static MidiEvent makeEvent(int cmd, int chnl, int one, int two, int tick) {
		MidiEvent event = null;
		try {
			ShortMessage msg = new ShortMessage();
			msg.setMessage(cmd, chnl, one, two);
			event = new MidiEvent(msg, tick);
		} catch (Exception e) {
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
