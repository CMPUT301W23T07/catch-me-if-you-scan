package cmput.app.catch_me_if_you_scan;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.util.Log;
import android.view.View;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CellMapTest {


    int size;
    HashCode hash;
    CellMap cellMap;


    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {


        size = 10;
        hash = Hashing.sha256().hashBytes(new byte[]{1, 2, 3, 4, 5});
        // 74f81fe167d99b4cb41d6d0ccda82278caee9f3e2f25d5e5a3936ff3dcec60d0
        // HashByte: [B@29c0f0d
        cellMap = new CellMap(size, hash);
    }
    /**
     * Clean up after test.
     */
    @After
    public void tearDown() {
        cellMap = null;
    }
    /**
     * Tests if the MainActivity is created properly.
     */
    @Test
    public void testGetMap() {
        boolean[][] map = cellMap.getMap();
        assertEquals(size, map.length);
        assertEquals(size, map[0].length);
    }
    @Test
    public void testGenerateMap(){
        boolean[][] map = cellMap.getMap();
        int dim_1 = map.length;
        int dim_2 = map[0].length;
        assertEquals(size, map.length);
        assertEquals(size, map[0].length);

        boolean hit = false;

        for(int i = 0; i < dim_1; i++){
            for(int k = 0;i < dim_2; i++){
                if(map[i][k] == true ){
                    hit = true;
                }
            }
        }

        assertTrue(hit);


    }
    @Test
    public void testBitToBool() {

        boolean bit0 = cellMap.bitToBool(0);
        boolean bit1 = cellMap.bitToBool(1);
        boolean bit2 = cellMap.bitToBool(2);
        boolean bit3 = cellMap.bitToBool(3);

        assertFalse(bit0);
        assertFalse(bit1);
        assertTrue(bit2);
        assertFalse(bit3);
    }

}
