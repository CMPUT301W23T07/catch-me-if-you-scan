package cmput.app.catch_me_if_you_scan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private TextView userName;
    private TextView userEmail;
    private TextView bioText;
    private TextView monstersAmount;
    private TextView monsterListAmount;
    private TextView totalScoreSum;
    private TextView highestScore;
    private TextView lowestScore;
    private ListView monstersList;
    private MonsterListAdapter monsterListAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UserController userController = new UserController(db);
    private Button viewListBtn;
    private User user;
    private String deviceId;

    /**
     * This method will create the fragment
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setUser(User u){
        user = u;
    }

    /**
     * This method will inflate the view with the necessary objects and visual items
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

//        Bundle incoming = this.getArguments();
//        if (incoming != null) {
//            user = incoming.getParcelable("USER");
//        }
//        else {
//            deviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//            user = userController.getUserByDeviceID(deviceId);
//        }

        if(user == null){
            deviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            user = userController.getUserByDeviceID(deviceId);
        }

        user.setDescription("Hey I'm "+ user.getName());

        userName = view.findViewById(R.id.name_text);
        userEmail = view.findViewById(R.id.email_text);
        bioText = view.findViewById(R.id.bio_text);
        monstersAmount = view.findViewById(R.id.total_scans_text);
        monsterListAmount = view.findViewById(R.id.num_codes_text);
        totalScoreSum = view.findViewById(R.id.scores_sum_text);
        highestScore = view.findViewById(R.id.highest_score_text);
        lowestScore = view.findViewById(R.id.lowest_score_text);
        monstersList = view.findViewById(R.id.MonstersList);
        viewListBtn = view.findViewById(R.id.viewListBtn);

        if(user!=null) {
            userName.setText(user.getName());
            userEmail.setText(user.getEmail());
            bioText.setText(user.getDescription());
            monstersAmount.setText(Integer.toString(user.getMonstersCount()));
            monsterListAmount.setText("Monsters(" + Integer.toString(user.getMonstersCount()) + ")");
            totalScoreSum.setText(Integer.toString(user.getScoreSum()));
            highestScore.setText(Integer.toString(user.getScoreHighest()));
            lowestScore.setText(Integer.toString(user.getScoreLowest()));
        }

       ArrayList<Monster> monstersListData =  user.getMonsters();

       if(monstersListData.size()>0){
           monsterListAdapter = new MonsterListAdapter(this.getContext(), monstersListData.subList(monstersListData.size()-1, monstersListData.size()));
           monstersList.setAdapter(monsterListAdapter);
       }


        viewListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key", user.getDeviceID());
                MonsterProfileListFragment nextFrag = new MonsterProfileListFragment();
                nextFrag.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.main_fragment_container, nextFrag, "monsterFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        monstersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Monster monster = monstersListData.get(monstersListData.size()-1);
                Bundle bundle = new Bundle();
                bundle.putString("key", user.getDeviceID());
                bundle.putString("id", monster.getHashHex());
                ViewMonsterFragment fragment = new ViewMonsterFragment();
                fragment.setArguments(bundle);

                FragmentManager fm = getParentFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.main_fragment_container, fragment);
                ft.commit();
            }
        });


        return view;
    }
}
