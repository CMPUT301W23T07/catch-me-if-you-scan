package cmput.app.catch_me_if_you_scan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Objects;

public class LeaderboardFragment extends Fragment {

    private ArrayAdapter<MockLeaderboardUserClass> leaderboardAdapter;

    private ArrayList<MockLeaderboardUserClass> dataList = new ArrayList<>();
    private ListView leaderboard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        leaderboard = v.findViewById(R.id.leaderboard_list_view);

        initSearchBar(v);
        createMockData();

        TextView filterType = v.findViewById(R.id.filter_title_text_view);
        filterType.setText("Highest Scoring Monsters");
        TextView userName = v.findViewById(R.id.first_place_username_text_view);
        userName.setText("rileyzilka01");
        TextView points = v.findViewById(R.id.first_place_points_text_view);
        points.setText("9999 points");

        leaderboardAdapter = new ArrayAdapter(getActivity(), R.layout.leaderboard_list_item, R.id.username_text_view, dataList) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView user = (TextView) view.findViewById(R.id.username_text_view);
                TextView points = (TextView) view.findViewById(R.id.subtitle_text_view);
                TextView pos = (TextView) view.findViewById(R.id.position_text_view);
                user.setText(dataList.get(position).user);
                points.setText(dataList.get(position).subtitle);
                pos.setText(dataList.get(position).position);
                return view;
            }
        };

        leaderboard.setAdapter(leaderboardAdapter);


        return v;
    }

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
                    }
                }

                ArrayAdapter<MockLeaderboardUserClass> filteredAdapter;
                filteredAdapter = new ArrayAdapter(getActivity(), R.layout.leaderboard_list_item, R.id.username_text_view, filtered) {
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView user = (TextView) view.findViewById(R.id.username_text_view);
                        TextView points = (TextView) view.findViewById(R.id.subtitle_text_view);
                        TextView pos = (TextView) view.findViewById(R.id.position_text_view);
                        user.setText(dataList.get(position).user);
                        points.setText(dataList.get(position).subtitle);
                        pos.setText(dataList.get(position).position);
                        return view;
                    }
                };

                leaderboard.setAdapter(filteredAdapter);

                return false;
            }
        });
    }

}