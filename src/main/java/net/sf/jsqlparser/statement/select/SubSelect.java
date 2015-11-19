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
package net.sf.jsqlparser.statement.select;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;

/**
 * A subselect followed by an optional alias.
 */
public class SubSelect
        implements FromItem, Expression, ItemsList
{

    private SelectBody selectBody;
    private Alias alias;
    private boolean useBrackets = true;

    private Pivot pivot;

    @Override
    public <R, C> R accept(FromItemVisitor<R, C> fromItemVisitor, C context)
    {
        return fromItemVisitor.visit(this, context);
    }

    public SelectBody getSelectBody()
    {
        return selectBody;
    }

    public void setSelectBody(SelectBody body)
    {
        selectBody = body;
    }

    @Override
    public <R, C> R accept(ExpressionVisitor<R, C> expressionVisitor, C context)
    {
        return expressionVisitor.visit(this, context);
    }

    @Override
    public Alias getAlias()
    {
        return alias;
    }

    @Override
    public void setAlias(Alias alias)
    {
        this.alias = alias;
    }

    @Override
    public Pivot getPivot()
    {
        return pivot;
    }

    @Override
    public void setPivot(Pivot pivot)
    {
        this.pivot = pivot;
    }

    public boolean isUseBrackets()
    {
        return useBrackets;
    }

    public void setUseBrackets(boolean useBrackets)
    {
        this.useBrackets = useBrackets;
    }

    @Override
    public <R, C> R accept(ItemsListVisitor<R, C> itemsListVisitor, C context)
    {
        return itemsListVisitor.visit(this, context);
    }

    @Override
    public String toString()
    {
        return (useBrackets ? "(" : "") + selectBody + (useBrackets ? ")" : "")
                + ((pivot != null) ? " " + pivot : "")
                + ((alias != null) ? alias.toString() : "");
    }
}
