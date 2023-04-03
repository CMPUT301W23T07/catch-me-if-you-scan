package cmput.app.catch_me_if_you_scan;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.firebase.Timestamp;

import java.util.Date;

@RunWith(JUnit4.class)
public class CellularAutomataTest {

    CellularAutomata cellularAutomata;
    int size;
    HashCode hash;

    @Before
    public void setup(){
        size = 8;
        hash = Hashing.sha256().hashBytes(new byte[]{1, 2, 3, 4, 5});

        cellularAutomata = new CellularAutomata(size , hash);
    }
    @After
    public void reset(){
        cellularAutomata = null;
    }

    @Test
    public void testGetMap(){

        assertFalse(null ==cellularAutomata.getMap());
    }


}


