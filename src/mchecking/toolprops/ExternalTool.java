/**
 * 
 */
package mchecking.toolprops;

import mchecking.enums.ExtTTypes;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class ExternalTool {
	private ExtTTypes extTType = null;
	private String externalToolPath = null;
	private String modelExtension = null;

	/**
	 * Returns the enum ExtTTypes
	 * 
	 * @return the extTType
	 */
	public ExtTTypes getExtTType() {
		return this.extTType;
	}

	/**
	 * Creates enum counterpart of @param extTType and assigns to extTType field.
	 * 
	 * @param extTType
	 */
	public void setExtTType(String extTType) {
		setExtTType(ExtTTypes.valueOf(extTType));
	}

	/**
	 * Return the type, i.e., name, of the external tool
	 * 
	 * @param extTType
	 *            the extTType to set
	 */
	public void setExtTType(ExtTTypes extTType) {
		this.extTType = extTType;
	}

	/**
	 * Return the path of the external tool, to be run
	 * 
	 * @return the externalToolPath
	 */
	public String getExternalToolPath() {
		return this.externalToolPath;
	}

	/**
	 * @param externalToolPath
	 *            the externalToolPath to set
	 */
	public void setExternalToolPath(String externalToolPath) {
		this.externalToolPath = externalToolPath;
	}

	/**
	 * Return the model extension required for the external tool
	 * 
	 * @return the modelExtension
	 */
	public String getModelExtension() {
		return this.modelExtension;
	}

	/**
	 * @param modelExtension
	 *            the modelExtension to set
	 */
	public void setModelExtension(String modelExtension) {
		this.modelExtension = modelExtension;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "";
		result += this.extTType + "\n"+ this.modelExtension + "\n" + this.externalToolPath + "\n";
		return result;
	}
}
