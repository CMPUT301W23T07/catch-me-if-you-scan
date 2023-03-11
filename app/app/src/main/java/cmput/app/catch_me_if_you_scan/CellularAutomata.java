package cmput.app.catch_me_if_you_scan;

public class CellularAutomata {
    private int[][] map;
    private int stepCount;
    private int birthLimit;
    private int deathLimit;

    public CellularAutomata(int size){
        this.map = new int[size][size];
        this.birthLimit = 5;
        this.deathLimit = 4;
        this.stepCount = 4;
    }

    public void doSteps(){

    }

}
