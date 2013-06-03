package net.frcdb.scoring;

import net.frcdb.scoring.game.GameLoader;
import net.frcdb.scoring.game.GamePeriod;
import net.frcdb.scoring.game.GamePeriods;
import net.frcdb.scoring.match.MatchManager;

/**
 *
 * @author tim
 */
public class Timer {

	private static Timer instance;

	private GamePeriods periodsModel;

	public int timeLeft;
	private GamePeriod period;
	private boolean running = false;
	private boolean paused = false;

	private Timer() {
		periodsModel = GameLoader.get().getGame().getPeriods();
	}

	public static Timer get() {
		if (instance == null) {
			instance = new Timer();
		}

		return instance;
	}

	public int getTimeLeft() {
		return timeLeft;
	}

	public String timeToString() {
		String mins = String.valueOf(timeLeft / 60);
		
		String secs = String.valueOf(timeLeft % 60);
		if (secs.length() == 1) {
			secs = "0" + secs;
		}

		if (paused) {
			return "<html>"
					+ "Match <span style=\"color: blue\">P</span>"
					+ "<span style=\"font-size: 70%; color: blue\">AUSED</span> - "
					+ mins + ":" + secs;
		} else if (timeLeft < -1) {
			return "<html>"
					+ "Match <span style=\"color: red\">C</span>"
					+ "<span style=\"color: red; font-size: 70%;\">ANCELED</span>";
		}

		return mins + ":" + secs;
	}

	private GamePeriod getCurrentPeriod() {
		GamePeriod latest = null;

		for (GamePeriod p : periodsModel.getOrderedPeriods()) {
			if (p.getStart() == timeLeft) {
				return p;
			} else if (p.getStart() >= timeLeft) {
				if (latest == null) {
					latest = p;
				} else {
					if (p.getStart() < latest.getStart()) {
						latest = p;
					}
				}
			}
		}

		return latest;
	}

	public GamePeriod getPeriod() {
		return period;
	}

	public void setPeriod(GamePeriod period) {
		this.period = period;

		MatchManager.get().notifyPeriodChanged(period);
	}

	public void start() {
		timeLeft = periodsModel.getLength();
		new Thread(timer).start();
	}
	
	public boolean isPaused() {
		return paused;
	}
	
	public void pause() {
		paused = true;
	}
	
	public void resume() {
		paused = false;
	}

	private void update() {
		GamePeriod current = getCurrentPeriod();
		if (period != current) {
			setPeriod(current);
		}

		MatchManager.get().notifyTimerUpdated();
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public void stop() {
		setRunning(false);

		timeLeft = -1;
		update();
	}

	public void reset() {
		running = false;
		paused = false;

		timeLeft = periodsModel.getLength();
		//update();
	}

	private Runnable timer = new Runnable() {

		public void run() {
			update();
			running = true;

			while (running && timeLeft > 0) {
				try {
					Thread.sleep(1000);
				} catch (Exception ex) {
					// do nothing.
				}

				if (!paused) {
					timeLeft--;
				}
				
				update();
			}
		}

	};

}
