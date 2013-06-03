package net.frcdb.scoring.game;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 *
 * @author tim
 */
@XStreamAlias("scheduler")
public class SchedulerConfiguration {
	
	public static final double DEFAULT_FIELD_TEMPERATURE = 100;
	public static final double DEFAULT_FIELD_TSTEP = .99;
	public static final double DEFAULT_FIELD_ANNEALTIME = 10;
	public static final double DEFAULT_FIELD_LOWESTTEMP = .001;
	
	public static final double DEFAULT_PAIRING_TEMPERATURE = 100;
	public static final double DEFAULT_PAIRING_TSTEP = .9;
	public static final double DEFAULT_PAIRING_ANNEALTIME = 10;
	public static final double DEFAULT_PAIRING_LOWESTTEMP = .001;
	
	@XStreamAsAttribute
	private int rounds;
	
	@XStreamAsAttribute
	private int allianceSize;
	
	private FieldConfiguration fieldConstants;
	private PairingConfiguration pairingConstants;

	public SchedulerConfiguration() {
		fieldConstants = new FieldConfiguration();
		pairingConstants = new PairingConfiguration();
	}

	public SchedulerConfiguration(int rounds, int allianceSize) {
		this.rounds = rounds;
		this.allianceSize = allianceSize;
		
		fieldConstants = new FieldConfiguration();
		pairingConstants = new PairingConfiguration();
	}

	public int getRounds() {
		return rounds;
	}

	public void setRounds(int rounds) {
		this.rounds = rounds;
	}

	public int getAllianceSize() {
		return allianceSize;
	}

	public void setAllianceSize(int allianceSize) {
		this.allianceSize = allianceSize;
	}

	public FieldConfiguration getFieldConstants() {
		return fieldConstants;
	}

	public PairingConfiguration getPairingConstants() {
		return pairingConstants;
	}
	
	public static class FieldConfiguration {
		
		@XStreamAsAttribute
		private double temperature = DEFAULT_FIELD_TEMPERATURE;
		
		@XStreamAsAttribute
		private double tStep = DEFAULT_FIELD_TSTEP;
		
		@XStreamAsAttribute
		private double annealTime = DEFAULT_FIELD_ANNEALTIME;
		
		@XStreamAsAttribute
		private double lowestTemp = DEFAULT_FIELD_LOWESTTEMP;

		public double getTemperature() {
			return temperature;
		}

		public void setTemperature(double temperature) {
			this.temperature = temperature;
		}

		public double getTStep() {
			return tStep;
		}

		public void setTStep(double tStep) {
			this.tStep = tStep;
		}

		public double getAnnealTime() {
			return annealTime;
		}

		public void setAnnealTime(double annealTime) {
			this.annealTime = annealTime;
		}

		public double getLowestTemp() {
			return lowestTemp;
		}

		public void setLowestTemp(double lowestTemp) {
			this.lowestTemp = lowestTemp;
		}
		
	}
	
	public static class PairingConfiguration {
		
		@XStreamAsAttribute
		private double temperature = DEFAULT_PAIRING_TEMPERATURE;
		
		@XStreamAsAttribute
		private double tStep = DEFAULT_PAIRING_TSTEP;
		
		@XStreamAsAttribute
		private double annealTime = DEFAULT_PAIRING_ANNEALTIME;
		
		@XStreamAsAttribute
		private double lowestTemp = DEFAULT_PAIRING_LOWESTTEMP;
		
		public double getTemperature() {
			return temperature;
		}

		public void setTemperature(double temperature) {
			this.temperature = temperature;
		}

		public double getTStep() {
			return tStep;
		}

		public void setTStep(double tStep) {
			this.tStep = tStep;
		}

		public double getAnnealTime() {
			return annealTime;
		}

		public void setAnnealTime(double annealTime) {
			this.annealTime = annealTime;
		}

		public double getLowestTemp() {
			return lowestTemp;
		}

		public void setLowestTemp(double lowestTemp) {
			this.lowestTemp = lowestTemp;
		}
		
	}
	
}
