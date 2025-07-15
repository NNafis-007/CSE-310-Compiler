// Generated from Expr.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ExprParser}.
 */
public interface ExprListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ExprParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(ExprParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(ExprParser.StartContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(ExprParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(ExprParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#unit}.
	 * @param ctx the parse tree
	 */
	void enterUnit(ExprParser.UnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#unit}.
	 * @param ctx the parse tree
	 */
	void exitUnit(ExprParser.UnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#var_declaration}.
	 * @param ctx the parse tree
	 */
	void enterVar_declaration(ExprParser.Var_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#var_declaration}.
	 * @param ctx the parse tree
	 */
	void exitVar_declaration(ExprParser.Var_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#type_specifier}.
	 * @param ctx the parse tree
	 */
	void enterType_specifier(ExprParser.Type_specifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#type_specifier}.
	 * @param ctx the parse tree
	 */
	void exitType_specifier(ExprParser.Type_specifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#declaration_list}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration_list(ExprParser.Declaration_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#declaration_list}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration_list(ExprParser.Declaration_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#func_declaration}.
	 * @param ctx the parse tree
	 */
	void enterFunc_declaration(ExprParser.Func_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#func_declaration}.
	 * @param ctx the parse tree
	 */
	void exitFunc_declaration(ExprParser.Func_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#func_definition}.
	 * @param ctx the parse tree
	 */
	void enterFunc_definition(ExprParser.Func_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#func_definition}.
	 * @param ctx the parse tree
	 */
	void exitFunc_definition(ExprParser.Func_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#parameter_list}.
	 * @param ctx the parse tree
	 */
	void enterParameter_list(ExprParser.Parameter_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#parameter_list}.
	 * @param ctx the parse tree
	 */
	void exitParameter_list(ExprParser.Parameter_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#compound_statement}.
	 * @param ctx the parse tree
	 */
	void enterCompound_statement(ExprParser.Compound_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#compound_statement}.
	 * @param ctx the parse tree
	 */
	void exitCompound_statement(ExprParser.Compound_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#var_declaration_list}.
	 * @param ctx the parse tree
	 */
	void enterVar_declaration_list(ExprParser.Var_declaration_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#var_declaration_list}.
	 * @param ctx the parse tree
	 */
	void exitVar_declaration_list(ExprParser.Var_declaration_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#statements}.
	 * @param ctx the parse tree
	 */
	void enterStatements(ExprParser.StatementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#statements}.
	 * @param ctx the parse tree
	 */
	void exitStatements(ExprParser.StatementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(ExprParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(ExprParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#expression_statement}.
	 * @param ctx the parse tree
	 */
	void enterExpression_statement(ExprParser.Expression_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#expression_statement}.
	 * @param ctx the parse tree
	 */
	void exitExpression_statement(ExprParser.Expression_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(ExprParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(ExprParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(ExprParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(ExprParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#logic_expression}.
	 * @param ctx the parse tree
	 */
	void enterLogic_expression(ExprParser.Logic_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#logic_expression}.
	 * @param ctx the parse tree
	 */
	void exitLogic_expression(ExprParser.Logic_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#rel_expression}.
	 * @param ctx the parse tree
	 */
	void enterRel_expression(ExprParser.Rel_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#rel_expression}.
	 * @param ctx the parse tree
	 */
	void exitRel_expression(ExprParser.Rel_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#simple_expression}.
	 * @param ctx the parse tree
	 */
	void enterSimple_expression(ExprParser.Simple_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#simple_expression}.
	 * @param ctx the parse tree
	 */
	void exitSimple_expression(ExprParser.Simple_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(ExprParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(ExprParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#unary_expression}.
	 * @param ctx the parse tree
	 */
	void enterUnary_expression(ExprParser.Unary_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#unary_expression}.
	 * @param ctx the parse tree
	 */
	void exitUnary_expression(ExprParser.Unary_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactor(ExprParser.FactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactor(ExprParser.FactorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#argument_list}.
	 * @param ctx the parse tree
	 */
	void enterArgument_list(ExprParser.Argument_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#argument_list}.
	 * @param ctx the parse tree
	 */
	void exitArgument_list(ExprParser.Argument_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterArguments(ExprParser.ArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitArguments(ExprParser.ArgumentsContext ctx);
}