/**    
 * @author Mehmet Emin BAKIR
 */
package mchecking.translator.mct.prismt.beans;

/**
 * 
 * <update.name>' = (<update.name> <update.operator> <update.stoichiometry>) (_E'=_E - 1) & (_S'= _S - 1) & (ES' = ES + 1)
 * 
 * 
 */
public class Next extends Molecule {
	public enum Comparer {
		/**
		 * Greater
		 */
		GR(">"),
		GEQ(">="),
		LEQ("<="),
		// Not applicable if occurs species both sides of reaction and difference is zero
		NA(""),
		EQ("=");
		String value;

		Comparer(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return this.value;
		}
	}

	public enum Operator {
		MINUS("-"),
		SUM("+"),
		// Not applicable if occurs species both sides of reaction and difference is zero
		NA("");
		String value;

		Operator(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return this.value;
		}

	}

	/**
	 * Either plus(increasing) or minus(decreasing) operator
	 */
	public enum Type {
		PRODUCT,
		REACTANT,
		BOTHSIDES;
	}

	Comparer comparer = null;

	Operator operator = null;

	String stoichiometry = null;

	Type type = null;
	String bound = "";

	public Next() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 * @param oper
	 * @param stoichiometry
	 */
	public Next(String name, String stoichiometry) {
		super();
		this.name = name;
		this.stoichiometry = stoichiometry;
	}

	/**
	 * @return the comparer
	 */
	public Comparer getComparer() {
		return comparer;
	}

	/**
	 * @return the operator
	 */
	public Operator getOperator() {
		return operator;
	}

	/**
	 * @return the bound
	 */
	public String getBound() {
		return bound;
	}

	/**
	 * @param bound
	 *            the bound to set
	 */
	public void setBound(String bound) {
		this.bound = bound;
	}

	/**
	 * @return the stoichiometry
	 */
	public String getStoichiometry() {
		return stoichiometry;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param comparer
	 *            the comparer to set
	 */
	public void setComparer(Comparer comparer) {
		this.comparer = comparer;
	}

	/**
	 * @param operator
	 *            the operator to set
	 */
	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	/**
	 * @param stoichiometry
	 *            the stoichiometry to set
	 */
	public void setStoichiometry(String stoichiometry) {
		this.stoichiometry = stoichiometry;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public String toString() {
		String result = "";
		result += "Name " + this.name + " Operator " + this.operator + " Comparer " + this.comparer + "\n"
				+ "Type " + this.type + " Bound " + this.bound + " Stoichiometry " + this.stoichiometry + "\n";
		return result;
	}
}
