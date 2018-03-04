// Generated from PQuery.g4 by ANTLR 4.5
package mchecking.translator.qt;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PQueryParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, WITH_PROBABILITY=3, WHAT_PROBABILITY=4, EVENTUALLY=5, 
		ALWAYS=6, NEXT=7, NEVER=8, INFINITELY_OFTEN=9, STEADY_STATE=10, UNTIL=11, 
		WEAK_UNTIL=12, RELEASE=13, FOLLOWS=14, PRECEDES=15, FALSE=16, TRUE=17, 
		NOT=18, IMPLIES=19, OR=20, AND=21, RELATION=22, IDENTIFIER=23, NUMBER=24, 
		MUL=25, DIV=26, MOD=27, ADD=28, SUB=29, MULTILINECOMMENT=30, LINECOMMENT=31, 
		WS=32;
	public static final int
		RULE_pQuery = 0, RULE_inequalityQuery = 1, RULE_questionMark = 2, RULE_query = 3, 
		RULE_unaryPattern = 4, RULE_binaryPattern = 5, RULE_notPattern = 6, RULE_eventually = 7, 
		RULE_always = 8, RULE_next = 9, RULE_never = 10, RULE_infinitelyOften = 11, 
		RULE_steadyState = 12, RULE_until = 13, RULE_weakUntil = 14, RULE_release = 15, 
		RULE_follows = 16, RULE_precedes = 17, RULE_expr = 18, RULE_boolExpr = 19, 
		RULE_logicalConstant = 20, RULE_logicalEntity = 21, RULE_probability = 22, 
		RULE_numericExp = 23, RULE_numeric = 24;
	public static final String[] ruleNames = {
		"pQuery", "inequalityQuery", "questionMark", "query", "unaryPattern", 
		"binaryPattern", "notPattern", "eventually", "always", "next", "never", 
		"infinitelyOften", "steadyState", "until", "weakUntil", "release", "follows", 
		"precedes", "expr", "boolExpr", "logicalConstant", "logicalEntity", "probability", 
		"numericExp", "numeric"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "')'", null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, "'*'", "'/'", "'%'", "'+'", "'-'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, "WITH_PROBABILITY", "WHAT_PROBABILITY", "EVENTUALLY", 
		"ALWAYS", "NEXT", "NEVER", "INFINITELY_OFTEN", "STEADY_STATE", "UNTIL", 
		"WEAK_UNTIL", "RELEASE", "FOLLOWS", "PRECEDES", "FALSE", "TRUE", "NOT", 
		"IMPLIES", "OR", "AND", "RELATION", "IDENTIFIER", "NUMBER", "MUL", "DIV", 
		"MOD", "ADD", "SUB", "MULTILINECOMMENT", "LINECOMMENT", "WS"
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

	@Override
	public String getGrammarFileName() { return "PQuery.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public PQueryParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class PQueryContext extends ParserRuleContext {
		public List<InequalityQueryContext> inequalityQuery() {
			return getRuleContexts(InequalityQueryContext.class);
		}
		public InequalityQueryContext inequalityQuery(int i) {
			return getRuleContext(InequalityQueryContext.class,i);
		}
		public List<QuestionMarkContext> questionMark() {
			return getRuleContexts(QuestionMarkContext.class);
		}
		public QuestionMarkContext questionMark(int i) {
			return getRuleContext(QuestionMarkContext.class,i);
		}
		public PQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pQuery; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitPQuery(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PQueryContext pQuery() throws RecognitionException {
		PQueryContext _localctx = new PQueryContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_pQuery);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(54);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << WITH_PROBABILITY) | (1L << WHAT_PROBABILITY) | (1L << EVENTUALLY) | (1L << ALWAYS) | (1L << NEXT) | (1L << NEVER) | (1L << INFINITELY_OFTEN) | (1L << STEADY_STATE) | (1L << FALSE) | (1L << TRUE) | (1L << NOT) | (1L << IDENTIFIER) | (1L << NUMBER))) != 0)) {
				{
				setState(52);
				switch (_input.LA(1)) {
				case T__0:
				case WITH_PROBABILITY:
				case EVENTUALLY:
				case ALWAYS:
				case NEXT:
				case NEVER:
				case INFINITELY_OFTEN:
				case STEADY_STATE:
				case FALSE:
				case TRUE:
				case NOT:
				case IDENTIFIER:
				case NUMBER:
					{
					setState(50);
					inequalityQuery();
					}
					break;
				case WHAT_PROBABILITY:
					{
					setState(51);
					questionMark();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(56);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InequalityQueryContext extends ParserRuleContext {
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public TerminalNode WITH_PROBABILITY() { return getToken(PQueryParser.WITH_PROBABILITY, 0); }
		public ProbabilityContext probability() {
			return getRuleContext(ProbabilityContext.class,0);
		}
		public InequalityQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inequalityQuery; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitInequalityQuery(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InequalityQueryContext inequalityQuery() throws RecognitionException {
		InequalityQueryContext _localctx = new InequalityQueryContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_inequalityQuery);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59);
			_la = _input.LA(1);
			if (_la==WITH_PROBABILITY) {
				{
				setState(57);
				match(WITH_PROBABILITY);
				setState(58);
				probability();
				}
			}

			setState(61);
			query();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QuestionMarkContext extends ParserRuleContext {
		public TerminalNode WHAT_PROBABILITY() { return getToken(PQueryParser.WHAT_PROBABILITY, 0); }
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public QuestionMarkContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_questionMark; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitQuestionMark(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QuestionMarkContext questionMark() throws RecognitionException {
		QuestionMarkContext _localctx = new QuestionMarkContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_questionMark);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(63);
			match(WHAT_PROBABILITY);
			setState(64);
			query();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QueryContext extends ParserRuleContext {
		public UnaryPatternContext unaryPattern() {
			return getRuleContext(UnaryPatternContext.class,0);
		}
		public BinaryPatternContext binaryPattern() {
			return getRuleContext(BinaryPatternContext.class,0);
		}
		public QueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_query; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitQuery(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QueryContext query() throws RecognitionException {
		QueryContext _localctx = new QueryContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_query);
		try {
			setState(68);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(66);
				unaryPattern();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(67);
				binaryPattern();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnaryPatternContext extends ParserRuleContext {
		public NotPatternContext notPattern() {
			return getRuleContext(NotPatternContext.class,0);
		}
		public EventuallyContext eventually() {
			return getRuleContext(EventuallyContext.class,0);
		}
		public AlwaysContext always() {
			return getRuleContext(AlwaysContext.class,0);
		}
		public NextContext next() {
			return getRuleContext(NextContext.class,0);
		}
		public NeverContext never() {
			return getRuleContext(NeverContext.class,0);
		}
		public InfinitelyOftenContext infinitelyOften() {
			return getRuleContext(InfinitelyOftenContext.class,0);
		}
		public SteadyStateContext steadyState() {
			return getRuleContext(SteadyStateContext.class,0);
		}
		public UnaryPatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryPattern; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitUnaryPattern(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryPatternContext unaryPattern() throws RecognitionException {
		UnaryPatternContext _localctx = new UnaryPatternContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_unaryPattern);
		try {
			setState(77);
			switch (_input.LA(1)) {
			case NOT:
				enterOuterAlt(_localctx, 1);
				{
				setState(70);
				notPattern();
				}
				break;
			case EVENTUALLY:
				enterOuterAlt(_localctx, 2);
				{
				setState(71);
				eventually();
				}
				break;
			case ALWAYS:
				enterOuterAlt(_localctx, 3);
				{
				setState(72);
				always();
				}
				break;
			case NEXT:
				enterOuterAlt(_localctx, 4);
				{
				setState(73);
				next();
				}
				break;
			case NEVER:
				enterOuterAlt(_localctx, 5);
				{
				setState(74);
				never();
				}
				break;
			case INFINITELY_OFTEN:
				enterOuterAlt(_localctx, 6);
				{
				setState(75);
				infinitelyOften();
				}
				break;
			case STEADY_STATE:
				enterOuterAlt(_localctx, 7);
				{
				setState(76);
				steadyState();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BinaryPatternContext extends ParserRuleContext {
		public UntilContext until() {
			return getRuleContext(UntilContext.class,0);
		}
		public WeakUntilContext weakUntil() {
			return getRuleContext(WeakUntilContext.class,0);
		}
		public ReleaseContext release() {
			return getRuleContext(ReleaseContext.class,0);
		}
		public FollowsContext follows() {
			return getRuleContext(FollowsContext.class,0);
		}
		public PrecedesContext precedes() {
			return getRuleContext(PrecedesContext.class,0);
		}
		public BinaryPatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_binaryPattern; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitBinaryPattern(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BinaryPatternContext binaryPattern() throws RecognitionException {
		BinaryPatternContext _localctx = new BinaryPatternContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_binaryPattern);
		try {
			setState(84);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(79);
				until();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(80);
				weakUntil();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(81);
				release();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(82);
				follows();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(83);
				precedes();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NotPatternContext extends ParserRuleContext {
		public TerminalNode NOT() { return getToken(PQueryParser.NOT, 0); }
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public NotPatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_notPattern; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitNotPattern(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NotPatternContext notPattern() throws RecognitionException {
		NotPatternContext _localctx = new NotPatternContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_notPattern);
		try {
			setState(93);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(86);
				match(NOT);
				setState(87);
				query();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(88);
				match(NOT);
				setState(89);
				match(T__0);
				setState(90);
				query();
				setState(91);
				match(T__1);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EventuallyContext extends ParserRuleContext {
		public TerminalNode EVENTUALLY() { return getToken(PQueryParser.EVENTUALLY, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public EventuallyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eventually; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitEventually(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EventuallyContext eventually() throws RecognitionException {
		EventuallyContext _localctx = new EventuallyContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_eventually);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
			match(EVENTUALLY);
			setState(96);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AlwaysContext extends ParserRuleContext {
		public TerminalNode ALWAYS() { return getToken(PQueryParser.ALWAYS, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public AlwaysContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_always; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitAlways(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AlwaysContext always() throws RecognitionException {
		AlwaysContext _localctx = new AlwaysContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_always);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(98);
			match(ALWAYS);
			setState(99);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NextContext extends ParserRuleContext {
		public TerminalNode NEXT() { return getToken(PQueryParser.NEXT, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public NextContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_next; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitNext(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NextContext next() throws RecognitionException {
		NextContext _localctx = new NextContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_next);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(101);
			match(NEXT);
			setState(102);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NeverContext extends ParserRuleContext {
		public TerminalNode NEVER() { return getToken(PQueryParser.NEVER, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public NeverContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_never; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitNever(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NeverContext never() throws RecognitionException {
		NeverContext _localctx = new NeverContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_never);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
			match(NEVER);
			setState(105);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InfinitelyOftenContext extends ParserRuleContext {
		public TerminalNode INFINITELY_OFTEN() { return getToken(PQueryParser.INFINITELY_OFTEN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public InfinitelyOftenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_infinitelyOften; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitInfinitelyOften(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InfinitelyOftenContext infinitelyOften() throws RecognitionException {
		InfinitelyOftenContext _localctx = new InfinitelyOftenContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_infinitelyOften);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			match(INFINITELY_OFTEN);
			setState(108);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SteadyStateContext extends ParserRuleContext {
		public TerminalNode STEADY_STATE() { return getToken(PQueryParser.STEADY_STATE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public SteadyStateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_steadyState; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitSteadyState(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SteadyStateContext steadyState() throws RecognitionException {
		SteadyStateContext _localctx = new SteadyStateContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_steadyState);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
			match(STEADY_STATE);
			setState(111);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UntilContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode UNTIL() { return getToken(PQueryParser.UNTIL, 0); }
		public UntilContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_until; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitUntil(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UntilContext until() throws RecognitionException {
		UntilContext _localctx = new UntilContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_until);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(113);
			expr(0);
			setState(114);
			match(UNTIL);
			setState(115);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WeakUntilContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode WEAK_UNTIL() { return getToken(PQueryParser.WEAK_UNTIL, 0); }
		public WeakUntilContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_weakUntil; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitWeakUntil(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WeakUntilContext weakUntil() throws RecognitionException {
		WeakUntilContext _localctx = new WeakUntilContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_weakUntil);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(117);
			expr(0);
			setState(118);
			match(WEAK_UNTIL);
			setState(119);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReleaseContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode RELEASE() { return getToken(PQueryParser.RELEASE, 0); }
		public ReleaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_release; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitRelease(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReleaseContext release() throws RecognitionException {
		ReleaseContext _localctx = new ReleaseContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_release);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(121);
			expr(0);
			setState(122);
			match(RELEASE);
			setState(123);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FollowsContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode FOLLOWS() { return getToken(PQueryParser.FOLLOWS, 0); }
		public FollowsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_follows; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitFollows(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FollowsContext follows() throws RecognitionException {
		FollowsContext _localctx = new FollowsContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_follows);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(125);
			expr(0);
			setState(126);
			match(FOLLOWS);
			setState(127);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrecedesContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode PRECEDES() { return getToken(PQueryParser.PRECEDES, 0); }
		public PrecedesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_precedes; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitPrecedes(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrecedesContext precedes() throws RecognitionException {
		PrecedesContext _localctx = new PrecedesContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_precedes);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(129);
			expr(0);
			setState(130);
			match(PRECEDES);
			setState(131);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class BinaryLogicalExprContext extends ExprContext {
		public Token operator;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode AND() { return getToken(PQueryParser.AND, 0); }
		public TerminalNode OR() { return getToken(PQueryParser.OR, 0); }
		public TerminalNode IMPLIES() { return getToken(PQueryParser.IMPLIES, 0); }
		public BinaryLogicalExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitBinaryLogicalExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NotExprContext extends ExprContext {
		public TerminalNode NOT() { return getToken(PQueryParser.NOT, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public NotExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitNotExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NoParanBoolExprContext extends ExprContext {
		public BoolExprContext boolExpr() {
			return getRuleContext(BoolExprContext.class,0);
		}
		public NoParanBoolExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitNoParanBoolExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParanthesesExprContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ParanthesesExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitParanthesesExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 36;
		enterRecursionRule(_localctx, 36, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				_localctx = new NotExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(134);
				match(NOT);
				setState(135);
				expr(4);
				}
				break;
			case 2:
				{
				_localctx = new NoParanBoolExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(136);
				boolExpr();
				}
				break;
			case 3:
				{
				_localctx = new ParanthesesExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(137);
				match(T__0);
				setState(138);
				expr(0);
				setState(139);
				match(T__1);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(148);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new BinaryLogicalExprContext(new ExprContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_expr);
					setState(143);
					if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
					setState(144);
					((BinaryLogicalExprContext)_localctx).operator = _input.LT(1);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IMPLIES) | (1L << OR) | (1L << AND))) != 0)) ) {
						((BinaryLogicalExprContext)_localctx).operator = (Token)_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(145);
					expr(4);
					}
					} 
				}
				setState(150);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class BoolExprContext extends ParserRuleContext {
		public LogicalEntityContext logicalEntity() {
			return getRuleContext(LogicalEntityContext.class,0);
		}
		public List<NumericExpContext> numericExp() {
			return getRuleContexts(NumericExpContext.class);
		}
		public NumericExpContext numericExp(int i) {
			return getRuleContext(NumericExpContext.class,i);
		}
		public TerminalNode RELATION() { return getToken(PQueryParser.RELATION, 0); }
		public BoolExprContext boolExpr() {
			return getRuleContext(BoolExprContext.class,0);
		}
		public BoolExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_boolExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitBoolExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BoolExprContext boolExpr() throws RecognitionException {
		BoolExprContext _localctx = new BoolExprContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_boolExpr);
		try {
			setState(160);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(151);
				logicalEntity();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(152);
				numericExp(0);
				setState(153);
				match(RELATION);
				setState(154);
				numericExp(0);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(156);
				match(T__0);
				setState(157);
				boolExpr();
				setState(158);
				match(T__1);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LogicalConstantContext extends ParserRuleContext {
		public TerminalNode TRUE() { return getToken(PQueryParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(PQueryParser.FALSE, 0); }
		public LogicalConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicalConstant; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitLogicalConstant(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogicalConstantContext logicalConstant() throws RecognitionException {
		LogicalConstantContext _localctx = new LogicalConstantContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_logicalConstant);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			_la = _input.LA(1);
			if ( !(_la==FALSE || _la==TRUE) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LogicalEntityContext extends ParserRuleContext {
		public LogicalConstantContext logicalConstant() {
			return getRuleContext(LogicalConstantContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(PQueryParser.IDENTIFIER, 0); }
		public LogicalEntityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicalEntity; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitLogicalEntity(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogicalEntityContext logicalEntity() throws RecognitionException {
		LogicalEntityContext _localctx = new LogicalEntityContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_logicalEntity);
		try {
			setState(166);
			switch (_input.LA(1)) {
			case FALSE:
			case TRUE:
				enterOuterAlt(_localctx, 1);
				{
				setState(164);
				logicalConstant();
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(165);
				match(IDENTIFIER);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ProbabilityContext extends ParserRuleContext {
		public TerminalNode RELATION() { return getToken(PQueryParser.RELATION, 0); }
		public NumericExpContext numericExp() {
			return getRuleContext(NumericExpContext.class,0);
		}
		public ProbabilityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_probability; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitProbability(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProbabilityContext probability() throws RecognitionException {
		ProbabilityContext _localctx = new ProbabilityContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_probability);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(168);
			match(RELATION);
			setState(169);
			numericExp(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NumericExpContext extends ParserRuleContext {
		public NumericExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numericExp; }
	 
		public NumericExpContext() { }
		public void copyFrom(NumericExpContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class NoOpNumericExprContext extends NumericExpContext {
		public NumericContext numeric() {
			return getRuleContext(NumericContext.class,0);
		}
		public NoOpNumericExprContext(NumericExpContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitNoOpNumericExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AddNumericExprContext extends NumericExpContext {
		public Token op;
		public List<NumericExpContext> numericExp() {
			return getRuleContexts(NumericExpContext.class);
		}
		public NumericExpContext numericExp(int i) {
			return getRuleContext(NumericExpContext.class,i);
		}
		public AddNumericExprContext(NumericExpContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitAddNumericExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MultNumericExprContext extends NumericExpContext {
		public Token op;
		public List<NumericExpContext> numericExp() {
			return getRuleContexts(NumericExpContext.class);
		}
		public NumericExpContext numericExp(int i) {
			return getRuleContext(NumericExpContext.class,i);
		}
		public MultNumericExprContext(NumericExpContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitMultNumericExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParensNumericExprContext extends NumericExpContext {
		public NumericExpContext numericExp() {
			return getRuleContext(NumericExpContext.class,0);
		}
		public ParensNumericExprContext(NumericExpContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitParensNumericExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumericExpContext numericExp() throws RecognitionException {
		return numericExp(0);
	}

	private NumericExpContext numericExp(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		NumericExpContext _localctx = new NumericExpContext(_ctx, _parentState);
		NumericExpContext _prevctx = _localctx;
		int _startState = 46;
		enterRecursionRule(_localctx, 46, RULE_numericExp, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(177);
			switch (_input.LA(1)) {
			case IDENTIFIER:
			case NUMBER:
				{
				_localctx = new NoOpNumericExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(172);
				numeric();
				}
				break;
			case T__0:
				{
				_localctx = new ParensNumericExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(173);
				match(T__0);
				setState(174);
				numericExp(0);
				setState(175);
				match(T__1);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(187);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(185);
					switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
					case 1:
						{
						_localctx = new MultNumericExprContext(new NumericExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_numericExp);
						setState(179);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(180);
						((MultNumericExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MUL) | (1L << DIV) | (1L << MOD))) != 0)) ) {
							((MultNumericExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(181);
						numericExp(5);
						}
						break;
					case 2:
						{
						_localctx = new AddNumericExprContext(new NumericExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_numericExp);
						setState(182);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(183);
						((AddNumericExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==ADD || _la==SUB) ) {
							((AddNumericExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(184);
						numericExp(4);
						}
						break;
					}
					} 
				}
				setState(189);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class NumericContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(PQueryParser.IDENTIFIER, 0); }
		public TerminalNode NUMBER() { return getToken(PQueryParser.NUMBER, 0); }
		public NumericContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numeric; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PQueryVisitor ) return ((PQueryVisitor<? extends T>)visitor).visitNumeric(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumericContext numeric() throws RecognitionException {
		NumericContext _localctx = new NumericContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_numeric);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(190);
			_la = _input.LA(1);
			if ( !(_la==IDENTIFIER || _la==NUMBER) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 18:
			return expr_sempred((ExprContext)_localctx, predIndex);
		case 23:
			return numericExp_sempred((NumericExpContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 3);
		}
		return true;
	}
	private boolean numericExp_sempred(NumericExpContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 4);
		case 2:
			return precpred(_ctx, 3);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\"\u00c3\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\3\2\3\2\7\2\67\n\2\f\2\16\2:\13\2\3\3\3\3\5\3>\n\3\3\3\3\3"+
		"\3\4\3\4\3\4\3\5\3\5\5\5G\n\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6P\n\6\3\7"+
		"\3\7\3\7\3\7\3\7\5\7W\n\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\b`\n\b\3\t\3\t"+
		"\3\t\3\n\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\16"+
		"\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\22\3\22"+
		"\3\22\3\22\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\5\24\u0090\n\24\3\24\3\24\3\24\7\24\u0095\n\24\f\24\16\24\u0098\13\24"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u00a3\n\25\3\26\3\26"+
		"\3\27\3\27\5\27\u00a9\n\27\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31"+
		"\5\31\u00b4\n\31\3\31\3\31\3\31\3\31\3\31\3\31\7\31\u00bc\n\31\f\31\16"+
		"\31\u00bf\13\31\3\32\3\32\3\32\2\4&\60\33\2\4\6\b\n\f\16\20\22\24\26\30"+
		"\32\34\36 \"$&(*,.\60\62\2\7\3\2\25\27\3\2\22\23\3\2\33\35\3\2\36\37\3"+
		"\2\31\32\u00c1\28\3\2\2\2\4=\3\2\2\2\6A\3\2\2\2\bF\3\2\2\2\nO\3\2\2\2"+
		"\fV\3\2\2\2\16_\3\2\2\2\20a\3\2\2\2\22d\3\2\2\2\24g\3\2\2\2\26j\3\2\2"+
		"\2\30m\3\2\2\2\32p\3\2\2\2\34s\3\2\2\2\36w\3\2\2\2 {\3\2\2\2\"\177\3\2"+
		"\2\2$\u0083\3\2\2\2&\u008f\3\2\2\2(\u00a2\3\2\2\2*\u00a4\3\2\2\2,\u00a8"+
		"\3\2\2\2.\u00aa\3\2\2\2\60\u00b3\3\2\2\2\62\u00c0\3\2\2\2\64\67\5\4\3"+
		"\2\65\67\5\6\4\2\66\64\3\2\2\2\66\65\3\2\2\2\67:\3\2\2\28\66\3\2\2\28"+
		"9\3\2\2\29\3\3\2\2\2:8\3\2\2\2;<\7\5\2\2<>\5.\30\2=;\3\2\2\2=>\3\2\2\2"+
		">?\3\2\2\2?@\5\b\5\2@\5\3\2\2\2AB\7\6\2\2BC\5\b\5\2C\7\3\2\2\2DG\5\n\6"+
		"\2EG\5\f\7\2FD\3\2\2\2FE\3\2\2\2G\t\3\2\2\2HP\5\16\b\2IP\5\20\t\2JP\5"+
		"\22\n\2KP\5\24\13\2LP\5\26\f\2MP\5\30\r\2NP\5\32\16\2OH\3\2\2\2OI\3\2"+
		"\2\2OJ\3\2\2\2OK\3\2\2\2OL\3\2\2\2OM\3\2\2\2ON\3\2\2\2P\13\3\2\2\2QW\5"+
		"\34\17\2RW\5\36\20\2SW\5 \21\2TW\5\"\22\2UW\5$\23\2VQ\3\2\2\2VR\3\2\2"+
		"\2VS\3\2\2\2VT\3\2\2\2VU\3\2\2\2W\r\3\2\2\2XY\7\24\2\2Y`\5\b\5\2Z[\7\24"+
		"\2\2[\\\7\3\2\2\\]\5\b\5\2]^\7\4\2\2^`\3\2\2\2_X\3\2\2\2_Z\3\2\2\2`\17"+
		"\3\2\2\2ab\7\7\2\2bc\5&\24\2c\21\3\2\2\2de\7\b\2\2ef\5&\24\2f\23\3\2\2"+
		"\2gh\7\t\2\2hi\5&\24\2i\25\3\2\2\2jk\7\n\2\2kl\5&\24\2l\27\3\2\2\2mn\7"+
		"\13\2\2no\5&\24\2o\31\3\2\2\2pq\7\f\2\2qr\5&\24\2r\33\3\2\2\2st\5&\24"+
		"\2tu\7\r\2\2uv\5&\24\2v\35\3\2\2\2wx\5&\24\2xy\7\16\2\2yz\5&\24\2z\37"+
		"\3\2\2\2{|\5&\24\2|}\7\17\2\2}~\5&\24\2~!\3\2\2\2\177\u0080\5&\24\2\u0080"+
		"\u0081\7\20\2\2\u0081\u0082\5&\24\2\u0082#\3\2\2\2\u0083\u0084\5&\24\2"+
		"\u0084\u0085\7\21\2\2\u0085\u0086\5&\24\2\u0086%\3\2\2\2\u0087\u0088\b"+
		"\24\1\2\u0088\u0089\7\24\2\2\u0089\u0090\5&\24\6\u008a\u0090\5(\25\2\u008b"+
		"\u008c\7\3\2\2\u008c\u008d\5&\24\2\u008d\u008e\7\4\2\2\u008e\u0090\3\2"+
		"\2\2\u008f\u0087\3\2\2\2\u008f\u008a\3\2\2\2\u008f\u008b\3\2\2\2\u0090"+
		"\u0096\3\2\2\2\u0091\u0092\f\5\2\2\u0092\u0093\t\2\2\2\u0093\u0095\5&"+
		"\24\6\u0094\u0091\3\2\2\2\u0095\u0098\3\2\2\2\u0096\u0094\3\2\2\2\u0096"+
		"\u0097\3\2\2\2\u0097\'\3\2\2\2\u0098\u0096\3\2\2\2\u0099\u00a3\5,\27\2"+
		"\u009a\u009b\5\60\31\2\u009b\u009c\7\30\2\2\u009c\u009d\5\60\31\2\u009d"+
		"\u00a3\3\2\2\2\u009e\u009f\7\3\2\2\u009f\u00a0\5(\25\2\u00a0\u00a1\7\4"+
		"\2\2\u00a1\u00a3\3\2\2\2\u00a2\u0099\3\2\2\2\u00a2\u009a\3\2\2\2\u00a2"+
		"\u009e\3\2\2\2\u00a3)\3\2\2\2\u00a4\u00a5\t\3\2\2\u00a5+\3\2\2\2\u00a6"+
		"\u00a9\5*\26\2\u00a7\u00a9\7\31\2\2\u00a8\u00a6\3\2\2\2\u00a8\u00a7\3"+
		"\2\2\2\u00a9-\3\2\2\2\u00aa\u00ab\7\30\2\2\u00ab\u00ac\5\60\31\2\u00ac"+
		"/\3\2\2\2\u00ad\u00ae\b\31\1\2\u00ae\u00b4\5\62\32\2\u00af\u00b0\7\3\2"+
		"\2\u00b0\u00b1\5\60\31\2\u00b1\u00b2\7\4\2\2\u00b2\u00b4\3\2\2\2\u00b3"+
		"\u00ad\3\2\2\2\u00b3\u00af\3\2\2\2\u00b4\u00bd\3\2\2\2\u00b5\u00b6\f\6"+
		"\2\2\u00b6\u00b7\t\4\2\2\u00b7\u00bc\5\60\31\7\u00b8\u00b9\f\5\2\2\u00b9"+
		"\u00ba\t\5\2\2\u00ba\u00bc\5\60\31\6\u00bb\u00b5\3\2\2\2\u00bb\u00b8\3"+
		"\2\2\2\u00bc\u00bf\3\2\2\2\u00bd\u00bb\3\2\2\2\u00bd\u00be\3\2\2\2\u00be"+
		"\61\3\2\2\2\u00bf\u00bd\3\2\2\2\u00c0\u00c1\t\6\2\2\u00c1\63\3\2\2\2\20"+
		"\668=FOV_\u008f\u0096\u00a2\u00a8\u00b3\u00bb\u00bd";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}