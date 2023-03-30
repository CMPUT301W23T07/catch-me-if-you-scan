package cmput.app.catch_me_if_you_scan;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.firebase.Timestamp;

import java.util.Date;

@RunWith(JUnit4.class)
public class CommentTest {

    Comment mock;
    String message = "Hello World";
    String message_two = "Hello New World";
    String monsterHashCode = "MockHash";
    String monsterHashCode_two = "MockHashTwo";
    String username = "MockUser";

    String username_two = "MockUserTwo";
    Timestamp timestamp = new Timestamp(new Date());


    public Comment createComment(String message){



        return new Comment(message, timestamp , monsterHashCode, username);
    }

    /**
     * Tests the getName method of the Monster class.
     */
    @Before
    public void setup(){
        mock = createComment(message);
    }
    @After
    public void reset(){
        mock = null;
    }

    @Test
    public void testGetMessage(){
        assertEquals(message,mock.getCommentMessage());
    }

    @Test
    public void testSetCommentMessage(){

        assertEquals(message,mock.getCommentMessage());
        mock.setCommentMessage(message_two);
        assertEquals(message_two,mock.getCommentMessage());
    }

    @Test
    public void testGetTimeStamp(){
        assertEquals(timestamp,mock.getCommentDate());
    }

    @Test
    public void testGetMonsterHash(){
        assertEquals(monsterHashCode,mock.getMonsterHashCode());
    }

    @Test
    public void testSetMonsterHash(){
        assertEquals(monsterHashCode,mock.getMonsterHashCode());
        mock.setMonsterHashCode(monsterHashCode_two);
        assertEquals(monsterHashCode_two,mock.getMonsterHashCode());
    }

    @Test
    public void testGetUser(){
        assertEquals(username,mock.getUsername());
    }

    @Test
    public void testSetUser(){
        assertEquals(username,mock.getUsername());
        mock.setUsername(username_two);
        assertEquals(username_two,mock.getUsername());
    }

}


