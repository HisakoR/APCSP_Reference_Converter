package scripts;

public class elseSaver {
    private int lineNum;
    private int frontSpace;

    public elseSaver(int linenum, int spacer){
        this.lineNum = linenum;
        this.frontSpace = spacer;
    }

    public int getFrontSpace() {
        return frontSpace;
    }

    public int getLineNum() {
        return lineNum;
    }
}
