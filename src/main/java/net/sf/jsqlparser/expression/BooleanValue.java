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

public class BooleanValue
        implements Expression
{
    private boolean value;

    public BooleanValue(final String value)
    {
        String val = value;
        if (value.equalsIgnoreCase("f")) {
            val = "false";
        }
        if (value.equalsIgnoreCase("t")) {
            val = "true";
        }
        this.value = Boolean.parseBoolean(val);
    }

    public BooleanValue(boolean value)
    {
        this.value = value;
    }

    @Override
    public <R, C> R accept(ExpressionVisitor<R, C> expressionVisitor, C context)
    {
        return expressionVisitor.visit(this, context);
    }

    public boolean getValue()
    {
        return value;
    }

    public void setValue(Boolean d)
    {
        value = d;
    }

    public String getStringValue()
    {
        return ((Boolean) value).toString();
    }

    @Override
    public String toString()
    {
        return getStringValue();
    }
}
