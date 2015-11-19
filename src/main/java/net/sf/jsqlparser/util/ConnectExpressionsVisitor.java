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
import net.sf.jsqlparser.statement.select.*;

import java.util.*;

/**
 * Connect all selected expressions with a binary expression. Out of select a,b
 * from table one gets select a || b as expr from table. The type of binary
 * expression is set by overwriting this class abstract method
 * createBinaryExpression.
 *
 * @author tw
 */
public abstract class ConnectExpressionsVisitor<R,C> implements SelectVisitor<R,C>, SelectItemVisitor<R,C> {

	private String alias = "expr";
	private final List<SelectExpressionItem> itemsExpr = new LinkedList<SelectExpressionItem>();

	public ConnectExpressionsVisitor() {
	}

	public ConnectExpressionsVisitor(String alias) {
		this.alias = alias;
	}

	/**
	 * Create instances of this binary expression that connects all selected
	 * expressions.
	 *
	 * @return
	 */
	protected abstract BinaryExpression createBinaryExpression();

	@Override
	public R visit(PlainSelect plainSelect,C context) {
		for (SelectItem item : plainSelect.getSelectItems()) {
			item.accept(this,context);
		}

		if (itemsExpr.size() > 1) {
			BinaryExpression binExpr = createBinaryExpression();
			binExpr.setLeftExpression(itemsExpr.get(0).getExpression());
			for (int i = 1; i < itemsExpr.size() - 1; i++) {
				binExpr.setRightExpression(itemsExpr.get(i).getExpression());
				BinaryExpression binExpr2 = createBinaryExpression();
				binExpr2.setLeftExpression(binExpr);
				binExpr = binExpr2;
			}
			binExpr.setRightExpression(itemsExpr.get(itemsExpr.size() - 1).getExpression());

			SelectExpressionItem sei = new SelectExpressionItem();
			sei.setExpression(binExpr);

			plainSelect.getSelectItems().clear();
			plainSelect.getSelectItems().add(sei);
		}

		((SelectExpressionItem) plainSelect.getSelectItems().get(0)).setAlias(new Alias(alias));
		return null;
	}

	@Override
	public R visit(SetOperationList setOpList,C context) {
		for (PlainSelect select : setOpList.getPlainSelects()) {
			select.accept(this,context);
		}
		return null;
	}

	@Override
	public R visit(WithItem withItem,C context) {
	    return null;
	}

	@Override
	public R visit(AllTableColumns allTableColumns,C context) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public R visit(AllColumns allColumns,C context) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public R visit(SelectExpressionItem selectExpressionItem,C context) {
		itemsExpr.add(selectExpressionItem);
		return null;
	}
}
