package cmput.app.catch_me_if_you_scan;

import com.google.android.gms.common.util.Hex;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class CellMap {

    private boolean[][] map;
    private int size;
    private byte[] hash;

    public CellMap(int size, String hash){
        double half = size * Math.ceil(size*0.5);
        double nHashes = Math.ceil(half/256);
        HashCode tempHash;

        for(int i = 1; i < nHashes; i++){
            tempHash = Hashing.sha256().hashString(hash, StandardCharsets.UTF_8);
            hash = hash + tempHash.toString();
        }

        this.hash = Hex.stringToBytes(hash);
        this.map = new boolean[size][size];
        this.size = size;
    }

    public boolean[][] getMap(){
        return this.map;
    }

    public boolean bitToBool(int index){
        byte b = this.hash[index/8];
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
                map[i][j] = bitToBool(index);
                map[this.size - i - 1][j] = map[i][j];

                double toCenter = ((this.size - j * 0.5)*2)/this.size;
                if(i == Math.floor(this.size*0.5) - 1 || i == Math.floor(this.size*0.5) - 2){
                    byte b = this.hash[index/8];
                    int pseudoRandom = (int) b % this.size;
                    bitToBool((int) b % this.size);
                }

                index += 1;
            }
        }
    }
}
