// Generated from C2105007Parser.g4 by ANTLR 4.13.2

import java.io.BufferedWriter;
import java.io.IOException;
import SymbolTable2.SymbolInfo2;
import java.util.Arrays;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link C2105007Parser}.
 */
public interface C2105007ParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(C2105007Parser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(C2105007Parser.StartContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(C2105007Parser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(C2105007Parser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#unit}.
	 * @param ctx the parse tree
	 */
	void enterUnit(C2105007Parser.UnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#unit}.
	 * @param ctx the parse tree
	 */
	void exitUnit(C2105007Parser.UnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#func_declaration}.
	 * @param ctx the parse tree
	 */
	void enterFunc_declaration(C2105007Parser.Func_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#func_declaration}.
	 * @param ctx the parse tree
	 */
	void exitFunc_declaration(C2105007Parser.Func_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#func_definition}.
	 * @param ctx the parse tree
	 */
	void enterFunc_definition(C2105007Parser.Func_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#func_definition}.
	 * @param ctx the parse tree
	 */
	void exitFunc_definition(C2105007Parser.Func_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#parameter_list}.
	 * @param ctx the parse tree
	 */
	void enterParameter_list(C2105007Parser.Parameter_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#parameter_list}.
	 * @param ctx the parse tree
	 */
	void exitParameter_list(C2105007Parser.Parameter_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#compound_statement}.
	 * @param ctx the parse tree
	 */
	void enterCompound_statement(C2105007Parser.Compound_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#compound_statement}.
	 * @param ctx the parse tree
	 */
	void exitCompound_statement(C2105007Parser.Compound_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#var_declaration}.
	 * @param ctx the parse tree
	 */
	void enterVar_declaration(C2105007Parser.Var_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#var_declaration}.
	 * @param ctx the parse tree
	 */
	void exitVar_declaration(C2105007Parser.Var_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#declaration_list_err}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration_list_err(C2105007Parser.Declaration_list_errContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#declaration_list_err}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration_list_err(C2105007Parser.Declaration_list_errContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#type_specifier}.
	 * @param ctx the parse tree
	 */
	void enterType_specifier(C2105007Parser.Type_specifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#type_specifier}.
	 * @param ctx the parse tree
	 */
	void exitType_specifier(C2105007Parser.Type_specifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#declaration_list}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration_list(C2105007Parser.Declaration_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#declaration_list}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration_list(C2105007Parser.Declaration_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#statements}.
	 * @param ctx the parse tree
	 */
	void enterStatements(C2105007Parser.StatementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#statements}.
	 * @param ctx the parse tree
	 */
	void exitStatements(C2105007Parser.StatementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(C2105007Parser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(C2105007Parser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#expression_statement}.
	 * @param ctx the parse tree
	 */
	void enterExpression_statement(C2105007Parser.Expression_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#expression_statement}.
	 * @param ctx the parse tree
	 */
	void exitExpression_statement(C2105007Parser.Expression_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(C2105007Parser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(C2105007Parser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(C2105007Parser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(C2105007Parser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#logic_expression}.
	 * @param ctx the parse tree
	 */
	void enterLogic_expression(C2105007Parser.Logic_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#logic_expression}.
	 * @param ctx the parse tree
	 */
	void exitLogic_expression(C2105007Parser.Logic_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#rel_expression}.
	 * @param ctx the parse tree
	 */
	void enterRel_expression(C2105007Parser.Rel_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#rel_expression}.
	 * @param ctx the parse tree
	 */
	void exitRel_expression(C2105007Parser.Rel_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#simple_expression}.
	 * @param ctx the parse tree
	 */
	void enterSimple_expression(C2105007Parser.Simple_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#simple_expression}.
	 * @param ctx the parse tree
	 */
	void exitSimple_expression(C2105007Parser.Simple_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(C2105007Parser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(C2105007Parser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#unary_expression}.
	 * @param ctx the parse tree
	 */
	void enterUnary_expression(C2105007Parser.Unary_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#unary_expression}.
	 * @param ctx the parse tree
	 */
	void exitUnary_expression(C2105007Parser.Unary_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactor(C2105007Parser.FactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactor(C2105007Parser.FactorContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#argument_list}.
	 * @param ctx the parse tree
	 */
	void enterArgument_list(C2105007Parser.Argument_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#argument_list}.
	 * @param ctx the parse tree
	 */
	void exitArgument_list(C2105007Parser.Argument_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterArguments(C2105007Parser.ArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitArguments(C2105007Parser.ArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#non_params}.
	 * @param ctx the parse tree
	 */
	void enterNon_params(C2105007Parser.Non_paramsContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#non_params}.
	 * @param ctx the parse tree
	 */
	void exitNon_params(C2105007Parser.Non_paramsContext ctx);
	/**
	 * Enter a parse tree produced by {@link C2105007Parser#invalid_char}.
	 * @param ctx the parse tree
	 */
	void enterInvalid_char(C2105007Parser.Invalid_charContext ctx);
	/**
	 * Exit a parse tree produced by {@link C2105007Parser#invalid_char}.
	 * @param ctx the parse tree
	 */
	void exitInvalid_char(C2105007Parser.Invalid_charContext ctx);
}