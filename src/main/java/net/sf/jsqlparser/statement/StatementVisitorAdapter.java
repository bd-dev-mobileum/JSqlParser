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
package net.sf.jsqlparser.statement;

import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.execute.Execute;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;

public class StatementVisitorAdapter<R,C> implements StatementVisitor<R,C> {
    @Override
    public R visit(Select select,C context) {
        return null;
    }

    @Override
    public R visit(Delete delete,C context) {
        return null;
    }

    @Override
    public R visit(Update update,C context) {
        return null;
    }

    @Override
    public R visit(Insert insert,C context) {
        return null;
    }

    @Override
    public R visit(Replace replace,C context) {
        return null;
    }

    @Override
    public R visit(Drop drop,C context) {
        return null;
    }

    @Override
    public R visit(Truncate truncate,C context) {
        return null;
    }

    @Override
    public R visit(CreateIndex createIndex,C context) {
        return null;
    }

    @Override
    public R visit(CreateTable createTable,C context) {
        return null;
    }

    @Override
    public R visit(CreateView createView,C context) {
        return null;
    }

    @Override
    public R visit(Alter alter,C context) {
        return null;
    }

    @Override
    public R visit(Statements stmts,C context) {
        return null;
    }

    @Override
    public R visit(Execute execute,C context) {
        return null;
    }
}
