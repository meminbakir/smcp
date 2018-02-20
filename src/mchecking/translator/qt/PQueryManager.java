/**
 * 
 */
package mchecking.translator.qt;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mce.Inputs;
import mchecking.ModelChecker;
import mchecking.enums.MCTypes;

/**
 * Used for:<br>
 * Loading PQueries from the file<br>
 * Translating a PQuery to MCs specification
 * 
 * @author Mehmet Emin BAKIR
 *
 */
public class PQueryManager {
	private static final Logger log = LoggerFactory.getLogger(PQueryManager.class);

	/**
	 * 
	 * TODO: If we load in main, then this can be removed.<br>
	 * Validates the in the file, and then loads queries to a list
	 * 
	 * @param pQueryFilePath
	 * @return
	 */
	public ArrayList<PQuery> loadPQueriesFromFile(String pQueryFilePath) {
		ArrayList<PQuery> pQueryList = null;
		String error = "";
		try {
			// check if all queries are valid
			// this.validatePQueries(pQueryFilePath);
			List<String> strPQueries = Files.readAllLines(Paths.get(pQueryFilePath), Charset.defaultCharset());
			if (strPQueries.size() > 0) {
				pQueryList = new ArrayList<PQuery>();
				int queryID = 0;
				int lineNumber = 0;
				boolean isComment = false;
				for (String strPQuery : strPQueries) {
					lineNumber++;
					strPQuery = strPQuery.trim();
					// here will add only single line comments or a regular query
					if (strPQuery.startsWith("//") || (strPQuery.startsWith("/*") && strPQuery.endsWith("*/"))) {
						continue;
					}
					// if there is multiple-line comment, we need to append them
					else if (strPQuery.startsWith("/*") && !strPQuery.endsWith("*/")) {
						isComment = true;
						continue;
					} else if (isComment && !strPQuery.endsWith("*/")) {
						isComment = true;
						continue;
					} else if (strPQuery.endsWith("*/")) {
						isComment = false;
						continue;
					}
					// if it is not comment then, validate PQuery and patterns
					if (!strPQuery.isEmpty()) {
						queryID++;
						PQuery pq = new PQuery();
						pq.setLineNumber(lineNumber);
						// validate, assign PQuery and Patterns
						pq.validateAndAssingPQuery(strPQuery);
						pq.setQueryID(queryID);
						pQueryList.add(pq);
					}
				}
			} else {
				error = "Query file is empty.";
				log.error(error);
			}
		} catch (Exception e) {
			error = "Error risen while trying to parse PQuery file at " + pQueryFilePath + "\n";
			error += e.getMessage();
			log.error(error);
			e.printStackTrace();
		}
		return pQueryList;
	}

	/**
	 * Checks if all queries are valid, if not throws exception. TODO: We check each PQuery inside PQuery class, so
	 * eventually this can be removed
	 * 
	 * @param pQueryFilePath
	 * @throws IOException
	 */
	// private void validatePQueries(String pQueryFilePath) throws Exception {
	// PQueryLexer lexer = new PQueryLexer(new ANTLRInputStream(new FileInputStream(pQueryFilePath)));
	// PQueryParser parser = new PQueryParser(new CommonTokenStream(lexer));
	// parser.addErrorListener(new BaseErrorListener() {
	// @Override
	// public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
	// String msg, RecognitionException e) {
	// throw new IllegalStateException("Pattern Query includes error at line " + line + " due to " + msg, e);
	// }
	// });
	// parser.pQuery();
	// }

	public MCQuery translate(Inputs input, PQuery pQuery, ModelChecker targetMC) {
		ANTLRInputStream inputStream = new ANTLRInputStream(pQuery.getPQuery());
		PQueryLexer lexer = new PQueryLexer(inputStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		PQueryParser parser = new PQueryParser(tokens);
		ParseTree tree = parser.pQuery();
		MCQuery translatedQuery = null;
		MCTypes targetMCType = targetMC.getType();
		if (targetMCType == MCTypes.PRISM) {
			translatedQuery = new PQuery2PRISM(targetMC);
			// translatedQuery.setTranslatedQuery("P>=0.5 [ true U na<20 ]"); @4Test purpose
		} else if (targetMCType == MCTypes.PLASMA) {
			translatedQuery = new PQuery2PLASMA(input, targetMC);
		} else if (targetMCType == MCTypes.YMER) {
			translatedQuery = new PQuery2YMER(targetMC);
		} else if (targetMCType == MCTypes.MRMC) {
			translatedQuery = new PQuery2MRMC(targetMC);
		} else if (targetMCType == MCTypes.MC2) {
			translatedQuery = new PQuery2MC2(targetMC);
		}
		if (translatedQuery != null) {
			translatedQuery.visit(tree);
			translatedQuery.setPQuery(pQuery);
		}
		return translatedQuery;
	}
}
