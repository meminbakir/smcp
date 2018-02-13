package mchecking.translator.qt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.TerminalNode;

import mchecking.ModelChecker;
import mchecking.translator.TranslatorUtil;
import mchecking.translator.qt.PQueryParser.AddNumericExprContext;
import mchecking.translator.qt.PQueryParser.AlwaysContext;
import mchecking.translator.qt.PQueryParser.BinaryLogicalExprContext;
import mchecking.translator.qt.PQueryParser.BinaryPatternContext;
import mchecking.translator.qt.PQueryParser.BoolExprContext;
import mchecking.translator.qt.PQueryParser.EventuallyContext;
import mchecking.translator.qt.PQueryParser.ExprContext;
import mchecking.translator.qt.PQueryParser.FollowsContext;
import mchecking.translator.qt.PQueryParser.InequalityQueryContext;
import mchecking.translator.qt.PQueryParser.InfinitelyOftenContext;
import mchecking.translator.qt.PQueryParser.LogicalConstantContext;
import mchecking.translator.qt.PQueryParser.LogicalEntityContext;
import mchecking.translator.qt.PQueryParser.MultNumericExprContext;
import mchecking.translator.qt.PQueryParser.NeverContext;
import mchecking.translator.qt.PQueryParser.NextContext;
import mchecking.translator.qt.PQueryParser.NoOpNumericExprContext;
import mchecking.translator.qt.PQueryParser.NoParanBoolExprContext;
import mchecking.translator.qt.PQueryParser.NotExprContext;
import mchecking.translator.qt.PQueryParser.NotPatternContext;
import mchecking.translator.qt.PQueryParser.NumericExpContext;
import mchecking.translator.qt.PQueryParser.ParanthesesExprContext;
import mchecking.translator.qt.PQueryParser.ParensNumericExprContext;
import mchecking.translator.qt.PQueryParser.PrecedesContext;
import mchecking.translator.qt.PQueryParser.ProbabilityContext;
import mchecking.translator.qt.PQueryParser.QueryContext;
import mchecking.translator.qt.PQueryParser.QuestionMarkContext;
import mchecking.translator.qt.PQueryParser.ReleaseContext;
import mchecking.translator.qt.PQueryParser.SteadyStateContext;
import mchecking.translator.qt.PQueryParser.UnaryPatternContext;
import mchecking.translator.qt.PQueryParser.UntilContext;
import mchecking.translator.qt.PQueryParser.WeakUntilContext;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class PQuery2MRMC extends MCQuery {
	private Map<String, String> boolExprs = new HashMap<String, String>();
	private int counter = 1;// boolExpr(prop) counter
	private String prop = "prop";// represents boolean expressions in mrmc
	private boolean isPrecedes;
	private boolean isNotPattern;
	private boolean isFollows;
	private boolean isRelease;
	private boolean isWeakUntil;
	private boolean steadyState;
	private boolean isNever;
	private boolean isAlways;
	private boolean isEventually;
	private boolean isNext;
	private boolean isUntil;

	public PQuery2MRMC(ModelChecker targetMC) {
		super(targetMC);
	}

	/**
	 * @return the boolExprs
	 */
	public Map<String, String> getBoolExprs() {
		return boolExprs;
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// * mchecking.translator.qt.PQueryBaseVisitor#visitPQuery(mchecking.translator
	// * .qt.PQueryParser.PQueryContext)
	// */
	// @Override
	// public String visitPQuery(PQueryContext ctx) {
	// String result = "";
	// for (int i = 0; i < ctx.getChildCount(); i++) {
	// result = null;
	// ParseTree ineqOrQues = ctx.getChild(i);
	//// if (ineqOrQues instanceof InequalityQueryContext) {
	//// result = visit(ineqOrQues);
	//// } else if (ineqOrQues instanceof QuestionMarkContext) {
	// result = visit(ineqOrQues);
	//// }
	// }
	// this.setTranslatedQuery(result);
	// return result;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitInequalityQuery(mchecking
	 * .translator.qt.PQueryParser.InequalityQueryContext)
	 */
	@Override
	public String visitInequalityQuery(InequalityQueryContext ctx) {
		String result = "";
		String prob = visit(ctx.probability());
		String query = visit(ctx.query());

		if (isVerifiable) {
			if (isAlways) {
				result = query;
			}
			if (isEventually || isNext || isUntil) {
				result = "P {" + prob + "} [" + query + "]";
			}
			if (isNever) {
				result = "!(" + "P {" + prob + "} [" + query + "])";
			}
			if (steadyState) {
				result += "S {" + prob + "} [" + query + "]";
			}
			if (isWeakUntil) {
				// P{>=0.5}[a U b] || a
				result = "P {" + prob + "} [" + query;
			}
			if (isRelease) {
				// (a R b), is equivalent to !(!a U !b)
				result = "!(" + "P {" + prob + "} [" + query + "])";
			}
			if (isFollows) {
				// return a=> tt U b
				String[] follows = query.split("=>");
				result = follows[0] + " => " + "P {" + prob + "} [" + follows[1] + "]";

			}
			if (isPrecedes) {// precedence adds ']', should not duplicate
				result = "P {" + prob + "} [" + query;
			}
			// if it is a negation pattern, then get query and negate it
			if (isNotPattern) {

				result = "!(" + result + ")";
			}
		} else {
			result = query;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitQuestionMark(mchecking
	 * .translator.qt.PQueryParser.QuestionMarkContext)
	 */
	@Override
	public String visitQuestionMark(QuestionMarkContext ctx) {
		isVerifiable = false;
		String result = "'what is the probability of'(=?) operator is not supported by MRMC";
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitQuery(mchecking.translator .qt.PQueryParser.QueryContext)
	 */
	@Override
	public String visitQuery(QueryContext ctx) {
		String result = "";
		UnaryPatternContext unaryPattern = ctx.unaryPattern();
		BinaryPatternContext binaryPattern = ctx.binaryPattern();
		if (unaryPattern != null) {
			result = visit(unaryPattern);
		} else if (binaryPattern != null) {
			result = visit(binaryPattern);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitUnaryPattern(mchecking
	 * .translator.qt.PQueryParser.UnaryPatternContext)
	 */
	@Override
	public String visitUnaryPattern(UnaryPatternContext ctx) {
		String result = "";
		NotPatternContext notPattern = ctx.notPattern();
		EventuallyContext eventually = ctx.eventually();
		AlwaysContext always = ctx.always();
		NextContext next = ctx.next();
		NeverContext never = ctx.never();
		InfinitelyOftenContext infinitelyOften = ctx.infinitelyOften();
		SteadyStateContext steadyState = ctx.steadyState();

		if (notPattern != null) {
			result = visit(notPattern);
		} else if (eventually != null) {
			result = visit(eventually);
		} else if (always != null) {
			result = visit(always);
		} else if (next != null) {
			result = visit(next);
		} else if (never != null) {
			result = visit(never);
		} else if (infinitelyOften != null) {
			result = visit(infinitelyOften);
		} else if (steadyState != null) {
			result = visit(steadyState);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitBinaryPattern(mchecking
	 * .translator.qt.PQueryParser.BinaryPatternContext)
	 */
	@Override
	public String visitBinaryPattern(BinaryPatternContext ctx) {
		String result = "";
		UntilContext until = ctx.until();
		WeakUntilContext weakUntil = ctx.weakUntil();
		ReleaseContext release = ctx.release();
		FollowsContext follows = ctx.follows();
		PrecedesContext precedes = ctx.precedes();

		if (until != null) {
			result = visit(until);
		} else if (weakUntil != null) {
			result = visit(weakUntil);
		} else if (release != null) {
			result = visit(release);
		} else if (follows != null) {
			result = visit(follows);
		} else if (precedes != null) {
			result = visit(precedes);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitNotPattern(mchecking.
	 * translator.qt.PQueryParser.NotPatternContext)
	 */
	@Override
	public String visitNotPattern(NotPatternContext ctx) {
		// String result = "!(";
		// QueryContext query = ctx.query();
		// if (query != null)
		// result += visit(query);
		// result += ")";
		// return result;
		isNotPattern = true;
		return visit(ctx.query());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitAlways(mchecking.translator .qt.PQueryParser.AlwaysContext)
	 */
	@Override
	public String visitAlways(AlwaysContext ctx) {
		isAlways = true;
		String result = "";
		ExprContext exp = ctx.expr();
		if (exp != null)
			result += visit(exp);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitEventually(mchecking.
	 * translator.qt.PQueryParser.EventuallyContext)
	 */
	@Override
	public String visitEventually(EventuallyContext ctx) {
		isEventually = true;
		String result = "tt U ";
		ExprContext exp = ctx.expr();
		if (exp != null)
			result += visit(exp);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitNext(mchecking.translator .qt.PQueryParser.NextContext)
	 */
	@Override
	public String visitNext(NextContext ctx) {
		isNext = true;
		String result = "X ";
		ExprContext exp = ctx.expr();
		if (exp != null)
			result += visit(exp);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitNever(mchecking.translator .qt.PQueryParser.NeverContext)
	 */
	@Override
	public String visitNever(NeverContext ctx) {
		isNever = true;
		String result = "tt U ";
		ExprContext exp = ctx.expr();
		if (exp != null)
			result += visit(exp);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitInfinitelyOften(mchecking
	 * .translator.qt.PQueryParser.InfinitelyOftenContext)
	 */
	@Override
	public String visitInfinitelyOften(InfinitelyOftenContext ctx) {
		isVerifiable = false;
		return "InfinitelyOften pattern is not possible to be represented by MRMC";
	}

	/*
	 * S{>=0.4}[prop1] (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitSteadyState(mchecking.
	 * translator.qt.PQueryParser.SteadyStateContext)
	 */
	@Override
	public String visitSteadyState(SteadyStateContext ctx) {
		steadyState = true;
		return visit(ctx.expr());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitUntil(mchecking.translator .qt.PQueryParser.UntilContext)
	 */
	@Override
	public String visitUntil(UntilContext ctx) {
		isUntil = true;
		String result = "";
		List<ExprContext> exprs = ctx.expr();
		result += visit(exprs.get(0));
		result += " U ";
		result += visit(exprs.get(1));
		return result;
	}

	/*
	 * P{>=0.5}[a U b] || a (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitWeakUntil(mchecking.translator .qt.PQueryParser.WeakUntilContext)
	 */
	@Override
	public String visitWeakUntil(WeakUntilContext ctx) {
		isWeakUntil = true;
		List<ExprContext> exprs = ctx.expr();
		String a = visit(exprs.get(0));
		String b = visit(exprs.get(1));
		// a U b] || a
		String result = a + " U " + b + "] || " + a;
		return result;
	}

	/*
	 * (a R b), is equivalent to !(!a U !b) (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitRelease(mchecking.translator .qt.PQueryParser.ReleaseContext)
	 */
	@Override
	public String visitRelease(ReleaseContext ctx) {
		isRelease = true;
		String result = "";
		List<ExprContext> exprs = ctx.expr();
		result += "!(" + visit(exprs.get(0)) + ")";
		result += " U ";
		result += "!(" + visit(exprs.get(1)) + ")";
		return result;
	}

	/**
	 * second(b) follows first(a) is translated to G(first -> F second) AG(first -> AF(second)) first =>(P{>0}[tt U second])
	 * 
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitFollows(mchecking. translator.qt.PQueryParser.FollowsContext)
	 */
	@Override
	public String visitFollows(FollowsContext ctx) {
		isFollows = true;
		List<ExprContext> exprs = ctx.expr();
		String second = visit(exprs.get(0));
		String first = visit(exprs.get(1));
		String result = first + "=>" + "tt U " + second;
		return result;
	}

	/**
	 * Precedes a precedes b is (!b W a) in MRMC P{>0.5}[!b U a] || a
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitPrecedes(mchecking. translator.qt.PQueryParser.PrecedesContext)
	 */
	@Override
	public String visitPrecedes(PrecedesContext ctx) {
		isPrecedes = true;
		List<ExprContext> exprs = ctx.expr();
		String a = visit(exprs.get(0));
		String b = visit(exprs.get(1));
		// !b U a] || a
		String result = "!(" + b + ") U " + a + "] || !(" + b + ")";
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitBinaryLogicalExpr(mchecking
	 * .translator.qt.PQueryParser.BinaryLogicalExprContext)
	 */
	@Override
	public String visitBinaryLogicalExpr(BinaryLogicalExprContext ctx) {
		String result = "";
		String left = visit(ctx.expr(0));
		String right = visit(ctx.expr(1));
		if ((ctx.operator.getType() == PQueryParser.AND)) {
			result = left + " && " + right;
		} else if (ctx.operator.getType() == PQueryParser.OR) {
			result = left + " || " + right;
		} else if (ctx.operator.getType() == PQueryParser.IMPLIES) {
			result = left + " => " + right;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitNoParanBoolExpr(mchecking
	 * .translator.qt.PQueryParser.NoParanBoolExprContext)
	 */
	@Override
	public String visitNoParanBoolExpr(NoParanBoolExprContext ctx) {
		BoolExprContext boolExpr = ctx.boolExpr();
		return visit(boolExpr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitParanthesesExpr(mchecking
	 * .translator.qt.PQueryParser.ParanthesesExprContext)
	 */
	@Override
	public String visitParanthesesExpr(ParanthesesExprContext ctx) {
		ExprContext expr = ctx.expr();
		String result = "(" + visit(expr) + ")";
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitNotExpr(mchecking.translator .qt.PQueryParser.NotExprContext)
	 */
	@Override
	public String visitNotExpr(NotExprContext ctx) {
		String result = "!" + visit(ctx.expr());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitBoolExpr(mchecking.translator .qt.PQueryParser.BoolExprContext)
	 */
	@Override
	public String visitBoolExpr(BoolExprContext ctx) {
		String result = "";
		LogicalEntityContext logicalEntity = ctx.logicalEntity();
		List<NumericExpContext> numericExps = ctx.numericExp();

		if (logicalEntity != null) {
			result = visit(logicalEntity);
		} else if (numericExps.size() == 2) {
			String boolVar = TranslatorUtil.checkKeyword(visit(ctx.numericExp().get(0)));
			boolVar += ctx.RELATION();
			boolVar += TranslatorUtil.checkKeyword(visit(ctx.numericExp().get(1)));
			// put the reference (prop1) of variable to query
			String ref2BoolVar = prop + counter;
			boolExprs.put(boolVar, ref2BoolVar);
			result = ref2BoolVar;
			counter++;

		} else // Parentheses
		{
			String boolVar = TranslatorUtil.checkKeyword(ctx.getText());
			// put the reference (prop1) of variable to query
			String ref2BoolVar = prop + counter;
			boolExprs.put(boolVar, ref2BoolVar);
			counter++;
			result = "(" + ref2BoolVar + ")";
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitLogicalEntity(mchecking
	 * .translator.qt.PQueryParser.LogicalEntityContext)
	 */
	@Override
	public String visitLogicalEntity(LogicalEntityContext ctx) {
		String result = "";
		LogicalConstantContext logicalConstant = ctx.logicalConstant();

		if (logicalConstant != null) {
			result = visit(logicalConstant);
		} else {
			String boolVar = TranslatorUtil.checkKeyword(ctx.IDENTIFIER().getText());
			// put the reference (prop1) of variable to query
			String ref2BoolVar = prop + counter;
			boolExprs.put(boolVar, ref2BoolVar);
			result = ref2BoolVar;
			counter++;
		}
		return result;
	}

	@Override
	public String visitLogicalConstant(LogicalConstantContext ctx) {
		String result = "";
		TerminalNode tt = ctx.TRUE();
		TerminalNode ff = ctx.FALSE();
		if (tt != null) {
			result = "tt";
		} else if (ff != null) {
			result = "ff";
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitProbability(mchecking.
	 * translator.qt.PQueryParser.ProbabilityContext)
	 */
	@Override
	public String visitProbability(ProbabilityContext ctx) {
		String result = "";
		result += ctx.RELATION().getText();
		result += ctx.numericExp().getText();
		return result;
	}

	@Override
	public String visitNumeric(PQueryParser.NumericContext ctx) {
		return TranslatorUtil.checkKeyword(ctx.getText());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mce.antlr.test.pquery.PQueryBaseVisitor#visitNoOpNumericExpr(mce.antlr
	 * .test.pquery.PQueryParser.NoOpNumericExprContext)
	 */
	@Override
	public String visitNoOpNumericExpr(NoOpNumericExprContext ctx) {
		return ctx.getText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mce.antlr.test.pquery.PQueryBaseVisitor#visitAddNumericExpr(mce.antlr
	 * .test.pquery.PQueryParser.AddNumericExprContext)
	 */
	@Override
	public String visitAddNumericExpr(AddNumericExprContext ctx) {
		String left = visit(ctx.numericExp(0));
		String right = visit(ctx.numericExp(1));
		if (ctx.op.getType() == PQueryParser.ADD) {
			return left + "+" + right;
		} else if (ctx.op.getType() == PQueryParser.SUB) {
			return left + "-" + right;
		} else {
			log.error("Conversion error, operator should be either '+' or '-'");
			return "conversion error check logs";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mce.antlr.test.pquery.PQueryBaseVisitor#visitMultNumericExpr(mce.antlr
	 * .test.pquery.PQueryParser.MultNumericExprContext)
	 */
	@Override
	public String visitMultNumericExpr(MultNumericExprContext ctx) {
		String left = visit(ctx.numericExp(0));
		String right = visit(ctx.numericExp(1));
		if (ctx.op.getType() == PQueryParser.MUL) {
			return left + "*" + right;
		} else if (ctx.op.getType() == PQueryParser.DIV) {
			return left + "/" + right;
		} else if (ctx.op.getType() == PQueryParser.MOD) {
			return left + "%" + right;
		} else {
			log.error("Conversion error, operator should be either '+' or '-'");
			return "conversion error check logs";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mce.antlr.test.pquery.PQueryBaseVisitor#visitParensNumericExpr(mce.antlr
	 * .test.pquery.PQueryParser.ParensNumericExprContext)
	 */
	@Override
	public String visitParensNumericExpr(ParensNumericExprContext ctx) {
		return "(" + visit(ctx.numericExp()) + ")";
	}

}
