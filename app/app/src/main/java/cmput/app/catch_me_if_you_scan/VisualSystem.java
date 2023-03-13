package cmput.app.catch_me_if_you_scan;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.google.common.collect.Table;
import com.google.common.hash.HashCode;
import com.google.common.primitives.Ints;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class VisualSystem {

    private HashCode hash;
    private int size;
    private boolean[][] map;
    private int[][] pixels;
    private Bitmap bmp;
    private int monsterSize;

    /**
     * A visual system for a HashCode, which uses deterministic pseudorandom walks, cellular automata and noise generation to create a "monster"
     * @param hash The HashCode object to generate with
     * @param size The size of the bitmap to draw to, in pixels - create a size*size square
     * @param monsterSize The size of the monster to draw in the bitmap, in pixels - create a monsterSize*monsterSize monster
     */
    public VisualSystem(HashCode hash, int size, int monsterSize){
        this.size = size;
        this.hash = hash;
        this.monsterSize = monsterSize;

        CellularAutomata ca = new CellularAutomata(monsterSize, hash);
        this.map = ca.getMap();
    }

    /**
     * Get the bitmap generated by the VisualSystem
     * @return the bitmap with the monster
     */
    public Bitmap getBitmap(){
        return this.bmp;
    }

    /**
     * generate the monster visual and draw it to the bitmap, scaling by a given factor of size
     * @param scale the scaling factor (positive int) to scale the monster by.
     */
    public void generate(int scale){
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        this.bmp = Bitmap.createBitmap(this.size, this.size, conf);
        Canvas canvas = new Canvas(this.bmp);
        canvas.drawRGB(255,255,255);

        int padding = this.size - scale * this.monsterSize;
        this.scale(scale, -16777216);

        //System.out.println(this.pixels.length);
        //this.bmp.setPixels(this.pixels, 0, this.size, 0,padding/2, this.monsterSize*scale, this.monsterSize*scale);
        int[][] drawMap = this.rotate();

        for(int i = 0; i<drawMap.length; i++){
            this.bmp.setPixels(drawMap[i], 0, this.size, padding/2, i+padding/2, this.pixels[i].length, 1);
        }
    }

    /**
     * Scales the monster pixels by a given factor - current iteration takes a single color to draw the monster with
     * @param scale the scaling factor for each monster pixel
     * @param color the color of the monster
     */
    public void scale(int scale, int color){

        int newSize = this.monsterSize*scale;
        int[][] temp = new int[newSize][newSize];
        for(int i = 0; i < newSize; i++){
            for(int j = 0; j < newSize; j++){
                if(this.map[i/scale][j/scale]) {
                    temp[i][j] = color;
                } else {
                    temp[i][j] = -1;
                }
            }
        }

        this.pixels = temp;
    }

    /**
     * Since the Bitmap.setPixels draws row by row and not column by column, the pixel map must be rotated 90 degrees to display correctly
     * @return the rotated pixel matrix.
     */
    public int[][] rotate(){
        int m = this.pixels.length;
        int n = this.pixels[0].length;

        int[][] ret = new int[n][m];

        for(int r = 0; r<m; r++){
            for(int c = 0; c<n; c++){
                ret[c][m-1-r] = this.pixels[r][c];
            }
        }

        return ret;

    }

}
