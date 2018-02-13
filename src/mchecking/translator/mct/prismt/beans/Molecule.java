/**
 * 
 */
package mchecking.translator.mct.prismt.beans;

import mchecking.translator.TranslatorUtil;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class Molecule {
	String name = null;
	String lowerBound = null;
	String upperBound = null;
	String init = null;

	/**
	 * 
	 */
	public Molecule() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 * @param low
	 * @param up
	 * @param init
	 */
	public Molecule(String name, String low, String up, String init) {
		super();
		this.name = name;
		this.lowerBound = low;
		this.upperBound = up;
		this.init = init;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = TranslatorUtil.checkKeyword(name);
	}

	/**
	 * @return the low
	 */
	public String getLowerBound() {
		return lowerBound;
	}

	/**
	 * @param low
	 *            the low to set
	 */
	public void setLowerBound(String low) {
		this.lowerBound = low;
	}

	/**
	 * @return the up
	 */
	public String getUpperBound() {
		return upperBound;
	}

	/**
	 * @param up
	 *            the up to set
	 */
	public void setUpperBound(String up) {
		this.upperBound = up;
	}

	/**
	 * @return the init
	 */
	public String getInit() {
		return init;
	}

	/**
	 * @param init
	 *            the init to set
	 */
	public void setInit(String init) {
		this.init = init;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result="";
		result+= this.name + ": [" + this.lowerBound +" .. "+ this.upperBound +"] init "+ this.init;
		return result;
	}
}
