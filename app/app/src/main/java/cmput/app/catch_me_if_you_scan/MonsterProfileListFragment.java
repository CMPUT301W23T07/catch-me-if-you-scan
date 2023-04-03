package cmput.app.catch_me_if_you_scan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MonsterProfileListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonsterProfileListFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UserController userController = new UserController(db);
    private User user;
    private ListView monsterList;
    private SearchView searchbtn;
    private MonsterListfragmentAdapter adapter;
    private MonsterListfragmentAdapter filteredAdapter;

    /**
     * Constructor
     */
    public MonsterProfileListFragment() {
        // Required empty public constructor
    }

    /**
     * creates the view
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
        View view = inflater.inflate(R.layout.fragment_monster_profile_list, container, false);
        Bundle bundle = this.getArguments();


        String deviceId = bundle.getString("key");
        user = userController.getUserByDeviceID(deviceId);

        ArrayList<Monster> monstersListData =  user.getMonsters();

        adapter = new MonsterListfragmentAdapter(getActivity(), monstersListData);

        monsterList = view.findViewById(R.id.monsterListFragment);
        searchbtn = (SearchView) view.findViewById(R.id.searchBtnMonsterList);
        monsterList.setAdapter(adapter);

        monsterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Monster monster = monstersListData.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("id", monster.getHashHex());
                ViewMonsterFragment fragment = new ViewMonsterFragment();
                fragment.setArguments(bundle);

                FragmentManager fm = getParentFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_fragment_container, fragment).addToBackStack(null);
                ft.commit();
            }
        });

        searchbtn.setQueryHint("Search");
        searchbtn.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Monster> filtered = new ArrayList<>();

                for (Monster item : monstersListData) {
                    if (item.getName().toLowerCase().contains(newText.toLowerCase())) {
                        filtered.add(item);
                    }
                }
                filteredAdapter = new MonsterListfragmentAdapter(getActivity(), filtered);
                monsterList.setAdapter(filteredAdapter);
                return false;
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}