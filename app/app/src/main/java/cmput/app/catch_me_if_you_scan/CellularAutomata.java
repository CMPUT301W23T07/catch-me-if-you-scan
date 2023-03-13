package cmput.app.catch_me_if_you_scan;

import com.google.common.hash.HashCode;

import java.util.Arrays;

public class CellularAutomata {
    private boolean[][] map;
    private int size;
    private int stepCount;
    private int birthLimit;
    private int deathLimit;

    public CellularAutomata(int size, HashCode hash){
        CellMap mapGen = new CellMap(size, hash);
        this.map = mapGen.getMap();
        this.birthLimit = 5;
        this.deathLimit = 4;
        this.stepCount = 4;
        this.size = size;

        for(int i= 0; i<this.stepCount; i++){
            //this.step();
        }
    }

    public boolean[][] getMap(){
        return this.map;
    }

    public void step(){
        boolean[][] dup = this.map.clone();
        System.out.println(Arrays.toString(dup[16]));
        for(int i = 0; i < this.size; i++){
            for(int j = 0; j < this.size; j++){
                boolean cell = dup[i][j];
                int neighbours = this.getNeighbours(i, j);
                if(cell && neighbours < deathLimit){
                    dup[i][j] = false;
                } else if (!cell && neighbours > birthLimit) {
                    dup[i][j] = true;
                }
            }
        }

        this.map = dup;
    }

    public int getNeighbours(int x, int y){
        int count = 0;
        for(int i = -1; i<2; i++){
            for(int j = -1; j<2; j++){
                if(!(i == 0 && j == 0)){
                    if(x+i>=0 && x+i<this.size && y+j>=0 && y+j<this.size){
                        if(this.map[x+i][y+j]){
                            count += 1;
                        }
                    }
                }
            }
        }

        return count;
    }

}
