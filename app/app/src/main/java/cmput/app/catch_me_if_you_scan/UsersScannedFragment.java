package cmput.app.catch_me_if_you_scan;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UsersScannedFragment extends Fragment {

    ArrayList<User> users;
    ScansArrayAdapter userAdapter;
    Monster monster;
    ListView userListView;
    TextView title;
    Button back;
    FirebaseFirestore db;
    Bundle bundle;

    public UsersScannedFragment() {
        // Required empty public constructor
    }

    public static UsersScannedFragment newInstance(String param1, String param2) {
        UsersScannedFragment fragment = new UsersScannedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users_scanned, container, false);
    }

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
    }
}