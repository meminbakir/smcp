/**
 * 
 */
package mchecking.translator.mct;

import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.KineticLaw;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.LocalParameter;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.Species;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mce.Inputs;
import mchecking.ModelChecker;
import mchecking.translator.mct.prismt.PRISMT;

/**
 * Scale the constants if they are not in bound of Integer. If the initial
 * amounts of species (non-constants) are decimal, it scale to integer value as
 * long as the rescaling conserves the integer bounds.
 * 
 * @author Mehmet Emin BAKIR
 *
 */
public class Scale {
	private static final Logger log = LoggerFactory.getLogger(Scale.class);

	// Scale the non-constant species if they are decimal
	private String scaleSpeciesMessage;
	boolean isSpeciesNeedsScaling = false;
	boolean isSpeciesScalable = true;

	private ModelChecker targetMC;
	private Inputs input;
	// // Scale the constant species if they are not within Integer bounds.
	// private String scaleConstantsMessage;
	// private boolean isConstantsScalable = true;
	// The min and max base values for rescaling constant species.
	double min = Integer.MAX_VALUE;
	double max = Integer.MIN_VALUE;
	boolean isConstantsWithinBounds = true;

	/**
	 * @param input
	 * @param targetMC
	 */
	public Scale(Inputs input, ModelChecker targetMC) {
		this.input = input;
		this.targetMC = targetMC;
	}

	public Scale(Inputs input) {
		this.input = input;
	}

	/**
	 * If the initial amount of non-constant species are in decimal format, then
	 * they needs to be scaled to integer values.
	 * 
	 * @param sbmlModel
	 * @return
	 * 
	 */
	public boolean isSpeciesScalable(Model sbmlModel) {

		Double coeff = getCoefficient(sbmlModel);
		// if after getting the coefficient it is still scalable
		if (isSpeciesNeedsScaling && isSpeciesScalable) {
			log.warn("The model includes species which have decimal initial amount/concentration.\n"
					+ "However, model checker requires the intial value of species be an integer number.\n"
					+ "Therefore, the initial amount of all species will be scaled to integer value.");

			// scale decimals
			isScaledSpeciesRemainWithinBounds(sbmlModel, coeff);
		}
		return isSpeciesScalable;
	}

	/**
	 * Check if after scaling the new value will be within the integer value bounds.
	 * 
	 * @param sbmlModel
	 * @param coeff
	 */
	private void isScaledSpeciesRemainWithinBounds(Model sbmlModel, Double coeff) {
		for (int n = 0; n < sbmlModel.getNumSpecies(); n++) {
			Species species = sbmlModel.getSpecies(n);
			// if species is not constant
			// if (!PRISMT.isSpeciesConstant(species)) {
			String speciesID = species.getId();
			// initial value can be either initialAmount or initialConcentration (exclusive)
			double initialValue = PRISMT.getSpeciesInitialValue(species);
			double scaled = initialValue * coeff;
			// if scaled value is not within the bounds then it is not possible to scale
			// a valid value for Model checkers
			if (Math.abs(scaled) <= getUpperBound()) {
				// it is scalable.
				continue;
			} else {
				String message = "Species '" + speciesID + "' has initial amount '" + initialValue + "'. "
						+ "After re-scaling the inital values of the species to be integer, its value became '" + scaled
						+ "'. " + "However, the new (re-scaled) value is not within the upper bound '"
						+ ((int) getUpperBound()) + "'. "
						+ "Please, adjust the lower and upper bounds with '-lB' and '-uB' the command options.";
				log.error(message);
				setScalableMessage(message);
				isSpeciesScalable = false;
				break;
			}
			// }
		}
	}

	/**
	 * @return
	 */
	private int getUpperBound() {
		return Integer.valueOf(input.getUpperBound());// Integer.MAX_VALUE;
	}

	public void scaleSpeciesInitialValue(Model sbmlModel) {
		Double coeff = getCoefficient(sbmlModel);
		if (isSpeciesNeedsScaling && isSpeciesScalable) {
			log.warn("The model includes species which have decimal initial amount/concentration.\n"
					+ "However, model checker requires the initial value of species be an integer number.\n"
					+ "Therefore, the initial amount of all species will be scaled to integer value.");

			// scale decimals
			scaleSpeciesInitialValue(sbmlModel, coeff);
		}

	}

	/**
	 * Check the initial amount will stay within the Integer bounds, then rescale
	 * it.
	 * 
	 * @param sbmlModel
	 * @param coeff
	 * @return
	 */
	private void scaleSpeciesInitialValue(Model sbmlModel, Double coeff) {
		for (int n = 0; n < sbmlModel.getNumSpecies(); n++) {
			Species species = sbmlModel.getSpecies(n);
			String speciesID = species.getId();
			// if (!PRISMT.isSpeciesConstant(species)) {
			// initial value can be either initialAmount or initialConcentration (exclusive)
			double initialValue = PRISMT.getSpeciesInitialValue(species);
			double scaledValue = initialValue * coeff;
			if (isWithinTheBounds(scaledValue)) {
				setSpeciesInitialValue(species, scaledValue);
				log.debug("Species:{} initial amount {} scaled to {}", speciesID, initialValue, scaledValue);
			} else {
				log.error("The model contains species {} before rescaling it has initial amount {}. "
						+ "After rescaling to integer the initial value becomes {}. "
						+ "However, the rescaled value is not within the integer bounds. "
						+ "Therefore, model checker's do not support it. "
						+ "Please, revise the initial amount of species.");

				break;
			}
			// }
		}
	}

	/**
	 * Check if any initial amount is in decimal, if so find the max coefficient so
	 * that all species initial amount can be rescaled to remain within upper and
	 * lower bounds
	 * 
	 * @param sbmlModel
	 * @return
	 */
	private Double getCoefficient(Model sbmlModel) {
		Species species = null;
		Double coeff = 0.0;
		for (int n = 0; n < sbmlModel.getNumSpecies(); n++) {
			species = sbmlModel.getSpecies(n);
			String speciesID = species.getId();
			double initialValue = PRISMT.getSpeciesInitialValue(species);
			if (!Double.isNaN(initialValue)) {
				// if it is decimal
				if (!isInteger(initialValue)) {
					// there is a decimal number, so species will be scaled to integer value
					isSpeciesNeedsScaling = true;

					Double tempCoeff = getCoefficient(initialValue);
					coeff = Math.max(coeff, tempCoeff);
				}
				if (!isWithinTheBounds(initialValue)) {
					String message = "Species " + speciesID
							+ " has initial amount "+initialValue+" which is not within the upper bound '" + ((int) getUpperBound())
							+ "'.\n" + "Please, adjust the lower and upper bounds with '-lB' and '-uB' the command options.";
					log.error(message);
					setScalableMessage(message);
					isSpeciesScalable = false;
					break;
				}
			} else {
				String message = "The initial amount/concentration of species " + speciesID + " is not defined. "
						+ "Model checker requires all species to have initial value.";
				isSpeciesScalable = false;
				setScalableMessage(message);// TODO: do we need this.
				log.error(message);
			}
		}
		return coeff;
	}

	/**
	 * Get the coefficient number which multiplication with initialAmount will
	 * convert it to Integer.
	 * 
	 * @param initialAmount
	 */
	Double getCoefficient(double initialAmount) {
		int i = 0;
		Double coeff = 0.0;
		do {
			if (isInteger(initialAmount * Math.pow(10, i))) {
				coeff = Math.pow(10, i);
				break;
			}
			i++;

		} while (Math.abs(coeff) <= getUpperBound());
		return coeff;
	}

	/**
	 * @param d
	 * @return
	 */
	private boolean isInteger(double d) {
		return d % 1 == 0;
	}

	/**
	 * @return the scalableMessage
	 */
	public String getScalableMessage() {
		return scaleSpeciesMessage;
	}

	/**
	 * @param scalableMessage
	 *            the scalableMessage to set
	 */
	public void setScalableMessage(String scalableMessage) {
		scaleSpeciesMessage = scalableMessage;
	}

	/**
	 * Check whether constants values are within the integer bounds. <br/>
	 * If not it, then it will scale it to fit the integer bounds.<br/>
	 * The new value does not have to be integer, but should remain within Integer
	 * bounds.
	 * 
	 * @param sbmlModel
	 */
	public void scaleConstatsRemainWithinIntegerBounds(Model sbmlModel) {
		if (!isConstantsWithinIntegerBounds(sbmlModel)) {
			// scale it.
			log.debug("Contants need so be scaled to remain in the Integer bounds.");
			scaleConstants(sbmlModel);
		}
	}

	/**
	 * Traverse constants and update their value to scaled value.
	 * 
	 * @param sbmlModel
	 * @param min
	 * @param max
	 */
	private void scaleConstants(Model sbmlModel) {
		// scale compartment(s)
		long numCompartments = sbmlModel.getNumCompartments();
		for (int i = 0; i < numCompartments; i++) {
			Compartment comp = sbmlModel.getCompartment(i);
			String constName = comp.getId();
			double size = comp.getSize();
			double scaledValue = scaleConstant2IntBounds(constName, size);
			comp.setSize(scaledValue);
		}
		// Parameters which are declared in compartment level not in reaction level
		ListOf<Parameter> listOfParameters1 = sbmlModel.getListOfParameters();
		for (int i = 0; i < listOfParameters1.size(); i++) {
			Parameter parameter = listOfParameters1.get(i);
			String constName = parameter.getId();
			double value = parameter.getValue();
			double scaledValue = scaleConstant2IntBounds(constName, value);
			parameter.setValue(scaledValue);
		}
		// Add constant true, and/or boundaryConditions true, species
		long numSpecies = sbmlModel.getNumSpecies();
		if (numSpecies != 0) {
			Species species = null;
			for (int n = 0; n < numSpecies; n++) {
				species = sbmlModel.getSpecies(n);
				// if species if constant || or boundary condition is true
				if (PRISMT.isSpeciesConstant(species)) {
					String constName = species.getId();
					double value = PRISMT.getSpeciesInitialValue(species);
					double scaledValue = scaleConstant2IntBounds(constName, value);
					setSpeciesInitialValue(species, scaledValue);
				}
			}
		}

		// Add reaction level local parameters as constant
		String constName;
		double value;
		for (int n = 0; n < sbmlModel.getNumReactions(); n++) {
			Reaction reaction = sbmlModel.getReaction(n);
			if (reaction.isSetKineticLaw()) {
				KineticLaw kineticLaw = reaction.getKineticLaw();
				ListOf<LocalParameter> listOfParameters2 = kineticLaw.getListOfLocalParameters();
				for (int i = 0; i < listOfParameters2.size(); i++) {
					LocalParameter parameter = listOfParameters2.get(i);
					if (reaction.getReversible()) {
						// forward rate
						if (i == 0) {
							constName = parameter.getId();
							value = parameter.getValue();
							double scaledValue = scaleConstant2IntBounds(constName, value);
							parameter.setValue(scaledValue);
						}
						// backward rate
						else if (i == 1) {
							constName = parameter.getId();
							value = parameter.getValue();
							double scaledValue = scaleConstant2IntBounds(constName, value);
							parameter.setValue(scaledValue);
						}
					}
					// one-directional rate
					else {
						constName = parameter.getId();
						value = parameter.getValue();
						double scaledValue = scaleConstant2IntBounds(constName, value);
						parameter.setValue(scaledValue);
					}
				}
			}
		}
	}

	/**
	 * Check if constants values are within the integer bounds. If not it will give
	 * warning.
	 * 
	 * @param sbmlModel
	 * @return true, if all constants are within the integer bounds.
	 */
	private boolean isConstantsWithinIntegerBounds(Model sbmlModel) {
		// Compartment name as a constant
		long numCompartments = sbmlModel.getNumCompartments();
		for (int i = 0; i < numCompartments; i++) {
			Compartment comp = sbmlModel.getCompartment(i);
			String constName = comp.getId();
			double size = comp.getSize();
			updateMinMaxAndCheckBounds(constName, size);
		}
		// Parameters which are declared in compartment level not in reaction level
		ListOf<Parameter> listOfParameters1 = sbmlModel.getListOfParameters();
		for (int i = 0; i < listOfParameters1.size(); i++) {
			Parameter parameter = listOfParameters1.get(i);
			String constName = parameter.getId();
			double value = parameter.getValue();
			updateMinMaxAndCheckBounds(constName, value);
		}
		// Add constant true, and/or boundaryConditions true, species
		long numSpecies = sbmlModel.getNumSpecies();
		if (numSpecies == 0) {
			log.warn("No species are defined");
		} else {
			Species species = null;
			for (int n = 0; n < numSpecies; n++) {
				species = sbmlModel.getSpecies(n);
				// if species if constant || or boundary condition is true
				if (PRISMT.isSpeciesConstant(species)) {
					String constName = species.getId();
					double value = PRISMT.getSpeciesInitialValue(species);
					updateMinMaxAndCheckBounds(constName, value);
				}
			}

		}
		// Add reaction level local parameters as constant
		String constName;
		double value;
		for (int n = 0; n < sbmlModel.getNumReactions(); n++) {
			Reaction reaction = sbmlModel.getReaction(n);
			if (reaction.isSetKineticLaw()) {
				KineticLaw kineticLaw = reaction.getKineticLaw();
				ListOf<LocalParameter> listOfParameters2 = kineticLaw.getListOfLocalParameters();
				for (int i = 0; i < listOfParameters2.size(); i++) {
					LocalParameter parameter = listOfParameters2.get(i);
					if (reaction.getReversible()) {
						// forward rate
						if (i == 0) {
							constName = parameter.getId();
							value = parameter.getValue();
							updateMinMaxAndCheckBounds(constName, value);
						}
						// backward rate
						else if (i == 1) {
							constName = parameter.getId();
							value = parameter.getValue();
							updateMinMaxAndCheckBounds(constName, value);
						}
					}
					// one-directional rate
					else {
						constName = parameter.getId();
						value = parameter.getValue();
						updateMinMaxAndCheckBounds(constName, value);
					}
				}
			}
		}

		return isConstantsWithinBounds;
	}

	/**
	 * @param species
	 * @param value
	 */
	private void setSpeciesInitialValue(Species species, double scaledValue) {
		if (!Double.isNaN(species.getInitialConcentration())) {
			species.setInitialConcentration(scaledValue);
		} else if (!Double.isNaN(species.getInitialAmount())) {
			species.setInitialAmount(scaledValue);
		}
	}

	/**
	 * @param needsScaling
	 * @param min
	 * @param max
	 * @param constName
	 * @param value
	 */
	private void updateMinMaxAndCheckBounds(String constName, double value) {
		updateMinMax(value);
		if (!isWithinTheBounds(value)) {
			isConstantsWithinBounds = false;
			String message = notInIntegerBoundsMessage(constName, value);
			log.warn(message);
		}
	}

	/**
	 * The message for warning the constant is larger or smaller than the integer
	 * bound
	 * 
	 * @param constName
	 * @param size
	 * @return
	 */
	private String notInIntegerBoundsMessage(String constName, double size) {
		return constName + " has " + size + " which is not withint the Integer bounds [" + Integer.MIN_VALUE + ", "
				+ getUpperBound() + "]. Therefore, it will be scaled to stay within the limits.";
	}

	/**
	 * @param value
	 * @return
	 */
	private boolean isWithinTheBounds(double value) {
		return (Math.abs(value) <= getUpperBound());
	}

	/**
	 * Update the min and max value of constant species
	 * 
	 * @param min
	 * @param max
	 * @param value
	 */
	private void updateMinMax(double value) {
		min = Double.valueOf(Math.min(min, value));
		max = Double.valueOf(Math.max(max, value));
	}

	/**
	 * Scale the value to remain within the Integer bounds, result does not have to
	 * be an integer.
	 * 
	 * @param constName
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	private double scaleConstant2IntBounds(String constName, Double value) {
		double scaledValue = scaleIntBounds(value, min, max);
		log.debug("{} constant scaled to new value {}", constName, scaledValue);
		return scaledValue;
	}

	private double scaleIntBounds(Double value, Double min, Double max) {
		double limitMin = getLowerBound();
		double limitMax = getUpperBound();

		// Set the bound min(limitMin) to min if min is within integer limit
		if (min > limitMin) {
			if (min <= 0) {
				limitMin = min;
			} else if (min > 0) {
				limitMin = 0;
			}
		}
		// if max is less than Integer.Max then we can set the max as the upper limit
		if (max < limitMax) {
			limitMax = max;
		}

		return scale(value, min, max, limitMin, limitMax);
	}

	private double getLowerBound() {
		return Double.parseDouble(input.getLowerBound());
	}

	/**
	 * <pre>
	 *       (limitMax-limitMin)*(valueIn - baseMin)
	 *f(x) = ---------------------------------------  + limitMin
	 *                 baseMax - baseMin
	 * </pre>
	 * 
	 * @param valueIn
	 * @param baseMin
	 * @param baseMax
	 * @param limitMin
	 *            integer min
	 * @param limitMax
	 *            integer max
	 * @return
	 */
	public static double scale(final double valueIn, final double baseMin, final double baseMax, final double limitMin,
			final double limitMax) {
		return ((limitMax - limitMin) * (valueIn - baseMin) / (baseMax - baseMin)) + limitMin;
	}

}
