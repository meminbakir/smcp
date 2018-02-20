/**
 * 
 */
package mtopology;

import org.junit.BeforeClass;
//import org.sbml.libsbml.Model;
//import org.sbml.libsbml.SBMLDocument;
//import org.sbml.libsbml.SBMLReader;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;

import mce.MySBMLReader;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class GraphTests {

	static Model sbmlModel = null;
	static PatternProps patternProps = null;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		String path = "";
		String file = "";
		path = "models/sbml/test/unitTest/";
		file = "EnzymaticReaction4.xml";
		file = "13_BIOMD0000000267.xml.ssa.sbml";
		file = "EnzymaticReaction.sbml";
		file = "NaCl.sbml";
		file = "NaCl4.xml";
		file = "EnzymaticReaction4.xml";
		file = "77_BIOMD0000000317.xml.ssa.sbml";
		// file="C:\\Users\\Mehmet\\OneDrive\\git\\mce\\mce\\models\\sbml\\EnzymaticReaction3.xml";
		String sbmlFilePath = path + file;
		MySBMLReader reader = new MySBMLReader();
		SBMLDocument document = reader.readSBML(sbmlFilePath);
		Model sbmlModel1 = document.getModel();
		if (sbmlModel1 != null)
			sbmlModel = sbmlModel1;
		else {
			System.out.println("SBML model is null");
			exit();
		}
		// printTopologicalProps();
		// printNonGraphProps();

	}

	// private static void printTopologicalProps() {
	//
	// ITopology topology = null;
	//
	// topology = new SpeciesGraph(sbmlModel, Topologies.SIMPLE_UNDIRECTED);
	// patternProps = topology.calculateProps();
	// System.out.println(patternProps);
	//
	// topology = new SpeciesGraph(sbmlModel, Topologies.SIMPLE_DIRECTED);
	// patternProps = topology.calculateProps();
	// System.out.println(patternProps);
	//
	// topology = new ReactionGraph(sbmlModel, Topologies.SIMPLE_UNDIRECTED);
	// patternProps = topology.calculateProps();
	// System.out.println(patternProps);
	//
	// topology = new ReactionGraph(sbmlModel, Topologies.SIMPLE_DIRECTED);
	// patternProps = topology.calculateProps();
	// System.out.println(patternProps);
	//
	// topology = new SpeciesGraph(sbmlModel, Topologies.PSEUDO_UNDIRECTED);
	// patternProps = topology.calculateProps();
	// System.out.println(patternProps);
	//
	// topology = new SpeciesGraph(sbmlModel, Topologies.PSEUDO_DIRECTED);
	// patternProps = topology.calculateProps();
	// System.out.println(patternProps);
	//
	// topology = new ReactionGraph(sbmlModel, Topologies.PSEUDO_UNDIRECTED);
	// patternProps = topology.calculateProps();
	// System.out.println(patternProps);
	//
	// topology = new ReactionGraph(sbmlModel, Topologies.PSEUDO_DIRECTED);
	// patternProps = topology.calculateProps();
	// System.out.println(patternProps);
	//
	// topology = new NonGraph(sbmlModel);
	// patternProps = topology.calculateProps();
	// System.out.println(patternProps);
	// }

	// @Test
	// public void testReactionGraph() {
	// MGraph graph = new ReactionGraph(sbmlModel, Topologies.PSEUDO_DIRECTED);
	// new CalculateGraphProps(graph).calculateProps();
	// patternProps = graph.getPatternProps();
	//
	// GraphProperties prop = null;
	// prop = GraphProperties.Vertices;
	// assertEquals(Double.parseDouble(patternProps.getProp(prop).getValue()), 6, 0.01);
	// prop = GraphProperties.Edges;
	// assertEquals(Double.parseDouble(patternProps.getProp(prop).getValue()), 9, 0.01);
	// prop = GraphProperties.InDegreeMIN;
	// assertEquals(Double.parseDouble(patternProps.getProp(prop).getValue()), 1, 0.01);
	// prop = GraphProperties.InDegreeMAX;
	// assertEquals(Double.parseDouble(patternProps.getProp(prop).getValue()), 2, 0.01);
	// prop = GraphProperties.InDegreeMEAN;
	// assertEquals(Double.parseDouble(patternProps.getProp(prop).getValue()), 1.5, 0.01);
	// }
	//
	// @Test
	// public void testSpeciesGraph() {
	// MGraph graph = new SpeciesGraph(sbmlModel, Topologies.PSEUDO_DIRECTED);
	// new CalculateGraphProps(graph).calculateProps();
	// patternProps = graph.getPatternProps();
	//
	// GraphProperties prop = null;
	// // The vertices and Edges number can be different than ssapredict values, because it ignores generated and
	// degraded
	// // variables, but they effect performance so we included them. The value effects mean values as well
	// prop = GraphProperties.Vertices;
	// assertEquals(Double.parseDouble(patternProps.getProp(prop).getValue()), 9, 0.01);
	// prop = GraphProperties.Edges;
	// assertEquals(Double.parseDouble(patternProps.getProp(prop).getValue()), 6, 0.01);
	// prop = GraphProperties.InDegreeMIN;
	// assertEquals(Double.parseDouble(patternProps.getProp(prop).getValue()), 0, 0.01);
	// prop = GraphProperties.InDegreeMAX;
	// assertEquals(Double.parseDouble(patternProps.getProp(prop).getValue()), 1, 0.01);
	// prop = GraphProperties.InDegreeMEAN;
	// assertEquals(Double.parseDouble(patternProps.getProp(prop).getValue()), 0.666, 0.01);
	// }

	private static void exit() {
		System.err.println("The application exits.");
		System.exit(1);
	}

	/**
	 * Loads the SWIG-generated libSBML Java module when this class is loaded, or reports a sensible diagnostic message
	 * about why it failed.
	 */
	static {
		try {
			System.loadLibrary("sbmlj");
			// For extra safety, check that the jar file is in the classpath.
			Class.forName("org.sbml.libsbml.libsbml");
		} catch (UnsatisfiedLinkError e) {
			System.err.println("Error encountered while attempting to load libSBML:");
			System.err
					.println("Please check the value of your "
							+ (System.getProperty("os.name").startsWith("Mac OS") ? "DYLD_LIBRARY_PATH"
									: "LD_LIBRARY_PATH")
							+ " environment variable and/or your" + " 'java.library.path' system property (depending on"
							+ " which one you are using) to make sure it list the" + " directories needed to find the "
							+ System.mapLibraryName("sbmlj") + " library file and"
							+ " libraries it depends upon (e.g., the XML parser).");
			exit();
		} catch (ClassNotFoundException e) {
			System.err.println("Error: unable to load the file 'libsbmlj.jar'."
					+ " It is likely that your -classpath command line "
					+ " setting or your CLASSPATH environment variable " + " do not include the file 'libsbmlj.jar'.");
			e.printStackTrace();

			exit();
		} catch (SecurityException e) {
			System.err.println("Error encountered while attempting to load libSBML:");
			e.printStackTrace();
			System.err.println("Could not load the libSBML library files due to a" + " security exception.\n");
			exit();
		}
	}

}
