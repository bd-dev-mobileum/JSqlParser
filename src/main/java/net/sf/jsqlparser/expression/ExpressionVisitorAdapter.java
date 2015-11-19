/*
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2013 JSQLParser
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */
package net.sf.jsqlparser.expression;

import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.SubSelect;

public class ExpressionVisitorAdapter<R,C> implements ExpressionVisitor<R,C>, ItemsListVisitor<R,C> {
    @Override
    public R visit(NullValue value,C context) {
        return null;
    }

    @Override
    public R visit(Function function,C context) {
        return null;
    }

    @Override
    public R visit(SignedExpression expr,C context) {
        return expr.getExpression().accept(this,context);

    }

    @Override
    public R visit(JdbcParameter parameter,C context) {
        return null;
    }

    @Override
    public R visit(JdbcNamedParameter parameter,C context) {
        return null;
    }

    @Override
    public R visit(DoubleValue value,C context) {
        return null;
    }

    @Override
    public R visit(LongValue value,C context) {
        return null;
    }

    @Override
    public R visit(DateValue value,C context) {
        return null;
    }

    @Override
    public R visit(TimeValue value,C context) {
        return null;
    }

    @Override
    public R visit(TimestampValue value,C context) {
        return null;
    }

    @Override
    public R visit(Parenthesis parenthesis,C context) {
        return parenthesis.getExpression().accept(this,context);
    }

    @Override
    public R visit(StringValue value,C context) {
        return null;
    }

    @Override
    public R visit(Addition expr,C context) {
        return visitBinaryExpression(expr,context);
    }

    @Override
    public R visit(Division expr,C context) {
        return visitBinaryExpression(expr,context);
    }

    @Override
    public R visit(Multiplication expr,C context) {
        return visitBinaryExpression(expr,context);
    }

    @Override
    public R visit(Subtraction expr,C context) {
        return visitBinaryExpression(expr,context);
    }

    @Override
    public R visit(AndExpression expr,C context) {
        return visitBinaryExpression(expr,context);
    }

    @Override
    public R visit(OrExpression expr,C context) {
        return visitBinaryExpression(expr,context);
    }

    @Override
    public R visit(Between expr,C context) {
        
        expr.getLeftExpression().accept(this,context);
        expr.getBetweenExpressionStart().accept(this,context);
        expr.getBetweenExpressionEnd().accept(this,context);
        return null;
    }

    @Override
    public R visit(EqualsTo expr,C context) {
        return visitBinaryExpression(expr,context);
    }

    @Override
    public R visit(GreaterThan expr,C context) {
        return visitBinaryExpression(expr,context);
    }

    @Override
    public R visit(GreaterThanEquals expr,C context) {
        return visitBinaryExpression(expr,context);
    }

    @Override
    public R visit(InExpression expr,C context) {
        expr.getLeftExpression().accept(this,context);
        expr.getLeftItemsList().accept(this,context);
        expr.getRightItemsList().accept(this,context);
        return null;
    }

    @Override
    public R visit(IsNullExpression expr,C context) {
        return expr.getLeftExpression().accept(this,context);
    }

    @Override
    public R visit(LikeExpression expr,C context) {
        return visitBinaryExpression(expr,context);
    }

    @Override
    public R visit(MinorThan expr,C context) {
        return visitBinaryExpression(expr,context);
    }

    @Override
    public R visit(MinorThanEquals expr,C context) {
        return visitBinaryExpression(expr,context);
    }

    @Override
    public R visit(NotEqualsTo expr,C context) {
        return visitBinaryExpression(expr,context);
    }

    @Override
    public R visit(Column column,C context) {  
        return null;
    }

    @Override
    public R visit(SubSelect subSelect,C context) {
        return null;
    }

    @Override
    public R visit(CaseExpression expr,C context) {
        expr.getSwitchExpression().accept(this,context);
        for (Expression x : expr.getWhenClauses()) {
            x.accept(this,context);
        }
        expr.getElseExpression().accept(this,context);
        return null;
    }

    @Override
    public R visit(WhenClause expr,C context) {
        expr.getWhenExpression().accept(this,context);
        expr.getThenExpression().accept(this,context);
        return null;
    }

    @Override
    public R visit(ExistsExpression expr,C context) {
        expr.getRightExpression().accept(this,context);
        return null;
    }

    @Override
    public R visit(AllComparisonExpression expr,C context) {
        return null;
    }

    @Override
    public R visit(AnyComparisonExpression expr,C context) {
        return null;
    }

    @Override
    public R visit(Concat expr,C context) {
        return visitBinaryExpression(expr,context);
    }

    @Override
    public R visit(Matches expr,C context) {
        return visitBinaryExpression(expr,context);
    }

    @Override
    public R visit(BitwiseAnd expr,C context) {
        return visitBinaryExpression(expr,context);
    }

    @Override
    public R visit(BitwiseOr expr,C context) {
        return visitBinaryExpression(expr,context);
    }

    @Override
    public R visit(BitwiseXor expr,C context) {
        return visitBinaryExpression(expr,context);
    }

    @Override
    public R visit(CastExpression expr,C context) {
        expr.getLeftExpression().accept(this,context);
        return null;
    }

    @Override
    public R visit(Modulo expr,C context) {
        return visitBinaryExpression(expr,context);
    }

    @Override
    public R visit(AnalyticExpression expr,C context) {
        expr.getExpression().accept(this,context);
        expr.getDefaultValue().accept(this,context);
        expr.getOffset().accept(this,context);
        for (OrderByElement element : expr.getOrderByElements()){
            element.getExpression().accept(this,context);
        }

        expr.getWindowElement().getRange().getStart().getExpression().accept(this,context);
        expr.getWindowElement().getRange().getEnd().getExpression().accept(this,context);
        expr.getWindowElement().getOffset().getExpression().accept(this,context);
        return null;
    }

    @Override
    public R visit(ExtractExpression expr,C context) {
        expr.getExpression().accept(this,context);
        return null;
    }

    @Override
    public R visit(IntervalExpression expr,C context) {
        return null;
    }

    @Override
    public R visit(OracleHierarchicalExpression expr,C context) {
        expr.getConnectExpression().accept(this,context);
        expr.getStartExpression().accept(this,context);
        return null;
    }

    @Override
    public R visit(RegExpMatchOperator expr,C context) {
        return visitBinaryExpression(expr,context);
    }

    @Override
    public R visit(ExpressionList expressionList,C context) {
        for (Expression expr : expressionList.getExpressions()) {
            expr.accept(this,context);
        }
        return null;
    }

    @Override
    public R visit(MultiExpressionList multiExprList,C context) {
        for (ExpressionList list : multiExprList.getExprList()) {
            visit(list,context);
        }
        return null;
    }

    protected R visitBinaryExpression(BinaryExpression expr,C context) {
        expr.getLeftExpression().accept(this,context);
        expr.getRightExpression().accept(this,context);
        return null;
    }

    @Override
    public R visit(JsonExpression jsonExpr,C context) {
       return visit(jsonExpr.getColumn(),context);
    }

	@Override
	public R visit(RegExpMySQLOperator expr,C context) {
		return visitBinaryExpression(expr,context);	
	}
	@Override
	public R visit(RangeOperators expr,C context) {
		return visitBinaryExpression(expr,context);
	}

    @Override
    public R visit(BooleanValue nullValue, C context)
    {
        return null;
    }
    
    @Override
    public R visit(ObjectValue value, C context)
    {
        return null;
    }
}