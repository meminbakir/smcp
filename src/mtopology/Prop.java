/**
 * 
 */
package mtopology;

import mtopology.enums.IProperties;

/**
 * It holds properties, and if enabled, their results, i.e, value and elapsed time.
 * 
 * @author Mehmet Emin BAKIR
 *
 */
public class Prop {
	IProperties prop;
	String value = "0";
	boolean enabled = false;
	long elapsedTime = 0; // how long it takes to produce the value

	Prop(IProperties prop, boolean enabled) {
		this.prop = prop;
		this.enabled = enabled;
	}

	/**
	 * 
	 * @return
	 */

	public IProperties getProp() {
		return prop;
	}

	/**
	 * 
	 * @param prop
	 */
	public void setProp(IProperties prop) {
		this.prop = prop;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the elapsedTime
	 */
	public long getElapsedTime() {
		return elapsedTime;
	}

	/**
	 * @param elapsedTime
	 *            the elapsedTime to set
	 */
	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Prop))
			return false;
		if (obj == this)
			return true;
		return ((this.prop.equals(((Prop) obj).prop)));
	}

	@Override
	public int hashCode() {
		return prop.toString().length();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (enabled ? "Enabled " : "Disabled ") + prop + ":" + value + "(Elapsed time:" + elapsedTime + ")";
	}
}