/*
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2015 JSQLParser
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

public class ObjectValue
        implements Expression
{
    private Object value;

    public ObjectValue(final Object value)
    {
        this.value = value;
    }

    @Override
    public <R, C> R accept(ExpressionVisitor<R, C> expressionVisitor, C context)
    {
        return expressionVisitor.visit(this, context);
    }

    public Object getValue()
    {
        return value;
    }

    public void setValue(Object d)
    {
        value = d;
    }

    public String getStringValue()
    {
        return value == null ? null : value.toString();
    }

    @Override
    public String toString()
    {
        return getStringValue();
    }
}
