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
import net.sf.jsqlparser.schema.*;
import net.sf.jsqlparser.statement.select.*;

import java.util.*;

/**
 * Add aliases to every column and expression selected by a select - statement.
 * Existing aliases are recognized and preserved. This class standard uses a
 * prefix of A and a counter to generate new aliases (e.g. A1, A5, ...). This
 * behaviour can be altered.
 *
 * @author tw
 */
public class AddAliasesVisitor<R,C> implements SelectVisitor<R,C>, SelectItemVisitor<R,C> {

	private List<String> aliases = new LinkedList<String>();
	private boolean firstRun = true;
	private int counter = 0;
	private String prefix = "A";

	@Override
	public R visit(PlainSelect plainSelect,C context) {
		firstRun = true;
		counter = 0;
		aliases.clear();
		for (SelectItem item : plainSelect.getSelectItems()) {
			item.accept(this,context);
		}
		firstRun = false;
		for (SelectItem item : plainSelect.getSelectItems()) {
			item.accept(this,context);
		}
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
	public R visit(AllTableColumns allTableColumns,C context) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public R visit(SelectExpressionItem selectExpressionItem,C context) {
		if (firstRun) {
			if (selectExpressionItem.getAlias() != null) {
				aliases.add(selectExpressionItem.getAlias().getName().toUpperCase());
			}
		} else {
			if (selectExpressionItem.getAlias() == null) {

				while (true) {
					String alias = getNextAlias().toUpperCase();
					if (!aliases.contains(alias)) {
						aliases.add(alias);
						selectExpressionItem.setAlias(new Alias(alias));
						break;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Calculate next alias name to use.
	 *
	 * @return
	 */
	protected String getNextAlias() {
		counter++;
		return prefix + counter;
	}

	/**
	 * Set alias prefix.
	 *
	 * @param prefix
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public R visit(WithItem withItem,C context) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public R visit(AllColumns allColumns,C context) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
