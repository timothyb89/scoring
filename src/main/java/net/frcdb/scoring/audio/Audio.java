package net.frcdb.scoring.audio;

import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tim
 */
@Slf4j
public class Audio {

	private static Audio instance;
	
	private Audio() {
		
	}

	private static void preinit() {
		if (instance == null) {
			instance = new Audio();
		}
	}

	public static Audio getInstance() {
		preinit();

		return instance;
	}

	public void _play(File file) {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(file));
			clip.start();
		} catch (Exception ex) {
			log.error("Failed to play audio file " + file + ": "
						+ ex.getMessage(), ex);
		}
	}

	public static void play(File file) {
		getInstance()._play(file);
	}

}
