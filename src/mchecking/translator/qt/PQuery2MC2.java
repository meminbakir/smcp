package mchecking.translator.qt;

import java.util.ArrayList;
import java.util.List;

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
public class PQuery2MC2 extends MCQuery {
	private List<String> speciesList = new ArrayList<String>();
	private boolean isFollows = false; // if isFollows always P>=1
	public PQuery2MC2(ModelChecker targetMC) {
		super(targetMC);
	}

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

		if (isFollows) { //if isFollows always P>=1
			note = "MC2 ignores the specified probability interval '" + prob
					+ "'for response because always P >=1 and inner P cannot be represented.";
			prob = " >=1";
		}
		if (isVerifiable) {
			result = "P ";
			result += prob + " [";
			result += query;
			result += "]";
		} else {
			// This Patterns are not supported
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
		String result = "";
		String query = visit(ctx.query());

		if (isVerifiable) {
			result = "P =? [" + query + "]";
		} else {
			// This Patterns are not supported
			result = query;
		}

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
		String notSign = "\u00AC"; // this is unicode of not sign (¬) , otherwise different OS acts weird
		String result = notSign + "(";
		QueryContext query = ctx.query();
		if (query != null)
			result += visit(query);
		result += ")";
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitAlways(mchecking.translator .qt.PQueryParser.AlwaysContext)
	 */
	@Override
	public String visitAlways(AlwaysContext ctx) {
		String result = "G(";
		ExprContext exp = ctx.expr();
		if (exp != null)
			result += visit(exp);
		result += ")";
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
		String result = "F(";
		ExprContext exp = ctx.expr();
		if (exp != null)
			result += visit(exp);
		result += ")";
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitNext(mchecking.translator .qt.PQueryParser.NextContext)
	 */
	@Override
	public String visitNext(NextContext ctx) {
		String result = "X(";
		ExprContext exp = ctx.expr();
		if (exp != null)
			result += visit(exp);
		result += ")";
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitNever(mchecking.translator .qt.PQueryParser.NeverContext)
	 */
	@Override
	public String visitNever(NeverContext ctx) {
		String notSign = "\u00AC"; // this is unicode of not sign (¬) , otherwise different OS acts weird
		String result = notSign + "(F(";
		ExprContext exp = ctx.expr();
		if (exp != null)
			result += visit(exp);
		result += "))";
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
		String result = "G(F(";
		ExprContext exp = ctx.expr();
		if (exp != null)
			result += visit(exp);
		result += "))";
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitSteadyState(mchecking.
	 * translator.qt.PQueryParser.SteadyStateContext)
	 */
	@Override
	public String visitSteadyState(SteadyStateContext ctx) {
		String result = "FG(";
		ExprContext exp = ctx.expr();
		if (exp != null)
			result += visit(exp);
		result += ")";
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitUntil(mchecking.translator .qt.PQueryParser.UntilContext)
	 */
	@Override
	public String visitUntil(UntilContext ctx) {
		List<ExprContext> exprs = ctx.expr();
		String a = visit(exprs.get(0));
		String b = visit(exprs.get(1));
		String result = "(" + a + ") U (" + b + ")";
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitWeakUntil(mchecking.translator .qt.PQueryParser.WeakUntilContext)
	 */
	@Override
	public String visitWeakUntil(WeakUntilContext ctx) {
		List<ExprContext> exprs = ctx.expr();
		String a = visit(exprs.get(0));
		String b = visit(exprs.get(1));
		// String result = "(" + b + ") R ((" + b + ") V " + a + ")";
		String result = "((" + a + ") U (" + b + ")) V (G(" + a + "))";
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitRelease(mchecking.translator .qt.PQueryParser.ReleaseContext)
	 */
	@Override
	public String visitRelease(ReleaseContext ctx) {
		List<ExprContext> exprs = ctx.expr();
		String a = visit(exprs.get(0));
		String b = visit(exprs.get(1));
		String result = "(" + a + ") R (" + b + ")";
		return result;
	}

	/**
	 * second(b) follows first(a) is translated to G(first -> F second)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitFollowsBy(mchecking. translator.qt.PQueryParser.FollowsContext)
	 */
	@Override
	public String visitFollows(FollowsContext ctx) {
		isFollows = true;// if it is follows then always P>=1
		List<ExprContext> exprs = ctx.expr();
		String second = visit(exprs.get(0));
		String first = visit(exprs.get(1));
		// translated to G(first -> F second)
		String result = "G((" + first + ") => F(" + second + "))";
		return result;
	}

	/**
	 * Precedes a precedes b is (!b W a) which is !b U a V G!b
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitPrecedes(mchecking. translator.qt.PQueryParser.PrecedesContext)
	 */
	@Override
	public String visitPrecedes(PrecedesContext ctx) {
		List<ExprContext> exprs = ctx.expr();
		String a = visit(exprs.get(0));
		String b = visit(exprs.get(1));
		String notSign = "\u00AC"; // this is unicode of not sign (¬) , otherwise different OS acts weird
		// a precedes b is (!b W a) = (!b U a) V (G!b)
		String result = "((" + notSign + "(" + b + ")) U (" + a + ")) V (G(" + notSign + "(" + b + ")))";
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
			result = left + " ^ " + right;
		} else if (ctx.operator.getType() == PQueryParser.OR) {
			result = left + " V " + right;
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
		String notSign = "\u00AC"; // this is unicode of not sign (¬) , otherwise different OS acts weird
		String result = "(" + notSign + "(" + visit(ctx.expr()) + "))";
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
			// if prism used as simulator, then we need to check the keywords
			result = targetMC.isUsesPrism() ? TranslatorUtil.checkKeyword(visit(ctx.numericExp().get(0)))
					: visit(ctx.numericExp().get(0));
			result += ctx.RELATION();
			result += targetMC.isUsesPrism() ? visit(ctx.numericExp().get(1))
					: TranslatorUtil.checkKeyword(visit(ctx.numericExp().get(1)));
		} else // Parentheses
		{
			result = "(" + ctx.getText() + ")";
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
			result = targetMC.isUsesPrism() ? TranslatorUtil.checkKeyword(ctx.IDENTIFIER().getText())
					: ctx.IDENTIFIER().getText();
		}
		return result;
	}

	@Override
	public String visitLogicalConstant(LogicalConstantContext ctx) {
		String result = "";
		TerminalNode tr = ctx.TRUE();
		TerminalNode fals = ctx.FALSE();
		if (tr != null) {
			result = "true";
		} else if (fals != null) {
			result = "false";
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
		String result = "";
		TerminalNode identifier = ctx.IDENTIFIER();
		if (identifier != null) {
			String species = ctx.getText();
			speciesList.add(species);
			species = targetMC.isUsesPrism() ? TranslatorUtil.checkKeyword(species) : species;
			result = "[" + species + "]";
		} else
			// number
			result = targetMC.isUsesPrism() ? ctx.getText() : TranslatorUtil.checkKeyword(ctx.getText());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mce.antlr.test.pquery.PQueryBaseVisitor#visitNoOpNumericExpr(mce.antlr
	 * .test.pquery.PQueryParser.NoOpNumericExprContext)
	 */
	@Override
	public String visitNoOpNumericExpr(NoOpNumericExprContext ctx) {
		return visit(ctx.numeric());
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
			log.error("Conversion error, operator should be either '*' or '/'");
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

	/**
	 * Keeps list of species in query, It will be used for generating only related species in simulation.
	 * 
	 * @return the species
	 */
	public List<String> getSpecies() {
		return speciesList;
	}

}
