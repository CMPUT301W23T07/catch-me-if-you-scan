package cmput.app.catch_me_if_you_scan;

import com.google.android.gms.common.util.Hex;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class CellMap {

    private boolean[][] map;
    private int size;
    private byte[] hashBytes;
    private HashCode hash;

    public CellMap(int size, HashCode hash){
        this.hash = hash;
        double half = size * Math.ceil(size*0.5);
        double nHashes = Math.ceil(half/256);
        HashCode tempHash;
        String hashString = hash.toString();

        for(int i = 1; i < nHashes; i++){
            tempHash = Hashing.sha256().hashString(hashString, StandardCharsets.UTF_8);
            hashString = hashString + tempHash.toString();
        }

        this.hashBytes = Hex.stringToBytes(hashString);
        this.map = new boolean[size][size];
        this.size = size;

        this.generateMap();
        randomWalk();
        randomWalk();
    }

    public boolean[][] getMap(){
        return this.map;
    }

    public boolean bitToBool(int index){
        byte b = this.hashBytes[index/8];
        if(((b >> (index%8)) & 1) == 1){
            return true;
        }
        return false;
    }

    public void generateMap(){
        for(int i = 0; i < this.size; i++){
            this.map[i] = new boolean[this.size];
        }

        int index = 0;

        for(int i = 0; i < Math.ceil(this.size * 0.5); i++){
            for(int j = 0; j< this.size; j++){
                this.map[i][j] = bitToBool(index);
                this.map[this.size - i - 1][j] = map[i][j];

                double toCenter = (Math.abs(j-this.size*0.5)*2)/this.size;
                if(i == Math.floor(this.size*0.5) - 1 || i == Math.floor(this.size*0.5) - 2){
                    int pseudoRand = (int) this.hashBytes[(i+j)%this.hashBytes.length];
                    if(pseudoRand/317.5 > toCenter){
                        this.map[i][j] = true;
                        this.map[this.size - i - 1][j] = true;
                    }
                }

                index += 1;
            }
        }
    }

    public void randomWalk(){
        Random gen = new Random(this.hash.asLong());
        int x;
        int y;
        x = gen.nextInt() % this.size;
        y = gen.nextInt() % this.size;
        for(int i = 0; i < 100; i++){
            if(x>=0 && x<this.size && y>=0 && y<this.size){
                this.map[x][y] = true;
                this.map[this.size - x - 1][y] = true;
            }

            x += gen.nextInt()%3-1;
            y += gen.nextInt()%3-1;
        }
    }
}
