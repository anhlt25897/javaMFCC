package mfcc_tarsosDSP;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.mfcc.MFCC;

public class MFCCs {

	public static void main(String[] args) {

		List<float[]> mfccs = new ArrayList<>();

		AudioDispatcher dispatcher;
		try {
			dispatcher = AudioDispatcherFactory.fromFile(new File("1530074385995.wav"), 1024, 0);
			dispatcher.addAudioProcessor(new AudioProcessor() {
				@Override
				public void processingFinished() {
					System.out.println("Size : " + size);
					System.out.println("Count : " + mfccs.size());
					StringBuilder builder = new StringBuilder();
					for (int i = 0; i < mfccs.size(); i++) {
						float[] mfcc = mfccs.get(i);
						StringBuilder data = new StringBuilder();
						for (double cell : mfcc) {
							data.append(", ").append(cell);
						}
						builder.append(data.substring(2, data.length() - 2)).append("\r\n");
					}

					new MFCCs().saveFile(builder.toString());
				}

				@Override
				public boolean process(AudioEvent audioEvent) {
					size += audioEvent.getBufferSize();
					MFCC mfcc = new MFCC(1024, 44100, 39, 40, 300, 8000);
					mfcc.process(audioEvent);

					boolean isSuccess = mfcc.process(audioEvent);
					if (isSuccess) {
						mfccs.add(mfcc.getMFCC());
					}

					printArray("mfccs", mfcc.getMFCC());
					return true;
				}
			});
			dispatcher.run();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static long size = 0;

	void saveFile(String data) {
		try (PrintWriter out = new PrintWriter("mfcc.txt")) {
			out.println(data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void printArray(String name, float[] array) {
		for (float f : array) {
			System.out.print(f + ", ");
		}
		System.out.println("");
	}
}
