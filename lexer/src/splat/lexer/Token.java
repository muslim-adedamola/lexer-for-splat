package splat.lexer;

public class Token {
    private String val;
    private int line;
    private int column;

    public Token(String val, int line, int column) {
        this.val = val;
        this.line = line;
        this.column = column;
    }

    public String getValue() {
        return val;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public String toString() {
        return val + " is on line " + line + " and column " + column;
    }
}
