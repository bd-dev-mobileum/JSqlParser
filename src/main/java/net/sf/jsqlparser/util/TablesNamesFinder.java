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
package net.sf.jsqlparser.util;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * Find all used tables within an select statement.
 */
public class TablesNamesFinder implements SelectVisitor<Void,Void>, FromItemVisitor<Void,Void>, ExpressionVisitor<Void,Void>, ItemsListVisitor<Void,Void>, SelectItemVisitor<Void,Void> {

    private List<String> tables;
    /**
     * There are special names, that are not table names but are parsed as
     * tables. These names are collected here and are not included in the tables
     * - names anymore.
     */
    private List<String> otherItemNames;

    /**
     * Main entry for this Tool class. A list of found tables is returned.
     *
     * @param delete
     * @return
     */
    public List<String> getTableList(Delete delete) {
        init();
        tables.add(delete.getTable().getName());
        if (delete.getWhere() != null) {
            delete.getWhere().accept(this,null);
        }

        return tables;
    }

    /**
     * Main entry for this Tool class. A list of found tables is returned.
     *
     * @param insert
     * @return
     */
    public List<String> getTableList(Insert insert) {
        init();
        tables.add(insert.getTable().getName());
        if (insert.getItemsList() != null) {
            insert.getItemsList().accept(this,null);
        }

        return tables;
    }

    /**
     * Main entry for this Tool class. A list of found tables is returned.
     *
     * @param replace
     * @return
     */
    public List<String> getTableList(Replace replace) {
        init();
        tables.add(replace.getTable().getName());
        if (replace.getExpressions() != null) {
            for (Expression expression : replace.getExpressions()) {
                expression.accept(this,null);
            }
        }
        if (replace.getItemsList() != null) {
            replace.getItemsList().accept(this,null);
        }

        return tables;
    }

    /**
     * Main entry for this Tool class. A list of found tables is returned.
     *
     * @param select
     * @return
     */
    public List<String> getTableList(Select select) {
        init();
        if (select.getWithItemsList() != null) {
            for (WithItem withItem : select.getWithItemsList()) {
                withItem.accept(this,null);
            }
        }
        select.getSelectBody().accept(this,null);

        return tables;
    }

    /**
     * Main entry for this Tool class. A list of found tables is returned.
     *
     * @param update
     * @return
     */
    public List<String> getTableList(Update update) {
        init();
        for (Table table : update.getTables()) {
            tables.add(table.getName());
        }
        if (update.getExpressions() != null) {
            for (Expression expression : update.getExpressions()) {
                expression.accept(this,null);
            }
        }

        if (update.getFromItem() != null) {
            update.getFromItem().accept(this,null);
        }

        if (update.getJoins() != null) {
            for (Join join : update.getJoins()) {
                join.getRightItem().accept(this,null);
            }
        }

        if (update.getWhere() != null) {
            update.getWhere().accept(this,null);
        }

        return tables;
    }

    @Override
    public Void visit(WithItem withItem,Void v) {
        otherItemNames.add(withItem.getName().toLowerCase());
        return withItem.getSelectBody().accept(this,v);
    }

    @Override
    public Void visit(PlainSelect plainSelect,Void v) {
        if (plainSelect.getSelectItems() != null) {
            for (SelectItem item : plainSelect.getSelectItems()) {
                item.accept(this,null);
            }
        }

        plainSelect.getFromItem().accept(this,null);

        if (plainSelect.getJoins() != null) {
            for (Join join : plainSelect.getJoins()) {
                join.getRightItem().accept(this,null);
            }
        }
        if (plainSelect.getWhere() != null) {
            plainSelect.getWhere().accept(this,null);
        }
        if (plainSelect.getOracleHierarchical() != null) {
            plainSelect.getOracleHierarchical().accept(this,null);
        }
        return null;
    }

    @Override
    public Void visit(Table tableName,Void v) {
        String tableWholeName = tableName.getFullyQualifiedName();
        if (!otherItemNames.contains(tableWholeName.toLowerCase())
                && !tables.contains(tableWholeName)) {
            tables.add(tableWholeName);
        }
        return null;
    }

    @Override
    public Void visit(SubSelect subSelect,Void v) {
        return subSelect.getSelectBody().accept(this,null);
    }

    @Override
    public Void visit(Addition addition,Void v) {
        return visitBinaryExpression(addition,v);
    }

    @Override
    public Void visit(AndExpression andExpression,Void v) {
        return visitBinaryExpression(andExpression, v);
    }

    @Override
    public Void visit(Between between,Void v) {
        between.getLeftExpression().accept(this,null);
        between.getBetweenExpressionStart().accept(this,null);
        between.getBetweenExpressionEnd().accept(this,null);
        return null;
    }

    @Override
    public Void visit(Column tableColumn,Void v) {
        return null;
    }

    @Override
    public Void visit(Division division,Void v) {
        visitBinaryExpression(division, v);
        return null;
    }

    @Override
    public Void visit(DoubleValue doubleValue,Void v) {
        return null;
    }

    @Override
    public Void visit(EqualsTo equalsTo,Void v) {
        return visitBinaryExpression(equalsTo, v);
    }

    @Override
    public Void visit(Function function,Void v) {
        return null;
    }

    @Override
    public Void visit(GreaterThan greaterThan,Void v) {
        return visitBinaryExpression(greaterThan,v);
    }

    @Override
    public Void visit(GreaterThanEquals greaterThanEquals,Void v) {
        return visitBinaryExpression(greaterThanEquals,v);
    }

    @Override
    public Void visit(InExpression inExpression,Void v) {
        inExpression.getLeftExpression().accept(this,null);
        inExpression.getRightItemsList().accept(this,null);
        return null;
    }

    @Override
    public Void visit(SignedExpression signedExpression,Void v) {
        return signedExpression.getExpression().accept(this,null);
    }

    @Override
    public Void visit(IsNullExpression isNullExpression,Void v) { return null;
    }

    @Override
    public Void visit(JdbcParameter jdbcParameter,Void v) { return null;
    }

    @Override
    public Void visit(LikeExpression likeExpression,Void v) { return 
        visitBinaryExpression(likeExpression,v);
    }

    @Override
    public Void visit(ExistsExpression existsExpression,Void v) { return 
        existsExpression.getRightExpression().accept(this,null);
    }

    @Override
    public Void visit(LongValue longValue,Void v) { return null;
    }

    @Override
    public Void visit(MinorThan minorThan,Void v) { return 
        visitBinaryExpression(minorThan,v);
    }

    @Override
    public Void visit(MinorThanEquals minorThanEquals,Void v) { return 
        visitBinaryExpression(minorThanEquals,v);
    }

    @Override
    public Void visit(Multiplication multiplication,Void v) { return 
        visitBinaryExpression(multiplication,v);
    }

    @Override
    public Void visit(NotEqualsTo notEqualsTo,Void v) { return
        visitBinaryExpression(notEqualsTo,v);
    }

    @Override
    public Void visit(NullValue nullValue,Void v) { return null;
    }

    @Override
    public Void visit(OrExpression orExpression,Void v) { 
        return 
        visitBinaryExpression(orExpression,v);
    }

    @Override
    public Void visit(Parenthesis parenthesis,Void v) { return 
        parenthesis.getExpression().accept(this,null);
    }

    @Override
    public Void visit(StringValue stringValue,Void v) { return null;
    }

    @Override
    public Void visit(Subtraction subtraction,Void v) { return 
        visitBinaryExpression(subtraction,v);
    }

    public Void visitBinaryExpression(BinaryExpression binaryExpression,Void v) { 
        binaryExpression.getLeftExpression().accept(this,null);
        binaryExpression.getRightExpression().accept(this,null);
        return null;
    }

    @Override
    public Void visit(ExpressionList expressionList,Void v) { 
        for (Expression expression : expressionList.getExpressions()) {
            expression.accept(this,null);
        }
        return null;
    }

    @Override
    public Void visit(DateValue dateValue,Void v) { return null;
    }

    @Override
    public Void visit(TimestampValue timestampValue,Void v) { return null;
    }

    @Override
    public Void visit(TimeValue timeValue,Void v) { return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see net.sf.jsqlparser.expression.ExpressionVisitor#visit(net.sf.jsqlparser.expression.CaseExpression)
     */
    @Override
    public Void visit(CaseExpression caseExpression,Void v) { return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see net.sf.jsqlparser.expression.ExpressionVisitor#visit(net.sf.jsqlparser.expression.WhenClause)
     */
    @Override
    public Void visit(WhenClause whenClause,Void v) { return null;
    }

    @Override
    public Void visit(AllComparisonExpression allComparisonExpression,Void v) { 
        allComparisonExpression.getSubSelect().getSelectBody().accept(this,null);
        return null;
    }

    @Override
    public Void visit(AnyComparisonExpression anyComparisonExpression,Void v) { 
        anyComparisonExpression.getSubSelect().getSelectBody().accept(this,null);
        return null;
    }

    @Override
    public Void visit(SubJoin subjoin,Void v) { 
        subjoin.getLeft().accept(this,null);
        subjoin.getJoin().getRightItem().accept(this,null);
        return null;
    }

    @Override
    public Void visit(Concat concat,Void v) { 
        visitBinaryExpression(concat,v);
        return null;
    }

    @Override
    public Void visit(Matches matches,Void v) { 
        visitBinaryExpression(matches,v);
        return null;
    }

    @Override
    public Void visit(BitwiseAnd bitwiseAnd,Void v) { 
        visitBinaryExpression(bitwiseAnd,v);
    return null;}

    @Override
    public Void visit(BitwiseOr bitwiseOr,Void v) { 
        visitBinaryExpression(bitwiseOr,v);
    return null;}

    @Override
    public Void visit(BitwiseXor bitwiseXor,Void v) { 
        visitBinaryExpression(bitwiseXor,v);
    return null;}

    @Override
    public Void visit(CastExpression cast,Void v) { 
        cast.getLeftExpression().accept(this,null);
    return null;}

    @Override
    public Void visit(Modulo modulo,Void v) { 
        visitBinaryExpression(modulo,v);
    return null;}

    @Override
    public Void visit(AnalyticExpression analytic,Void v) { 
    return null;}

    @Override
    public Void visit(SetOperationList list,Void v) { 
        for (PlainSelect plainSelect : list.getPlainSelects()) {
            visit(plainSelect,v);
        }
    return null;}

    @Override
    public Void visit(ExtractExpression eexpr,Void v) { 
        return null;
    }

    @Override
    public Void visit(LateralSubSelect lateralSubSelect,Void v) { 
        return lateralSubSelect.getSubSelect().getSelectBody().accept(this,null);
    }

    @Override
    public Void visit(MultiExpressionList multiExprList,Void v) { 
        for (ExpressionList exprList : multiExprList.getExprList()) {
            exprList.accept(this,null);
        }
    return null;}

    @Override
    public Void visit(ValuesList valuesList,Void v) { 
    return null;}

    private void init() {
        otherItemNames = new ArrayList<String>();
        tables = new ArrayList<String>();
    }

    @Override
    public Void visit(IntervalExpression iexpr,Void v) { 
        return null;
    }

    @Override
    public Void visit(JdbcNamedParameter jdbcNamedParameter,Void v) { 
        return null;
    }

    @Override
    public Void visit(OracleHierarchicalExpression oexpr,Void v) { 
        if (oexpr.getStartExpression() != null) {
            oexpr.getStartExpression().accept(this,null);
        }

        if (oexpr.getConnectExpression() != null) {
            oexpr.getConnectExpression().accept(this,null);
        }
    return null;}

    @Override
    public Void visit(RegExpMatchOperator rexpr,Void v) { 
        visitBinaryExpression(rexpr,v);
    return null;}

    @Override
    public Void visit(RegExpMySQLOperator rexpr,Void v) { 
        visitBinaryExpression(rexpr,v);
    return null;}

    @Override
    public Void visit(JsonExpression jsonExpr,Void v) { 
    return null;}

    @Override
    public Void visit(AllColumns allColumns,Void v) { 
    return null;}

    @Override
    public Void visit(AllTableColumns allTableColumns,Void v) { 
    return null;}

    @Override
    public Void visit(SelectExpressionItem item,Void v) { 
        item.getExpression().accept(this,null);
    return null;}

	@Override
	public Void visit(RangeOperators rangeOperators,Void v) { 
        visitBinaryExpression(rangeOperators,v);
	return null;}

    @Override
    public Void visit(BooleanValue nullValue, Void context)
    {
        return null;
    }

    @Override
    public Void visit(ObjectValue booleanValue, Void context)
    {
        return null;
    }
}
