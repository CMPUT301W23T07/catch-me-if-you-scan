package cmput.app.catch_me_if_you_scan;

import com.google.common.hash.HashCode;

import java.util.Arrays;

/**
 * This class represents the cellular automata for the visual system
 */
public class CellularAutomata {
    private boolean[][] map;
    private int size;
    private int stepCount;

    /**
     * The birth limit for a cell to be born.
     */
    private int birthLimit;

    /**
     * The death limit for a cell to die.
     */
    private int deathLimit;

    /**
     * Constructs a new CellularAutomata object with the specified size and hash.
     *
     * @param size the size of the cellular automata
     * @param hash the hash code to use for generating the initial cell map
     */
    public CellularAutomata(int size, HashCode hash){
        CellMap mapGen = new CellMap(size, hash);
        this.map = mapGen.getMap();
        this.birthLimit = 5;
        this.deathLimit = 4;
        this.stepCount = 4;
        this.size = size;

        for(int i= 0; i<this.stepCount; i++){
//            this.step();
        }
    }

    /**
     * Returns the boolean map of the cellular automata.
     *
     * @return the boolean map of the cellular automata
     */
    public boolean[][] getMap(){
        return this.map;
    }

    /**
     * Executes one step of the cellular automata algorithm.
     */
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

    /**
     * Returns the number of live neighbors for the specified cell.
     *
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     * @return the number of live neighbors for the specified cell
     */
    private int getNeighbours(int x, int y){
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
