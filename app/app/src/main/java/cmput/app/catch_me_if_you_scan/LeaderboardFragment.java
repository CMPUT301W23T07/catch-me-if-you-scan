package cmput.app.catch_me_if_you_scan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Objects;


/**
 * A fragment that supports the UI for the leaderboard and its functions such as filtering etc.
 */
public class LeaderboardFragment extends Fragment {

    private ArrayAdapter<MockLeaderboardUserClass> leaderboardAdapter;
    private ArrayAdapter<MockLeaderboardUserClass> filteredAdapter;
    private ArrayList<MockLeaderboardUserClass> dataList = new ArrayList<>();
    private ListView leaderboard;
    private ArrayList<User> users = new ArrayList<User>();
    private UserController uc = new UserController(FirebaseFirestore.getInstance());

    /**
     * Creates the fragment
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates the view for the fragment
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        leaderboard = v.findViewById(R.id.leaderboard_list_view);

        initSearchBar(v);

        users = uc.getAllUsers();

        sortUsers();

        //Set the first place user
        TextView filterType = v.findViewById(R.id.filter_title_text_view);
        filterType.setText("Highest Scoring Monsters");
        TextView userName = v.findViewById(R.id.first_place_username_text_view);
        userName.setText(users.get(0).getName());
        TextView points = v.findViewById(R.id.first_place_points_text_view);
        points.setText(users.get(0).getScoreHighest());

        //Create the custom adapter for the list view
        leaderboardAdapter = new ArrayAdapter(getActivity(), R.layout.leaderboard_list_item, R.id.username_text_view, users) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView user = (TextView) view.findViewById(R.id.username_text_view);
                TextView points = (TextView) view.findViewById(R.id.subtitle_text_view);
                TextView pos = (TextView) view.findViewById(R.id.position_text_view);
                user.setText(users.get(position+1).getName());
                points.setText(users.get(position+1).getScoreHighest());
                pos.setText(position+1);
                return view;
            }
        };

        leaderboard.setAdapter(leaderboardAdapter);

        return v;
    }

    /**
     * This method creates some mock data for testing and usage while the app is incomplete
     */
    private void createMockData() {
        MockLeaderboardUserClass item1 = new MockLeaderboardUserClass("Flying-Kimbo", "245 points", "2");
        MockLeaderboardUserClass item2 = new MockLeaderboardUserClass("Mr. Snowden", "210 points", "3");
        MockLeaderboardUserClass item3 = new MockLeaderboardUserClass("Kris-Johnson", "199 points", "4");
        MockLeaderboardUserClass item4 = new MockLeaderboardUserClass("Kristen", "175 points", "5");
        MockLeaderboardUserClass item5 = new MockLeaderboardUserClass("Wilson", "5 points", "6");
        dataList.add(item1);
        dataList.add(item2);
        dataList.add(item3);
        dataList.add(item4);
        dataList.add(item5);
    }

    /**
     * This method will sort the users returned by the database for leaderboard display
     * purposes
     */
    public void sortUsers() {

    }

    /**
     * This function is for creating the async search function for the leaderboard, it will take the
     * text entered into the search bar and find all leaderboard users that have some text that
     * matches the string entered.
     *
     * @param v
     *      Takes in an object v of type View
     */
    private void initSearchBar(View v) {
        SearchView search = (SearchView) v.findViewById(R.id.search_bar);
        leaderboard = v.findViewById(R.id.leaderboard_list_view);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                ArrayList<MockLeaderboardUserClass> filtered = new ArrayList<>();

                for (MockLeaderboardUserClass item : dataList) {
                    if (item.user.toLowerCase().contains(s.toLowerCase())) {
                        filtered.add(item);
                        Log.d("FILTERING LEADERBOARD", item.user);
                    }
                }

                filteredAdapter = new ArrayAdapter(getActivity(), R.layout.leaderboard_list_item, R.id.position_text_view, filtered) {
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView user = (TextView) view.findViewById(R.id.username_text_view);
                        TextView points = (TextView) view.findViewById(R.id.subtitle_text_view);
                        TextView pos = (TextView) view.findViewById(R.id.position_text_view);
                        user.setText(filtered.get(position).user);
                        points.setText(filtered.get(position).subtitle);
                        pos.setText(filtered.get(position).position);
                        return view;
                    }
                };

                leaderboard.setAdapter(filteredAdapter);

                return false;
            }
        });
    }

}