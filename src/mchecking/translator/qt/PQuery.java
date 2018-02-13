/**
 * 
 */
package mchecking.translator.qt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mce.util.StringUtils;
import mchecking.enums.Pattern;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class PQuery {
	private static final Logger log = LoggerFactory.getLogger(PQuery.class);

	private String pQuery = "";
	private int queryID = 0;
	private int lineNumber = 0;
	private List<Pattern> patterns = null;

	Set<String> queriedSpecies = new HashSet<>();

	/**
	 * Get the user defined pattern query
	 * 
	 * @return the pattern query
	 */
	public String getPQuery() {
		return pQuery;
	}

	/**
	 * It checks the syntax of the PQuery<br>
	 * Assigns PQuery<br>
	 * Extracts and assigns the patterns inside the PQuery
	 * 
	 * @param pQuery
	 *            the pattern query to set
	 * @throws Exception
	 */
	public void validateAndAssingPQuery(String pQuery) throws Exception {
		// Validate query
		CommonTokenStream tokens;
		try {
			ANTLRInputStream inputStream = new ANTLRInputStream(pQuery);
			PQueryLexer lexer = new PQueryLexer(inputStream);
			tokens = new CommonTokenStream(lexer);
			PQueryParser parser = new PQueryParser(tokens);
			parser.addErrorListener(new BaseErrorListener() {
				@Override
				public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
						int charPositionInLine, String msg, RecognitionException e) {
					throw new IllegalStateException("Pattern Query includes error at line " + line + " due to " + msg,
							e);
				}
			});
			parser.pQuery();
			// if valid that is no exception has thrown, assign the pQuery
			this.pQuery = pQuery;
			// Extract the patterns
			extractPatternsAndQueriedSpecies(tokens);
		} catch (IllegalStateException e) {
			String error = "Error risen while trying to parse PQuery file\n";
			error += e.getMessage();
			e.printStackTrace();
			error += "\nExiting!\n";
			log.error(error);
			System.exit(1);
		}
	}

	/**
	 * Which line of query
	 * 
	 * @return the queryID
	 */
	public int getQueryID() {
		return queryID;
	}

	/**
	 * @param queryID
	 *            the queryID to set
	 */
	public void setQueryID(int queryID) {
		this.queryID = queryID;
	}

	/**
	 * @return the lineNumber
	 */
	public int getLineNumber() {
		return lineNumber;
	}

	/**
	 * @param lineNumber
	 *            the lineNumber to set
	 */
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public List<Pattern> getPatterns() {
		return patterns;
	}

	/**
	 * Concatenates all pattern names(descriptions)
	 * 
	 * @return
	 */
	public String getPatternsAsString() {
		String result = "";
		for (Pattern pattern : patterns) {
			result += pattern.toString();
		}
		return result;
	}

	/**
	 * Iterates over tokens to extract Patterns,<br>
	 * Also extracts the IDENTIFIERS, namely the species inside the query.
	 * 
	 * @param tokens
	 */
	private void extractPatternsAndQueriedSpecies(CommonTokenStream tokens) {
		patterns = new ArrayList<Pattern>();
		// gets all tokens
		tokens.fill();
		// iterate over token to extract patterns.
		for (Token token : tokens.getTokens()) {
			switch (token.getType()) {
			case PQueryLexer.WITH_PROBABILITY:
				patterns.add(Pattern.WITH_PROBABILITY);
				break;
			case PQueryLexer.WHAT_PROBABILITY:
				patterns.add(Pattern.WHAT_PROBABILITY);
				break;
			case PQueryLexer.NOT:
				patterns.add(Pattern.NOT);
				break;
			case PQueryLexer.EVENTUALLY:
				patterns.add(Pattern.EVENTUALLY);
				break;
			case PQueryLexer.ALWAYS:
				patterns.add(Pattern.ALWAYS);
				break;
			case PQueryLexer.NEXT:
				patterns.add(Pattern.NEXT);
				break;
			case PQueryLexer.NEVER:
				patterns.add(Pattern.NEVER);
				break;
			case PQueryLexer.INFINITELY_OFTEN:
				patterns.add(Pattern.INFINITELY_OFTEN);
				break;
			case PQueryLexer.STEADY_STATE:
				patterns.add(Pattern.STEADY_STATE);
				break;
			case PQueryLexer.UNTIL:
				patterns.add(Pattern.UNTIL);
				break;
			case PQueryLexer.WEAK_UNTIL:
				patterns.add(Pattern.WEAK_UNTIL);
				break;
			case PQueryLexer.RELEASE:
				patterns.add(Pattern.RELEASE);
				break;
			case PQueryLexer.FOLLOWS:
				patterns.add(Pattern.FOLLOWS);
				break;
			case PQueryLexer.PRECEDES:
				patterns.add(Pattern.PRECEDES);
				break;
			case PQueryLexer.IDENTIFIER:
				if (!StringUtils.isNullOrEmpty(token.getText()))
					queriedSpecies.add(token.getText());
				break;
			}
		}
	}

	/**
	 * Returns the species inside the query
	 * 
	 * @return
	 */
	public Set<String> getQueriedSpecies() {
		return queriedSpecies;
	}

	// TODO: be removed
	// private void setPattern() {
	// // we need to identify whether =? or >k is queried, then we need to
	// assign matched MCs to patterns.
	// for (Pattern p : Pattern.values()) {
	// if (this.patterns == null) {
	// if (this.pQuery.toLowerCase().contains(p.toString().toLowerCase())) {
	// this.patterns.add(p);
	// break;
	// }
	// }
	// }
	// }

}
