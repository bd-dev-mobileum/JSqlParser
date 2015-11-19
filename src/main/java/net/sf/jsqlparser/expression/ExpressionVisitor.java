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

import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Modulo;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.expression.operators.relational.RangeOperators;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sf.jsqlparser.expression.operators.relational.RegExpMySQLOperator;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;

public interface ExpressionVisitor<R, C>
{

    R visit(NullValue nullValue, C context);

    R visit(BooleanValue booleanValue, C context);

    R visit(ObjectValue booleanValue, C context);
    
    R visit(Function function, C context);

    R visit(SignedExpression signedExpression, C context);

    R visit(JdbcParameter jdbcParameter, C context);

    R visit(JdbcNamedParameter jdbcNamedParameter, C context);

    R visit(DoubleValue doubleValue, C context);

    R visit(LongValue longValue, C context);

    R visit(DateValue dateValue, C context);

    R visit(TimeValue timeValue, C context);

    R visit(TimestampValue timestampValue, C context);

    R visit(Parenthesis parenthesis, C context);

    R visit(StringValue stringValue, C context);

    R visit(Addition addition, C context);

    R visit(Division division, C context);

    R visit(Multiplication multiplication, C context);

    R visit(Subtraction subtraction, C context);

    R visit(AndExpression andExpression, C context);

    R visit(OrExpression orExpression, C context);

    R visit(Between between, C context);

    R visit(EqualsTo equalsTo, C context);

    R visit(GreaterThan greaterThan, C context);

    R visit(GreaterThanEquals greaterThanEquals, C context);

    R visit(InExpression inExpression, C context);

    R visit(IsNullExpression isNullExpression, C context);

    R visit(LikeExpression likeExpression, C context);

    R visit(MinorThan minorThan, C context);

    R visit(MinorThanEquals minorThanEquals, C context);

    R visit(NotEqualsTo notEqualsTo, C context);

    R visit(Column tableColumn, C context);

    R visit(SubSelect subSelect, C context);

    R visit(CaseExpression caseExpression, C context);

    R visit(WhenClause whenClause, C context);

    R visit(ExistsExpression existsExpression, C context);

    R visit(AllComparisonExpression allComparisonExpression, C context);

    R visit(AnyComparisonExpression anyComparisonExpression, C context);

    R visit(Concat concat, C context);

    R visit(Matches matches, C context);

    R visit(BitwiseAnd bitwiseAnd, C context);

    R visit(BitwiseOr bitwiseOr, C context);

    R visit(BitwiseXor bitwiseXor, C context);

    R visit(CastExpression cast, C context);

    R visit(Modulo modulo, C context);

    R visit(AnalyticExpression aexpr, C context);

    R visit(ExtractExpression eexpr, C context);

    R visit(IntervalExpression iexpr, C context);

    R visit(OracleHierarchicalExpression oexpr, C context);

    R visit(RegExpMatchOperator rexpr, C context);

    R visit(JsonExpression jsonExpr, C context);

    R visit(RegExpMySQLOperator regExpMySQLOperator, C context);

    R visit(RangeOperators rangeOperators, C context);
}
