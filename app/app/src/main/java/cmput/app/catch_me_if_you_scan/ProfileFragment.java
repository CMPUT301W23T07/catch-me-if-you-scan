package cmput.app.catch_me_if_you_scan;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String deviceId;

    private TextView userName;
    private TextView userEmail;
    private TextView bioText;
    private TextView monstersAmount;
    private TextView monsterListAmount;
    private TextView totalScoreSum;
    private TextView highestScore;
    private TextView lowestScore;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UserController userController = new UserController(db);

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        String deviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        User user = userController.getUserByDeviceID(deviceId);
//        Monster monster1 = new Monster("mark",2.0, 1.0, "");
//        Monster monster2 = new Monster("jay",2.0, 1.0, "");
//        user.addMonster(monster1);
//        user.addMonster(monster2);


        userName = view.findViewById(R.id.name_text);
        userEmail = view.findViewById(R.id.email_text);
        bioText = view.findViewById(R.id.bio_text);
        monstersAmount = view.findViewById(R.id.total_scans_text);
        monsterListAmount = view.findViewById(R.id.num_codes_text);
        totalScoreSum = view.findViewById(R.id.scores_sum_text);
        highestScore = view.findViewById(R.id.highest_score_text);
        lowestScore = view.findViewById(R.id.lowest_score_text);

        userName.setText(user.getName());
        userEmail.setText(user.getEmail());
        bioText.setText(user.getDescription());
        monstersAmount.setText(Integer.toString(user.getMonstersCount()));
        monsterListAmount.setText("Monsters("+Integer.toString(user.getMonstersCount())+")");
        totalScoreSum.setText(Integer.toString(user.getScoreSum()));
        highestScore.setText(Integer.toString(user.getScoreHighest()));
        lowestScore.setText(Integer.toString(user.getScoreLowest()));

        return view;
    }
}
