// Generated from KineticLawEval.g4 by ANTLR 4.5
package mtopology;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class KineticLawEvalLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, IDENTIFIER=3, NUMBER=4, MUL=5, DIV=6, MOD=7, ADD=8, SUB=9, 
		MULTILINECOMMENT=10, LINECOMMENT=11, WS=12;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "IDENTIFIER", "NUMBER", "MUL", "DIV", "MOD", "ADD", "SUB", 
		"MULTILINECOMMENT", "LINECOMMENT", "WS"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "')'", null, null, "'*'", "'/'", "'%'", "'+'", "'-'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, "IDENTIFIER", "NUMBER", "MUL", "DIV", "MOD", "ADD", 
		"SUB", "MULTILINECOMMENT", "LINECOMMENT", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public KineticLawEvalLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "KineticLawEval.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\16b\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\3\2\3\2\3\3\3\3\3\4\3\4\7\4\"\n\4\f\4\16\4%\13\4\3"+
		"\5\5\5(\n\5\3\5\6\5+\n\5\r\5\16\5,\3\5\3\5\6\5\61\n\5\r\5\16\5\62\5\5"+
		"\65\n\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\13\7"+
		"\13E\n\13\f\13\16\13H\13\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\6"+
		"\fS\n\f\r\f\16\fT\3\f\5\fX\n\f\3\f\3\f\3\r\6\r]\n\r\r\r\16\r^\3\r\3\r"+
		"\4FT\2\16\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\3\2"+
		"\6\5\2C\\aac|\6\2\62;C\\aac|\3\3\f\f\5\2\13\f\16\17\"\"i\2\3\3\2\2\2\2"+
		"\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2"+
		"\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\3"+
		"\33\3\2\2\2\5\35\3\2\2\2\7\37\3\2\2\2\t\'\3\2\2\2\13\66\3\2\2\2\r8\3\2"+
		"\2\2\17:\3\2\2\2\21<\3\2\2\2\23>\3\2\2\2\25@\3\2\2\2\27N\3\2\2\2\31\\"+
		"\3\2\2\2\33\34\7*\2\2\34\4\3\2\2\2\35\36\7+\2\2\36\6\3\2\2\2\37#\t\2\2"+
		"\2 \"\t\3\2\2! \3\2\2\2\"%\3\2\2\2#!\3\2\2\2#$\3\2\2\2$\b\3\2\2\2%#\3"+
		"\2\2\2&(\7/\2\2\'&\3\2\2\2\'(\3\2\2\2(*\3\2\2\2)+\4\62;\2*)\3\2\2\2+,"+
		"\3\2\2\2,*\3\2\2\2,-\3\2\2\2-\64\3\2\2\2.\60\7\60\2\2/\61\4\62;\2\60/"+
		"\3\2\2\2\61\62\3\2\2\2\62\60\3\2\2\2\62\63\3\2\2\2\63\65\3\2\2\2\64.\3"+
		"\2\2\2\64\65\3\2\2\2\65\n\3\2\2\2\66\67\7,\2\2\67\f\3\2\2\289\7\61\2\2"+
		"9\16\3\2\2\2:;\7\'\2\2;\20\3\2\2\2<=\7-\2\2=\22\3\2\2\2>?\7/\2\2?\24\3"+
		"\2\2\2@A\7\61\2\2AB\7,\2\2BF\3\2\2\2CE\13\2\2\2DC\3\2\2\2EH\3\2\2\2FG"+
		"\3\2\2\2FD\3\2\2\2GI\3\2\2\2HF\3\2\2\2IJ\7,\2\2JK\7\61\2\2KL\3\2\2\2L"+
		"M\b\13\2\2M\26\3\2\2\2NO\7\61\2\2OP\7\61\2\2PR\3\2\2\2QS\13\2\2\2RQ\3"+
		"\2\2\2ST\3\2\2\2TU\3\2\2\2TR\3\2\2\2UW\3\2\2\2VX\t\4\2\2WV\3\2\2\2XY\3"+
		"\2\2\2YZ\b\f\2\2Z\30\3\2\2\2[]\t\5\2\2\\[\3\2\2\2]^\3\2\2\2^\\\3\2\2\2"+
		"^_\3\2\2\2_`\3\2\2\2`a\b\r\2\2a\32\3\2\2\2\f\2#\',\62\64FTW^\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}