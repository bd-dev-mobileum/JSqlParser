package net.sf.jsqlparser.statement;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.JdbcNamedParameter;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectVisitorAdapter;

import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.assertEquals;

/**
 * @author aalmiray
 */
public class AdaptersTest {
    /**
     * Test extracting JDBC named parameters using adapters
     */
    @Test
    public void testAdapters() throws JSQLParserException {
        String sql = "SELECT * FROM MYTABLE WHERE COLUMN_A = :paramA AND COLUMN_B <> :paramB";
        Statement stmnt = CCJSqlParserUtil.parse(sql);

        final Stack<Pair<String, String>> params = new Stack<Pair<String, String>>();
        stmnt.accept(new StatementVisitorAdapter<Void,Void>() {
            @Override
            public Void visit(Select select,Void v) {
               return select.getSelectBody().accept(new SelectVisitorAdapter<Void,Void>() {
                    @Override
                    public Void visit(PlainSelect plainSelect,Void v) {
                        return plainSelect.getWhere().accept(new ExpressionVisitorAdapter<Void,Void>() {
                            @Override
                            protected Void visitBinaryExpression(BinaryExpression expr,Void v) {
                                if (!(expr instanceof AndExpression)) {
                                    params.push(new Pair<String, String>(null, null));
                                }
                                super.visitBinaryExpression(expr,v);
                                return null;
                            }

                            @Override
                            public Void visit(Column column,Void v) {
                                params.push(new Pair<String, String>(column.getColumnName(), params.pop().getRight()));
                                return null;
                            }

                            @Override
                            public Void visit(JdbcNamedParameter parameter,Void v) {
                                params.push(new Pair<String, String>(params.pop().getLeft(), parameter.getName()));
                                return null;
                            }
                        },v);
                    }
                },v);
            }
        },null);

        assertEquals(2, params.size());
        Pair<String, String> param2 = params.pop();
        assertEquals("COLUMN_B", param2.getLeft());
        assertEquals("paramB", param2.getRight());
        Pair<String, String> param1 = params.pop();
        assertEquals("COLUMN_A", param1.getLeft());
        assertEquals("paramA", param1.getRight());
    }

    private static class Pair<L, R> {
        private final L left;
        private final R right;

        private Pair(L left, R right) {
            this.left = left;
            this.right = right;
        }

        public L getLeft() {
            return left;
        }

        public R getRight() {
            return right;
        }

        public boolean isEmpty() {
            return left == null && right == null;
        }

        public boolean isFull() {
            return left != null && right != null;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Pair{");
            sb.append("left=").append(left);
            sb.append(", right=").append(right);
            sb.append('}');
            return sb.toString();
        }
    }
}
