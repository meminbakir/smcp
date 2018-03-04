// Generated from KineticLawEval.g4 by ANTLR 4.5
package mtopology;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link KineticLawEvalParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface KineticLawEvalVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link KineticLawEvalParser#eval}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEval(KineticLawEvalParser.EvalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Parenthesis}
	 * labeled alternative in {@link KineticLawEvalParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesis(KineticLawEvalParser.ParenthesisContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NoOperator}
	 * labeled alternative in {@link KineticLawEvalParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoOperator(KineticLawEvalParser.NoOperatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MultiplyDivide}
	 * labeled alternative in {@link KineticLawEvalParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplyDivide(KineticLawEvalParser.MultiplyDivideContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddSubtract}
	 * labeled alternative in {@link KineticLawEvalParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSubtract(KineticLawEvalParser.AddSubtractContext ctx);
	/**
	 * Visit a parse tree produced by {@link KineticLawEvalParser#numeric}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumeric(KineticLawEvalParser.NumericContext ctx);
}