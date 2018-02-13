/**
 * 
 */
package mchecking.verify;

import java.io.IOException;

import mce.Inputs;
import mchecking.ModelChecker;
import mchecking.translator.qt.MCQuery;
import output.Output;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public abstract class Verify {
	protected Inputs input = null;
	protected Output output = null;
	protected ModelChecker targetMC = null;

	public Verify(Inputs input, ModelChecker targetMC) {
		this.input = input;
		this.targetMC = targetMC;
	}

	/**
	 * Verifies the given model with current model checker, fills results into Output object
	 * 
	 * @param input
	 * @param mcModelFilePath
	 * @param mcQuery
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public abstract Output verify(String mcModelFilePath, MCQuery mcQuery) throws IOException, InterruptedException;
}
