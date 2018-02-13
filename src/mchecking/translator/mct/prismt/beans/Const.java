/**
 * 
 */
package mchecking.translator.mct.prismt.beans;

import mchecking.translator.TranslatorUtil;

/**
 * @author Mehmet
 *
 */
public class Const {
	String type = null;
	String name = null;
	String value = null;

	/**
	 * @param type
	 * @param name
	 * @param value
	 */
	public Const(String type, String name, String value) {
		setType(type);
		setName(name);
		setValue(value);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = TranslatorUtil.checkKeyword(name);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
