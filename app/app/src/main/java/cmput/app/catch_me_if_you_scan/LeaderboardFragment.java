package cmput.app.catch_me_if_you_scan;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * A fragment that supports the UI for the leaderboard and its functions such as filtering etc.
 */
public class LeaderboardFragment extends Fragment {
    private ArrayAdapter<User> usersLeaderboardAdapter;
    private ArrayAdapter<Monster> monstersLeaderboardAdapter;
    private ArrayAdapter<User> usersFilteredAdapter;
    private ArrayAdapter<User> monstersFilteredAdapter;
    private ListView leaderboard;
    private ArrayList<User> users = new ArrayList<User>();
    private ArrayList<Monster> monsters = new ArrayList<Monster>();
    private User firstPlaceUser;
    private Monster firstPlaceMonster;
    private int current;
    private UserController uc = new UserController(FirebaseFirestore.getInstance());
    private MonsterController mc = new MonsterController(FirebaseFirestore.getInstance());
    private TextView filterType;
    private TextView userName;
    private TextView points;

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
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        setViewObjects(v);

        initSearchBar(v);

        users = uc.getAllUsers();
        monsters = mc.getAllMonsters();

        sortUsers();
        sortMonsters();

        current = 1;

        //Set the first place user
        filterType.setText("User High Scores");
        userName.setText(users.get(0).getName());
        points.setText(String.format("%d", users.get(0).getScoreSum()));

        firstPlaceUser = users.get(0);
        users.remove(0);

        firstPlaceMonster = monsters.get(0);
        monsters.remove(0);

        createAdapters();

        leaderboard.setAdapter(usersLeaderboardAdapter);
        leaderboard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (current == 1) {
                    launchFragmentWithUser(users.get(position));
                }
                else if (current == 2) {
                    launchFragmentWithMonster(monsters.get(position));
                }
            }
        });

        v.findViewById(R.id.first_place_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == 1) {
                    launchFragmentWithUser(firstPlaceUser);
                }
                else if (current == 2) {
                    launchFragmentWithMonster(firstPlaceMonster);
                }
            }
        });

        v.findViewById(R.id.leaderboard_filter_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLeaderboard();
            }
        });

        return v;
    }

    /**
     * This method will sort the users returned by the database for leaderboard display
     * purposes
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sortUsers() {
        users.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if (o1.getScoreSum() < o2.getScoreSum()) {
                    return 1;
                }
                return -1;
            }
        });
    }

    /**
     * This method will sort all the monsters for the second leaderboard type
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sortMonsters() {
        monsters.sort(new Comparator<Monster>() {
            @Override
            public int compare(Monster o1, Monster o2) {
                if (o1.getScore() < o2.getScore()) {
                    return 1;
                }
                return -1;
            }
        });
    }

    /**
     * This method creates the adapters that are used for the base leaderboard, no filters applied
     */
    public void createAdapters() {
        //Create the custom adapter for the list view
        usersLeaderboardAdapter = new ArrayAdapter(getActivity(), R.layout.leaderboard_list_item, R.id.username_text_view, users) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView user = (TextView) view.findViewById(R.id.username_text_view);
                TextView points = (TextView) view.findViewById(R.id.subtitle_text_view);
                TextView pos = (TextView) view.findViewById(R.id.position_text_view);
                user.setText(users.get(position).getName());
                points.setText(String.format("%d", users.get(position).getScoreSum()));
                pos.setText(String.format("%d", position+2));
                return view;
            }
        };

        //Create the custom adapter for the list view
        monstersLeaderboardAdapter = new ArrayAdapter(getActivity(), R.layout.leaderboard_list_item, R.id.username_text_view, monsters) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView user = (TextView) view.findViewById(R.id.username_text_view);
                TextView points = (TextView) view.findViewById(R.id.subtitle_text_view);
                TextView pos = (TextView) view.findViewById(R.id.position_text_view);
                user.setText(monsters.get(position).getName());
                points.setText(String.format("%d", monsters.get(position).getScore()));
                pos.setText(String.format("%d", position+2));
                return view;
            }
        };
    }

    /**
     * This method will take in the view and get the view objects to edit by their ID's
     * @param v
     */
    public void setViewObjects(View v) {
        leaderboard = v.findViewById(R.id.leaderboard_list_view);
        filterType = v.findViewById(R.id.filter_title_text_view);
        userName = v.findViewById(R.id.first_place_username_text_view);
        points = v.findViewById(R.id.first_place_points_text_view);
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
                if (current == 1) {

                    ArrayList<User> filtered = new ArrayList<>();

                    for (User item : users) {
                        if (item.getName().toLowerCase().contains(s.toLowerCase())) {
                            filtered.add(item);
                            Log.d("FILTERING LEADERBOARD", item.getName());
                        }
                    }

                    usersFilteredAdapter = new ArrayAdapter(getActivity(), R.layout.leaderboard_list_item, R.id.position_text_view, filtered) {
                        @SuppressLint("DefaultLocale")
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView user = (TextView) view.findViewById(R.id.username_text_view);
                            TextView points = (TextView) view.findViewById(R.id.subtitle_text_view);
                            TextView pos = (TextView) view.findViewById(R.id.position_text_view);
                            user.setText(filtered.get(position).getName());
                            points.setText(String.format("%d", filtered.get(position).getScoreSum()));
                            pos.setText(String.format("%d", position+2));
                            return view;
                        }
                    };

                    leaderboard.setAdapter(usersFilteredAdapter);
                }
                else if (current == 2) {

                    ArrayList<Monster> filtered = new ArrayList<>();

                    for (Monster item : monsters) {
                        if (item.getName().toLowerCase().contains(s.toLowerCase())) {
                            filtered.add(item);
                            Log.d("FILTERING LEADERBOARD", item.getName());
                        }
                    }

                    monstersFilteredAdapter = new ArrayAdapter(getActivity(), R.layout.leaderboard_list_item, R.id.position_text_view, filtered) {
                        @SuppressLint("DefaultLocale")
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView user = (TextView) view.findViewById(R.id.username_text_view);
                            TextView points = (TextView) view.findViewById(R.id.subtitle_text_view);
                            TextView pos = (TextView) view.findViewById(R.id.position_text_view);
                            user.setText(filtered.get(position).getName());
                            points.setText(String.format("%d", filtered.get(position).getScore()));
                            pos.setText(String.format("%d", position+2));
                            return view;
                        }
                    };

                    leaderboard.setAdapter(monstersFilteredAdapter);
                }
                return false;
            }
        });
    }

    /**
     * This method will launch the leaderboard fragment give a user
     * @param user
     */
    private void launchFragmentWithUser(User user) {
        ProfileFragment nextFrag = new ProfileFragment();
        nextFrag.setUser(user);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, nextFrag, "userFragment")
                .addToBackStack(null)
                .commit();
    }

    /**
     * This method will launch the leaderboard fragment give a monster
     * @param monster
     */
    private void launchFragmentWithMonster(Monster monster) {
        ViewMonsterFragment nextFrag = new ViewMonsterFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", monster.getHashHex());
        nextFrag.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, nextFrag, "monsterFragment")
                .addToBackStack(null)
                .commit();
    }

    /**
     * This method will change the leaderboard upon filter button click
     * There are 2 main leaderboard types
     *  1: Highest sum of scores from players
     *  2: Highest monster score
     */
    @SuppressLint("DefaultLocale")
    private void changeLeaderboard() {
        if (current == 1) {

            filterType.setText("Highest Scoring Monster");
            userName.setText(firstPlaceMonster.getName());
            points.setText(String.format("%d", firstPlaceMonster.getScore()));

            leaderboard.setAdapter(monstersLeaderboardAdapter);

            current = 2;
        }
        else if (current == 2) {

            filterType.setText("User High Scores");
            userName.setText(firstPlaceUser.getName());
            points.setText(String.format("%d", firstPlaceUser.getScoreSum()));

            leaderboard.setAdapter(usersLeaderboardAdapter);

            current = 1;
        }
    }
}