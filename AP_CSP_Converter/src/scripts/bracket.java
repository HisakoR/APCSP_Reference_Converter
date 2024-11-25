package scripts;

public class bracket {
    private String type;
    private int spaces;
    public bracket(String typer, int spacesr){
        this.type = typer;
        this.spaces = spacesr;
    }
    public String getType(){
        return type;
    }
    public int getSpaces(){
        return spaces;
    }
}
