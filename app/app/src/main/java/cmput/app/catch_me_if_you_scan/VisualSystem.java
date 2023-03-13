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

    public VisualSystem(HashCode hash, int size, int monsterSize){
        this.size = size;
        this.hash = hash;
        this.monsterSize = monsterSize;

        CellularAutomata ca = new CellularAutomata(monsterSize, hash);
        this.map = ca.getMap();
    }

    public Bitmap getBitmap(){
        return this.bmp;
    }
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
