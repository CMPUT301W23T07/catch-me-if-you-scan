package cmput.app.catch_me_if_you_scan;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
 * Display a list of the users who have scanned a monster.
 * Use putArgs to send in a Bundle containing the monster to get user scans from.
 */
public class UsersScannedFragment extends Fragment {
    ArrayList<User> users;
    ScansArrayAdapter userAdapter;
    Monster monster;
    ListView userListView;
    TextView title;
    Button back;
    FirebaseFirestore db;
    Bundle bundle;

    /**
     * constructor
     */
    public UsersScannedFragment() {
        // Required empty public constructor
    }

    /**
     * this method creates the view
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * this method infaltes the view of the fragment with the view elements contained inside
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
        return inflater.inflate(R.layout.fragment_users_scanned, container, false);
    }

    /**
     * Populate the fragment with users who scanned and set listeners on them so profiles can be visited
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){

        db = FirebaseFirestore.getInstance();
        MonsterController mc = new MonsterController(db);
        UserController uc = new UserController(db);

        bundle = this.getArguments();

        monster = mc.getMonster(bundle.getString("id", "0"));
        users = uc.getAllUsersForMonster(mc.getMonsterDoc(monster.getHashHex()));

        userListView = getView().findViewById(R.id.scansListView);
        title = getView().findViewById(R.id.scansTitle);
        back = getView().findViewById(R.id.backButton);

        title.setText(monster.getName() + "'s scans");

        userAdapter = new ScansArrayAdapter(this.getContext(), users);
        userListView.setAdapter(userAdapter);

        // Back button; simply relaunches the monster fragment
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putString("id", monster.getHashHex());
                ViewMonsterFragment mf = new ViewMonsterFragment();
                mf.setArguments(b);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_fragment_container, mf).addToBackStack(null);
                ft.commit();
            }
        });

        // On clicking user, launch their profile fragment
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User u = userAdapter.getItem(position);

                ProfileFragment pf = new ProfileFragment();
                pf.setUser(u);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_fragment_container, pf).addToBackStack(null);
                ft.commit();
            }
        });
    }
}