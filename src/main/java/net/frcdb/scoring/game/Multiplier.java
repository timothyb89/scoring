package net.frcdb.scoring.game;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 *
 * @author tim
 */
@XStreamAlias("multiplier")
public class Multiplier {

	public static final String DEFAULT_TYPE = "normal";
	public static final int DEFAULT_MULTIPLIER = 1;
	
	@XStreamAsAttribute
	private String type;
	
	@XStreamAsAttribute
	private int amount;

	public Multiplier() {
		type = DEFAULT_TYPE;
		amount = DEFAULT_MULTIPLIER;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	/**
	 * Applies this multiplier to the specified number of points. Depending on
	 * the type, the result may vary between linear and exponential models.
	 * @param n the number of instances of the scoring parameter
	 * @return the multiplied value for the given parameters
	 */
	public int apply(int n) {
		return MultiplierType.get(type).apply(amount, n);
	}
	
	public static enum MultiplierType {
		
		LINEAR,
		NORMAL,
		
		EXPONENTIAL() {
			
			@Override
			public int apply(int amount, int n) {
				return (int) Math.pow(amount, n);
			}
			
		},
		
		EXPONENTIALZERO() {

			@Override
			public int apply(int amount, int n) {
				return n == 0 ? 0 : ((int) Math.pow(amount, n));
			}
			
		},
		
		DEFAULT;
		
		private MultiplierType() {
			
		}
		
		public int apply(int amount, int n) {
			return amount * n; // linear
		}
		
		public static MultiplierType get(String name) {
			for (MultiplierType t : values()) {
				if (t.name().equalsIgnoreCase(name)) {
					return t;
				}
			}
			
			return DEFAULT;
		}
		
	}
	
}
