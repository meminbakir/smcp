package mchecking.translator.qt;

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
public class PQuery2PRISM extends MCQuery {
	public PQuery2PRISM(ModelChecker targetMC) {
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
		String result = "!(";
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
		String result = "G ";
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
		String result = "F ";
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
		String result = "!(F ";
		ExprContext exp = ctx.expr();
		if (exp != null)
			result += visit(exp);
		result += ")";
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
		return "InfinitelyOften pattern is not possible to be represented by PRISM statistical model checker specifications";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitSteadyState(mchecking.
	 * translator.qt.PQueryParser.SteadyStateContext)
	 */
	@Override
	public String visitSteadyState(SteadyStateContext ctx) {
		isVerifiable = false;
		return "SteadyState pattern is not possible to be represented by PRISM statistical model checker specifications";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitUntil(mchecking.translator .qt.PQueryParser.UntilContext)
	 */
	@Override
	public String visitUntil(UntilContext ctx) {
		String result = "";
		List<ExprContext> exprs = ctx.expr();
		result += visit(exprs.get(0));
		result += " U ";
		result += visit(exprs.get(1));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitWeakUntil(mchecking.translator .qt.PQueryParser.WeakUntilContext)
	 */
	@Override
	public String visitWeakUntil(WeakUntilContext ctx) {
		String result = "";
		List<ExprContext> exprs = ctx.expr();
		result += visit(exprs.get(0));
		result += " W ";
		result += visit(exprs.get(1));
		result += "";
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitRelease(mchecking.translator .qt.PQueryParser.ReleaseContext)
	 */
	@Override
	public String visitRelease(ReleaseContext ctx) {
		String result = "";
		List<ExprContext> exprs = ctx.expr();
		result += visit(exprs.get(0));
		result += " R ";
		result += visit(exprs.get(1));
		return result;
	}

	/**
	 * second(b) follows first(a) is translated to G(first -> F second)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitFollows(mchecking. translator.qt.PQueryParser.FollowsContext)
	 */
	@Override
	public String visitFollows(FollowsContext ctx) {
		isVerifiable = false;
		return "Follows pattern is not possible to be represented by PRISM statistical model checker specifications";
	}

	/**
	 * Precedes a precedes b is (!b W a)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitPrecedes(mchecking. translator.qt.PQueryParser.PrecedesContext)
	 */
	@Override
	public String visitPrecedes(PrecedesContext ctx) {
		String result = "!(";
		List<ExprContext> exprs = ctx.expr();
		result += visit(exprs.get(1));
		result += ") W ";
		result += visit(exprs.get(0));
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
			result = left + " & " + right;
		} else if (ctx.operator.getType() == PQueryParser.OR) {
			result = left + " | " + right;
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
			result = TranslatorUtil.checkKeyword(visit(ctx.numericExp().get(0)));
			result += ctx.RELATION();
			result += TranslatorUtil.checkKeyword(visit(ctx.numericExp().get(1)));
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
			result = TranslatorUtil.checkKeyword(ctx.IDENTIFIER().getText());
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
