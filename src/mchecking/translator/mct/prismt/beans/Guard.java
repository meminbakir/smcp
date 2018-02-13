package mchecking.translator.mct.prismt.beans;

/**
 * @author Mehmet Emin BAKIR
 * 
 * (<guard.left>  >= <guard.right>)
 * (_E-1 >=lowBound) & (_S-1 >=lowBound) & (upBound >= ES+1)
 */
public class Guard {
	String left = null;
	String right = null;

	/**
	 * 
	 */
	public Guard() {
	}

	/**
	 * If it is a reactant, then left is reactant - stoichiometry >= lowerBound
	 * if it is a product left side will be upperBound >= product + stoichiometry
	 * @param left
	 * @param right
	 */
	public Guard(String left, String right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * @return the left
	 */
	public String getLeft() {
		return left;
	}

	/**
	 * @param left
	 *            the left to set
	 */
	public void setLeft(String left) {
		this.left = left;
	}

	/**
	 * @return the right
	 */
	public String getRight() {
		return right;
	}

	/**
	 * @param right
	 *            the right to set
	 */
	public void setRight(String right) {
		this.right = right;
	}

}
