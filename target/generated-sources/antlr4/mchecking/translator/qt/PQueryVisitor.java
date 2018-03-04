// Generated from PQuery.g4 by ANTLR 4.5
package mchecking.translator.qt;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PQueryParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PQueryVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PQueryParser#pQuery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPQuery(PQueryParser.PQueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link PQueryParser#inequalityQuery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInequalityQuery(PQueryParser.InequalityQueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link PQueryParser#questionMark}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuestionMark(PQueryParser.QuestionMarkContext ctx);
	/**
	 * Visit a parse tree produced by {@link PQueryParser#query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery(PQueryParser.QueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link PQueryParser#unaryPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryPattern(PQueryParser.UnaryPatternContext ctx);
	/**
	 * Visit a parse tree produced by {@link PQueryParser#binaryPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryPattern(PQueryParser.BinaryPatternContext ctx);
	/**
	 * Visit a parse tree produced by {@link PQueryParser#notPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotPattern(PQueryParser.NotPatternContext ctx);
	/**
	 * Visit a parse tree produced by {@link PQueryParser#eventually}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEventually(PQueryParser.EventuallyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PQueryParser#always}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlways(PQueryParser.AlwaysContext ctx);
	/**
	 * Visit a parse tree produced by {@link PQueryParser#next}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNext(PQueryParser.NextContext ctx);
	/**
	 * Visit a parse tree produced by {@link PQueryParser#never}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNever(PQueryParser.NeverContext ctx);
	/**
	 * Visit a parse tree produced by {@link PQueryParser#infinitelyOften}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfinitelyOften(PQueryParser.InfinitelyOftenContext ctx);
	/**
	 * Visit a parse tree produced by {@link PQueryParser#steadyState}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSteadyState(PQueryParser.SteadyStateContext ctx);
	/**
	 * Visit a parse tree produced by {@link PQueryParser#until}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUntil(PQueryParser.UntilContext ctx);
	/**
	 * Visit a parse tree produced by {@link PQueryParser#weakUntil}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWeakUntil(PQueryParser.WeakUntilContext ctx);
	/**
	 * Visit a parse tree produced by {@link PQueryParser#release}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelease(PQueryParser.ReleaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PQueryParser#follows}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFollows(PQueryParser.FollowsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PQueryParser#precedes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrecedes(PQueryParser.PrecedesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code binaryLogicalExpr}
	 * labeled alternative in {@link PQueryParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryLogicalExpr(PQueryParser.BinaryLogicalExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code notExpr}
	 * labeled alternative in {@link PQueryParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpr(PQueryParser.NotExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code noParanBoolExpr}
	 * labeled alternative in {@link PQueryParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoParanBoolExpr(PQueryParser.NoParanBoolExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code paranthesesExpr}
	 * labeled alternative in {@link PQueryParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParanthesesExpr(PQueryParser.ParanthesesExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link PQueryParser#boolExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolExpr(PQueryParser.BoolExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link PQueryParser#logicalConstant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalConstant(PQueryParser.LogicalConstantContext ctx);
	/**
	 * Visit a parse tree produced by {@link PQueryParser#logicalEntity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalEntity(PQueryParser.LogicalEntityContext ctx);
	/**
	 * Visit a parse tree produced by {@link PQueryParser#probability}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProbability(PQueryParser.ProbabilityContext ctx);
	/**
	 * Visit a parse tree produced by the {@code noOpNumericExpr}
	 * labeled alternative in {@link PQueryParser#numericExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoOpNumericExpr(PQueryParser.NoOpNumericExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addNumericExpr}
	 * labeled alternative in {@link PQueryParser#numericExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddNumericExpr(PQueryParser.AddNumericExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code multNumericExpr}
	 * labeled alternative in {@link PQueryParser#numericExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultNumericExpr(PQueryParser.MultNumericExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parensNumericExpr}
	 * labeled alternative in {@link PQueryParser#numericExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParensNumericExpr(PQueryParser.ParensNumericExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link PQueryParser#numeric}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumeric(PQueryParser.NumericContext ctx);
}