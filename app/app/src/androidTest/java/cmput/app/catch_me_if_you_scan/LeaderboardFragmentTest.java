//package cmput.app.catch_me_if_you_scan;
//
//import static org.junit.Assert.assertNotNull;
//
//import androidx.fragment.app.testing.FragmentScenario;
//import androidx.test.core.app.ActivityScenario;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//@RunWith(AndroidJUnit4.class)
//public class LeaderboardFragmentTest {
//
////    private FragmentScenario<LeaderboardFragment> scenario;
//
//    private FragmentScenario<LeaderboardFragment> scenario = FragmentScenario.launchInContainer(LeaderboardFragment.class);
//
//    @Before
//    public void setUp() {
//
//        scenario = FragmentScenario.launchInContainer(LeaderboardFragment.class);
//    }
//    @Test
//    public void interact(){
//
//        // Create a new instance of the fragment you want to test
//        LeaderboardFragment fragment = new LeaderboardFragment();
//
//        // Launch the fragment in a container
//        FragmentScenario<LeaderboardFragment> scenario = FragmentScenario.launchInContainer(LeaderboardFragment.class);
//
//        // Now you can perform actions on the fragment and verify its behavior
//        scenario.onFragment(new FragmentScenario.FragmentAction<LeaderboardFragment>() {
//            @Override
//            public void perform(LeaderboardFragment fragment) {
//
//            }
//        });
//    }
//
//    @Test
//    public void testFragmentCreation() {
//
//        scenario.onFragment(fragment -> assertNotNull(fragment));
//    }
//
//
//
//}