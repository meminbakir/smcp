package mchecking.translator.qt;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import mce.Inputs;
import mchecking.ModelChecker;
import mchecking.enums.MCTypes;
import mchecking.translator.qt.PQueryParser.PQueryContext;

public class PQueryTest {
	static String path = "./src/pQuery.pq";

	public static void main(String[] args) {
		try {
			String arguments = "-s ./models/sbml/test/temp/147_BIOMD0000000206.xml.ssa.sbml"
					+ " -q ./src/pQuery.pq -lb 0 -upb 100 -simsamples 3 -simDepth 100";
			// TODO we can allow user to specify a target MC if they don't want best
			// one be estimated.

			long startTime = System.currentTimeMillis();
			String directory = "./models/sbml/test/temp";
			arguments = "-d " + directory + " -q ./src/pQuery.pq -lb 0 -upb 100 -simsamples 500 -simDepth 5000";
			args = arguments.split(" ");
			// new PQueryTest().testPQs();
			List<String> list = Files.readAllLines(Paths.get(path), Charset.defaultCharset());
			for (String query : list) {
				// testQuery(query);
				translatePQueries4AllMCs(args, query);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param query
	 */
	@SuppressWarnings("unused")
	private static void testQuery(String query) {
		ParseTree tree = getParseTree(query);
		ModelChecker targetMC = new ModelChecker(MCTypes.MC2);
		MCQuery pQuery2All = new PQuery2MC2(targetMC);
		System.out.println("PQuery: " + query);
		System.out.println("Translated Query: " + pQuery2All.visit(tree));
	}

	private static void translatePQueries4AllMCs(String[] args, String query) {
		System.out.println("***********************************");
		System.out.println("PQuery: " + query);
		System.out.println("----------------------------------");
		ParseTree tree = getParseTree(query);
		MCQuery translatedQuery = null;
		for (MCTypes targetMC : MCTypes.values()) {
			ModelChecker targetModelChecker = new ModelChecker(targetMC);
			if (targetMC == MCTypes.PRISM) {
				translatedQuery = new PQuery2PRISM(targetModelChecker);
			} else if (targetMC == MCTypes.PLASMA) {
				Inputs input = new Inputs(args);
				translatedQuery = new PQuery2PLASMA(input, targetModelChecker);
			} else if (targetMC == MCTypes.YMER) {
				translatedQuery = new PQuery2YMER(targetModelChecker);
			} else if (targetMC == MCTypes.MRMC) {
				translatedQuery = new PQuery2MRMC(targetModelChecker);
			} else if (targetMC == MCTypes.MC2) {
				translatedQuery = new PQuery2MC2(targetModelChecker);
			} else
				continue;
			System.out.println(String.format("%6s: %s", targetMC, translatedQuery.visit(tree)));
		}
	}

	/**
	 * @param query
	 * @return
	 */
	private static PQueryContext getParseTree(String query) {
		ANTLRInputStream inputStream = new ANTLRInputStream(query);
		PQueryLexer lexer = new PQueryLexer(inputStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		PQueryParser parser = new PQueryParser(tokens);

		return parser.pQuery();
	}

	/**
	 * Test if all queries inside PQuery file are parse-able.
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void testPQs() throws IOException {
		PQueryLexer lexer = new PQueryLexer(new ANTLRInputStream(this.getClass().getResourceAsStream(path)));
		PQueryParser parser = new PQueryParser(new CommonTokenStream(lexer));
		parser.addErrorListener(new BaseErrorListener() {
			@Override
			public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
					String msg, RecognitionException e) {
				throw new IllegalStateException("failed to parse at line " + line + " due to " + msg, e);
			}
		});
	}

	@Test
	public void PQuery2PrismTest() throws IOException {
		PQueryLexer lexer = new PQueryLexer(new ANTLRInputStream(this.getClass().getResourceAsStream(path)));
		PQueryParser parser = new PQueryParser(new CommonTokenStream(lexer));
		parser.addErrorListener(new BaseErrorListener() {
			@Override
			public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
					String msg, RecognitionException e) {
				throw new IllegalStateException("failed to parse at line " + line + " due to " + msg, e);
			}
		});
		ParseTree tree = parser.pQuery();

		// ParseTree tree = parser.pQuery();
		System.out.println(tree.toStringTree(parser));
		// PQuery2PRISM pQuery2All = new PQuery2PRISM();
		// System.out.println("PQuery2PRISM:" + pQuery2All.visit(tree));

	}

	@Test
	public void PQuery2MRMCTest() throws IOException {
		PQueryLexer lexer = new PQueryLexer(new ANTLRInputStream(this.getClass().getResourceAsStream("/pQuery.pq")));
		PQueryParser parser = new PQueryParser(new CommonTokenStream(lexer));
		parser.addErrorListener(new BaseErrorListener() {
			@Override
			public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
					String msg, RecognitionException e) {
				throw new IllegalStateException("failed to parse at line " + line + " due to " + msg, e);
			}
		});
		ParseTree tree = parser.pQuery();
		ModelChecker targetMC = new ModelChecker(MCTypes.MC2);
		MCQuery pQuery2All = new PQuery2MC2(targetMC);
		System.out.println("PQuery2MRMC:" + pQuery2All.visit(tree));

	}

}
