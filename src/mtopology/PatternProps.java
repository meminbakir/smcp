package mtopology;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mce.util.Utils;
import mchecking.enums.Pattern;
import mtopology.enums.GraphP;
import mtopology.enums.IProperties;
import mtopology.enums.NonGraphProperties;

/**
 * Assigns related graph properties to each pattern types.
 * 
 * @author Mehmet Emin BAKIR
 *
 */
public class PatternProps {
	private static final Logger log = LoggerFactory.getLogger(PatternProps.class);
	Pattern pattern = null;
	private String prefix = "";// The prefix to be added before the name of
								// property, e.g.
								// species_pesudo_directed_graph...
	Set<Prop> props = new LinkedHashSet<Prop>();

	public PatternProps() {
		this.loadAllGraphProperties();
		this.disableAllProps();
	}

	/**
	 * Disable all props except the properties provided by @param enableByName
	 * 
	 * @param enableByName,
	 *            String name of properties
	 */
	public PatternProps(String[] enableByName) {
		this.loadAllGraphProperties();
		this.disableAllProps();
		this.enableByName(enableByName);
	}

	public PatternProps(Pattern pattern) {
		this.pattern = pattern;
		// load common properties, if exist
		loadAllGraphProperties();
		switch (this.pattern) {
		case EVENTUALLY:
			loadEventuallyProps();
			break;
		case ALWAYS:
			loadAlwaysProps();
			break;
		// TODO add remained patterns
		default:
			break;
		}
	}

	/**
	 * Checks if given property is enabled for current pattern
	 * 
	 * @param prop
	 * @return
	 */
	public boolean isEnabled(IProperties prop) {
		boolean isEnabled = false;
		for (Prop p : props) {
			if (p.getProp().equals(prop)) {
				isEnabled = p.enabled;
				break;
			}
		}
		return isEnabled;
	}

	/**
	 * Checks if any of the properties has been enabled
	 * 
	 * @param prop
	 *            properties check if any enabled
	 */
	public boolean isAnyEnabled(IProperties[] paramProps) {
		for (IProperties paramP : paramProps) {
			for (Prop p : props) {
				if (p.getProp().equals(paramP)) {
					// if any one has been enabled then return true
					if (p.isEnabled())
						return true;
					else
						break;
				}
			}
		}
		return false;
	}

	/**
	 * Checks if all properties are enabled
	 * 
	 * @param prop
	 *            properties to be enabled
	 */
	public boolean areAllEnabled(IProperties[] paramProps) {
		for (IProperties paramP : paramProps) {
			for (Prop p : props) {
				if (p.getProp().equals(paramP)) {
					// if any one is not enabled then return false
					if (!p.isEnabled())
						return false;
				}
			}
		}
		return true;
	}

	/**
	 * Enable a specific graph property
	 * 
	 * @param prop
	 *            property to be enabled
	 */
	public void enableProp(IProperties prop) {
		for (Prop p : props) {
			if (p.getProp().equals(prop)) {
				p.setEnabled(true);
				break;
			}
		}
	}

	/**
	 * Enable properties in the list
	 * 
	 * @param prop
	 *            properties to be enabled
	 */
	public void enableProps(List<IProperties> beEnabledProps) {
		for (IProperties prop : beEnabledProps) {
			enableProp(prop);
		}
	}

	/**
	 * Enable the graph properties by their string name
	 * 
	 * @param properties
	 */
	public void enableByName(String[] properties) {
		if (properties != null) {
			for (String property : properties) {
				boolean found = false;
				for (Prop p : props) {
					if (p.getProp().toString().equals(property)) {
						p.setEnabled(true);
						found = true;
						break;
					}
				}
				if (!found) {
					log.error(property + " could not be found in property list.");
					System.exit(1);
				}
			}
		}
	}

	/**
	 * Disable a specific graph property
	 * 
	 * @param prop
	 *            property to be disabled
	 */
	public void disableProp(IProperties prop) {
		for (Prop p : props) {
			if (p.getProp().equals(prop)) {
				p.setEnabled(false);
				break;
			}
		}
	}

	/**
	 * Disable properties in the list
	 * 
	 * @param prop
	 *            properties to be disabled
	 */
	public void disableProps(List<IProperties> beDisabledProps) {
		for (IProperties prop : beDisabledProps) {
			disableProp(prop);
		}
	}

	/**
	 * Disable all properties, it is useful before enabling a few properties.
	 */
	public void disableAllProps() {
		for (Prop p : props) {
			p.setEnabled(false);
		}
	}

	public void loadAllGraphProperties() {
		// add all properties for default
		for (GraphP prop : GraphP.values()) {
			props.add(new Prop(prop, true));
		}
		// we can disable here, if there are some unwanted properties
	}

	public void loadAllNonGraphProperties() {
		// add all properties for default
		for (NonGraphProperties prop : NonGraphProperties.values()) {
			props.add(new Prop(prop, true));
		}
		// we can disable here, if there are some unwanted properties
	}

	/**
	 * 
	 */
	private void loadAlwaysProps() {
		// the properties related to all vertices :P
	}

	private void loadEventuallyProps() {
		// e.g., disableProp(GProps.Vertices);
	}

	/**
	 * @param edgenumber
	 * @param value
	 */
	public void setPropValue(IProperties prop, String value) {
		for (Prop p : props) {
			if (p.getProp().equals(prop)) {
				p.setValue(value);
				break;
			}
		}
	}

	/**
	 * Set the time elapsed for generating value of given properties
	 * 
	 * @param edgenumber
	 * @param value
	 */
	public void setPropElapsedTime(IProperties prop, long time) {
		for (Prop p : props) {
			if (p.getProp().equals(prop)) {
				p.setElapsedTime(time);
				break;
			}
		}
	}

	/**
	 * Returns props list, including disabled
	 * 
	 * @return
	 */
	public Set<Prop> getProps() {
		return props;
	}

	/**
	 * @param edgenumber
	 * @return
	 */
	public Prop getProp(IProperties prop) {
		Prop result = null;
		for (Prop p : props) {
			if (p.getProp().equals(prop)) {
				result = p;
				break;
			}
		}
		if (result == null) {
			String error = "Propety " + prop + " not found in existing propert list";
			log.error(error);
			Utils.out(error);
		}
		return result;
	}

	/**
	 * Set consumed time and generated value of given property
	 * 
	 * @param prop
	 *            the property
	 * @param elapsedTime
	 *            consumed time for generating the property
	 * @param value
	 *            the property
	 */
	public void setTimeAndValue(IProperties prop, long elapsedTime, String value) {
		for (Prop p : props) {
			if (p.getProp().equals(prop)) {
				// double check if p is enabled then set the time
				if (p.isEnabled()) {
					p.setElapsedTime(elapsedTime);
					p.setValue(value);
					break;
				} else {
					log.error("The " + prop + " property has not been enabled, but you attempt to set time");
				}
			}
		}
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix
	 *            the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mtopology.Attr#toString()
	 */
	@Override
	public String toString() {
		String result = "";
		result += (pattern == null ? "" : pattern) + "\n";
		result += prefix + "\n";
		int count = 1;
		for (Prop prop : props) {
			result += count + ". " + prop + "\n";
			count++;
		}
		return result;
	}

	/**
	 * TODO: I used for quick test purpose can be removed.
	 * 
	 * @param string
	 */
	public void enablePropsStartWith(String string) {
		for (Prop p : props) {
			if (p.getProp().toString().startsWith(string)) {
				p.setEnabled(true);
			}
		}

	}

}