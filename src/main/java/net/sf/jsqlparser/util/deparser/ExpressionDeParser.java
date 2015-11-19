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
package net.sf.jsqlparser.util.deparser;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.*;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SubSelect;

import java.util.Iterator;

/**
 * A class to de-parse (that is, tranform from JSqlParser hierarchy into a
 * string) an {@link net.sf.jsqlparser.expression.Expression}
 */
public class ExpressionDeParser
        implements ExpressionVisitor<Void, Void>, ItemsListVisitor<Void, Void>
{

    private StringBuilder buffer;
    private SelectVisitor selectVisitor;
    private boolean useBracketsInExprList = true;

    public ExpressionDeParser()
    {}

    /**
     * @param selectVisitor a SelectVisitor to de-parse SubSelects. It has to
     * share the same<br> StringBuilder as this object in order to work, as:
     *
     * <pre>
     * <code>
     * StringBuilder myBuf = new StringBuilder();
     * MySelectDeparser selectDeparser = new  MySelectDeparser();
     * selectDeparser.setBuffer(myBuf);
     * ExpressionDeParser expressionDeParser = new ExpressionDeParser(selectDeparser, myBuf);
     * </code>
     * </pre>
     *
     * @param buffer the buffer that will be filled with the expression
     */
    public ExpressionDeParser(SelectVisitor selectVisitor, StringBuilder buffer)
    {
        this.selectVisitor = selectVisitor;
        this.buffer = buffer;
    }

    public StringBuilder getBuffer()
    {
        return buffer;
    }

    public void setBuffer(StringBuilder buffer)
    {
        this.buffer = buffer;
    }

    @Override
    public Void visit(Addition addition, Void v)
    {
        visitBinaryExpression(addition, " + ");
        return null;
    }

    @Override
    public Void visit(AndExpression andExpression, Void v)
    {
        visitBinaryExpression(andExpression, " AND ");
        return null;
    }

    @Override
    public Void visit(Between between, Void v)
    {
        between.getLeftExpression().accept(this, v);
        if (between.isNot()) {
            buffer.append(" NOT");
        }

        buffer.append(" BETWEEN ");
        between.getBetweenExpressionStart().accept(this, v);
        buffer.append(" AND ");
        between.getBetweenExpressionEnd().accept(this, v);
        return null;
    }

    @Override
    public Void visit(EqualsTo equalsTo, Void v)
    {
        visitOldOracleJoinBinaryExpression(equalsTo, " = ");
        return null;
    }

    @Override
    public Void visit(Division division, Void v)
    {
        visitBinaryExpression(division, " / ");
        return null;
    }

    @Override
    public Void visit(DoubleValue doubleValue, Void v)
    {
        buffer.append(doubleValue.toString());
        return null;
    }

    public Void visitOldOracleJoinBinaryExpression(OldOracleJoinBinaryExpression expression, String operator)
    {
        if (expression.isNot()) {
            buffer.append(" NOT ");
        }
        expression.getLeftExpression().accept(this, null);
        if (expression.getOldOracleJoinSyntax() == EqualsTo.ORACLE_JOIN_RIGHT) {
            buffer.append("(+)");
        }
        buffer.append(operator);
        expression.getRightExpression().accept(this, null);
        if (expression.getOldOracleJoinSyntax() == EqualsTo.ORACLE_JOIN_LEFT) {
            buffer.append("(+)");
        }
        return null;
    }

    @Override
    public Void visit(GreaterThan greaterThan, Void v)
    {
        visitOldOracleJoinBinaryExpression(greaterThan, " > ");
        return null;
    }

    @Override
    public Void visit(GreaterThanEquals greaterThanEquals, Void v)
    {
        visitOldOracleJoinBinaryExpression(greaterThanEquals, " >= ");
        return null;
    }

    @Override
    public Void visit(InExpression inExpression, Void v)
    {
        if (inExpression.getLeftExpression() == null) {
            inExpression.getLeftItemsList().accept(this, v);
        }
        else {
            inExpression.getLeftExpression().accept(this, v);
            if (inExpression.getOldOracleJoinSyntax() == SupportsOldOracleJoinSyntax.ORACLE_JOIN_RIGHT) {
                buffer.append("(+)");
            }
        }
        if (inExpression.isNot()) {
            buffer.append(" NOT");
        }
        buffer.append(" IN ");

        inExpression.getRightItemsList().accept(this, v);
        return null;
    }

    @Override
    public Void visit(SignedExpression signedExpression, Void v)
    {
        buffer.append(signedExpression.getSign());
        signedExpression.getExpression().accept(this, v);
        return null;
    }

    @Override
    public Void visit(IsNullExpression isNullExpression, Void v)
    {
        isNullExpression.getLeftExpression().accept(this, v);
        if (isNullExpression.isNot()) {
            buffer.append(" IS NOT NULL");
        }
        else {
            buffer.append(" IS NULL");
        }
        return null;
    }

    @Override
    public Void visit(JdbcParameter jdbcParameter, Void v)
    {
        buffer.append("?");
        return null;
    }

    @Override
    public Void visit(LikeExpression likeExpression, Void v)
    {
        visitBinaryExpression(likeExpression, " LIKE ");
        String escape = likeExpression.getEscape();
        if (escape != null) {
            buffer.append(" ESCAPE '").append(escape).append('\'');
        }
        return null;
    }

    @Override
    public Void visit(ExistsExpression existsExpression, Void v)
    {
        if (existsExpression.isNot()) {
            buffer.append("NOT EXISTS ");
        }
        else {
            buffer.append("EXISTS ");
        }
        existsExpression.getRightExpression().accept(this, v);
        return null;
    }

    @Override
    public Void visit(LongValue longValue, Void v)
    {
        buffer.append(longValue.getStringValue());
        return null;
    }

    @Override
    public Void visit(MinorThan minorThan, Void v)
    {
        visitOldOracleJoinBinaryExpression(minorThan, " < ");
        return null;
    }

    @Override
    public Void visit(MinorThanEquals minorThanEquals, Void v)
    {
        visitOldOracleJoinBinaryExpression(minorThanEquals, " <= ");
        return null;
    }

    @Override
    public Void visit(Multiplication multiplication, Void v)
    {
        visitBinaryExpression(multiplication, " * ");
        return null;
    }

    @Override
    public Void visit(NotEqualsTo notEqualsTo, Void v)
    {
        visitOldOracleJoinBinaryExpression(notEqualsTo, " " + notEqualsTo.getStringExpression() + " ");
        return null;
    }

    @Override
    public Void visit(NullValue nullValue, Void v)
    {
        buffer.append("NULL");
        return null;
    }

    @Override
    public Void visit(OrExpression orExpression, Void v)
    {
        visitBinaryExpression(orExpression, " OR ");
        return null;
    }

    @Override
    public Void visit(Parenthesis parenthesis, Void v)
    {
        if (parenthesis.isNot()) {
            buffer.append(" NOT ");
        }

        buffer.append("(");
        parenthesis.getExpression().accept(this, v);
        buffer.append(")");
        return null;
    }

    @Override
    public Void visit(StringValue stringValue, Void v)
    {
        buffer.append("'").append(stringValue.getValue()).append("'");
        return null;
    }

    @Override
    public Void visit(Subtraction subtraction, Void v)
    {
        visitBinaryExpression(subtraction, " - ");
        return null;
    }

    private Void visitBinaryExpression(BinaryExpression binaryExpression, String operator)
    {
        if (binaryExpression.isNot()) {
            buffer.append(" NOT ");
        }
        binaryExpression.getLeftExpression().accept(this, null);
        buffer.append(operator);
        binaryExpression.getRightExpression().accept(this, null);

        return null;
    }

    @Override
    public Void visit(SubSelect subSelect, Void v)
    {
        buffer.append("(");
        subSelect.getSelectBody().accept(selectVisitor, v);
        buffer.append(")");
        return null;
    }

    @Override
    public Void visit(Column tableColumn, Void v)
    {
        final Table table = tableColumn.getTable();
        String tableName = null;
        if (table != null) {
            if (table.getAlias() != null) {
                tableName = table.getAlias().getName();
            }
            else {
                tableName = table.getFullyQualifiedName();
            }
        }
        if (tableName != null && !tableName.isEmpty()) {
            buffer.append(tableName).append(".");
        }

        buffer.append(tableColumn.getColumnName());
        return null;
    }

    @Override
    public Void visit(Function function, Void v)
    {
        if (function.isEscaped()) {
            buffer.append("{fn ");
        }

        buffer.append(function.getName());
        if (function.isAllColumns() && function.getParameters() == null) {
            buffer.append("(*)");
        }
        else if (function.getParameters() == null) {
            buffer.append("()");
        }
        else {
            boolean oldUseBracketsInExprList = useBracketsInExprList;
            if (function.isDistinct()) {
                useBracketsInExprList = false;
                buffer.append("(DISTINCT ");
            }
            else if (function.isAllColumns()) {
                useBracketsInExprList = false;
                buffer.append("(ALL ");
            }
            visit(function.getParameters(), v);
            useBracketsInExprList = oldUseBracketsInExprList;
            if (function.isDistinct() || function.isAllColumns()) {
                buffer.append(")");
            }
        }

        if (function.getAttribute() != null) {
            buffer.append(".").append(function.getAttribute());
        }

        if (function.isEscaped()) {
            buffer.append("}");
        }
        return null;
    }

    @Override
    public Void visit(ExpressionList expressionList, Void v)
    {
        if (useBracketsInExprList) {
            buffer.append("(");
        }
        for (Iterator<Expression> iter = expressionList.getExpressions().iterator(); iter.hasNext();) {
            Expression expression = iter.next();
            expression.accept(this, v);
            if (iter.hasNext()) {
                buffer.append(", ");
            }
        }
        if (useBracketsInExprList) {
            buffer.append(")");
        }
        return null;
    }

    public SelectVisitor getSelectVisitor()
    {
        return selectVisitor;
    }

    public void setSelectVisitor(SelectVisitor visitor)
    {
        selectVisitor = visitor;
    }

    @Override
    public Void visit(DateValue dateValue, Void v)
    {
        buffer.append("{d '").append(dateValue.getValue().toString()).append("'}");
        return null;
    }

    @Override
    public Void visit(TimestampValue timestampValue, Void v)
    {
        buffer.append("{ts '").append(timestampValue.getValue().toString()).append("'}");
        return null;
    }

    @Override
    public Void visit(TimeValue timeValue, Void v)
    {
        buffer.append("{t '").append(timeValue.getValue().toString()).append("'}");
        return null;
    }

    @Override
    public Void visit(CaseExpression caseExpression, Void v)
    {
        buffer.append("CASE ");
        Expression switchExp = caseExpression.getSwitchExpression();
        if (switchExp != null) {
            switchExp.accept(this, v);
            buffer.append(" ");
        }

        for (Expression exp : caseExpression.getWhenClauses()) {
            exp.accept(this, v);
        }

        Expression elseExp = caseExpression.getElseExpression();
        if (elseExp != null) {
            buffer.append("ELSE ");
            elseExp.accept(this, v);
            buffer.append(" ");
        }

        buffer.append("END");
        return null;
    }

    @Override
    public Void visit(WhenClause whenClause, Void v)
    {
        buffer.append("WHEN ");
        whenClause.getWhenExpression().accept(this, v);
        buffer.append(" THEN ");
        whenClause.getThenExpression().accept(this, v);
        buffer.append(" ");
        return null;
    }

    @Override
    public Void visit(AllComparisonExpression allComparisonExpression, Void v)
    {
        buffer.append(" ALL ");
        allComparisonExpression.getSubSelect().accept((ExpressionVisitor) this, v);
        return null;
    }

    @Override
    public Void visit(AnyComparisonExpression anyComparisonExpression, Void v)
    {
        buffer.append(" ANY ");
        anyComparisonExpression.getSubSelect().accept((ExpressionVisitor) this, v);
        return null;
    }

    @Override
    public Void visit(Concat concat, Void v)
    {
        visitBinaryExpression(concat, " || ");
        return null;
    }

    @Override
    public Void visit(Matches matches, Void v)
    {
        visitOldOracleJoinBinaryExpression(matches, " @@ ");
        return null;
    }

    @Override
    public Void visit(BitwiseAnd bitwiseAnd, Void v)
    {
        visitBinaryExpression(bitwiseAnd, " & ");
        return null;
    }

    @Override
    public Void visit(BitwiseOr bitwiseOr, Void v)
    {
        visitBinaryExpression(bitwiseOr, " | ");
        return null;
    }

    @Override
    public Void visit(BitwiseXor bitwiseXor, Void v)
    {
        visitBinaryExpression(bitwiseXor, " ^ ");
        return null;
    }

    @Override
    public Void visit(CastExpression cast, Void v)
    {
        if (cast.isUseCastKeyword()) {
            buffer.append("CAST(");
            buffer.append(cast.getLeftExpression());
            buffer.append(" AS ");
            buffer.append(cast.getType());
            buffer.append(")");
        }
        else {
            buffer.append(cast.getLeftExpression());
            buffer.append("::");
            buffer.append(cast.getType());
        }
        return null;
    }

    @Override
    public Void visit(Modulo modulo, Void v)
    {
        visitBinaryExpression(modulo, " % ");
        return null;
    }

    @Override
    public Void visit(AnalyticExpression aexpr, Void v)
    {
        buffer.append(aexpr.toString());
        return null;
    }

    @Override
    public Void visit(ExtractExpression eexpr, Void v)
    {
        buffer.append(eexpr.toString());
        return null;
    }

    @Override
    public Void visit(MultiExpressionList multiExprList, Void v)
    {
        for (Iterator<ExpressionList> it = multiExprList.getExprList().iterator(); it.hasNext();) {
            it.next().accept(this, v);
            if (it.hasNext()) {
                buffer.append(", ");
            }
        }
        return null;
    }

    @Override
    public Void visit(IntervalExpression iexpr, Void v)
    {
        buffer.append(iexpr.toString());
        return null;
    }

    @Override
    public Void visit(JdbcNamedParameter jdbcNamedParameter, Void v)
    {
        buffer.append(jdbcNamedParameter.toString());
        return null;
    }

    @Override
    public Void visit(OracleHierarchicalExpression oexpr, Void v)
    {
        buffer.append(oexpr.toString());
        return null;
    }

    @Override
    public Void visit(RegExpMatchOperator rexpr, Void v)
    {
        visitBinaryExpression(rexpr, " " + rexpr.getStringExpression() + " ");
        return null;
    }

    @Override
    public Void visit(RegExpMySQLOperator rexpr, Void v)
    {
        visitBinaryExpression(rexpr, " " + rexpr.getStringExpression() + " ");
        return null;
    }

    @Override
    public Void visit(JsonExpression jsonExpr, Void v)
    {
        buffer.append(jsonExpr.toString());
        return null;
    }

    @Override
    public Void visit(RangeOperators rangeOperators, Void v)
    {
        visitOldOracleJoinBinaryExpression(rangeOperators,
                " " + rangeOperators.getStringExpression() + " ");
        return null;
    }

    @Override
    public Void visit(BooleanValue booleanValue, Void context)
    {
        buffer.append(booleanValue.getStringValue());
        return null;
    }

    @Override
    public Void visit(ObjectValue objectValue, Void context)
    {
        buffer.append(objectValue.getStringValue());
        return null;
    }
}
