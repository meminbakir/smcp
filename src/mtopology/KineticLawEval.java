package mtopology;

import java.util.HashMap;

import org.antlr.v4.runtime.tree.TerminalNode;

import mtopology.KineticLawEvalParser.AddSubtractContext;
import mtopology.KineticLawEvalParser.EvalContext;
import mtopology.KineticLawEvalParser.MultiplyDivideContext;
import mtopology.KineticLawEvalParser.NoOperatorContext;
import mtopology.KineticLawEvalParser.NumericContext;
import mtopology.KineticLawEvalParser.ParenthesisContext;
/**
 * 
 */

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class KineticLawEval extends KineticLawEvalBaseVisitor<Double> {

	private HashMap<String, Double> variables = new HashMap<String, Double>();

	/**
	 * @return the variables
	 */
	public HashMap<String, Double> getVariables() {
		return variables;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * KineticLawEvalBaseVisitor#visitEval(KineticLawEvalParser.EvalContext)
	 */
	@Override
	public Double visitEval(EvalContext ctx) {
		// TODO Auto-generated method stub
		return super.visitEval(ctx);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see KineticLawEvalBaseVisitor#visitParenthesis(KineticLawEvalParser.
	 * ParenthesisContext)
	 */
	@Override
	public Double visitParenthesis(ParenthesisContext ctx) {
		return visit(ctx.expression());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see KineticLawEvalBaseVisitor#visitNoOperator(KineticLawEvalParser.
	 * NoOperatorContext)
	 */
	@Override
	public Double visitNoOperator(NoOperatorContext ctx) {
		// TODO Auto-generated method stub
		return super.visitNoOperator(ctx);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see KineticLawEvalBaseVisitor#visitMultiplyDivide(KineticLawEvalParser.
	 * MultiplyDivideContext)
	 */
	@Override
	public Double visitMultiplyDivide(MultiplyDivideContext ctx) {
		Double left = visit(ctx.expression(0));
		Double right = visit(ctx.expression(1));
		if (ctx.operator.getType() == KineticLawEvalParser.MUL) {
			return left * right;
		} else if (ctx.operator.getType() == KineticLawEvalParser.DIV) {
			return left / right;
		} else if (ctx.operator.getType() == KineticLawEvalParser.MOD) {
			return left % right;
		}
		else
			return 1.0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see KineticLawEvalBaseVisitor#visitAddSubtract(KineticLawEvalParser.
	 * AddSubtractContext)
	 */
	@Override
	public Double visitAddSubtract(AddSubtractContext ctx) {
		Double left = visit(ctx.expression(0));
		Double right = visit(ctx.expression(1));
		if (ctx.operator.getType() == KineticLawEvalParser.ADD) {
			return left + right;
		} else if (ctx.operator.getType() == KineticLawEvalParser.SUB) {
			return left - right;
		}
		else 
			return 1.0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * KineticLawEvalBaseVisitor#visitNumeric(KineticLawEvalParser.NumericContext
	 * )
	 */
	@Override
	public Double visitNumeric(NumericContext ctx) {
		TerminalNode numeric = ctx.IDENTIFIER();
		if (numeric!=null) {
			if (variables.containsKey(numeric.getText()))
				return variables.get(numeric.getText());
			else
				return 1.0;
		} else {
			numeric = ctx.NUMBER();
			return Double.valueOf(numeric.getText());
		}
	}

}
