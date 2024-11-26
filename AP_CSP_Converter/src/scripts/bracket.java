package scripts;

public class bracket {
    private int lineNum;
    private int spaces;
    public bracket(int typer, int spacesr){
        this.lineNum = typer;
        this.spaces = spacesr;
    }
    public int getLineNum(){
        return lineNum;
    }
    public int getSpaces(){
        return spaces;
    }
}
