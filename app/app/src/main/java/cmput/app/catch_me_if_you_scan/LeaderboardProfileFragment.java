package cmput.app.catch_me_if_you_scan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * This is a fragment that is displayed when a User clicks another user on the leaderboard screen
 */
public class LeaderboardProfileFragment extends Fragment {
    private User user;
    private TextView userName;
    private TextView userEmail;
    private TextView bioText;
    private TextView monstersAmount;
    private TextView monsterListAmount;
    private TextView totalScoreSum;
    private TextView highestScore;
    private TextView lowestScore;

    /**
     * Creates the view for the fragment
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * This the the inflater for the Fragment
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
        View view = inflater.inflate(R.layout.fragment_leaderboard_profile, container, false);

        Bundle incoming = this.getArguments();
        user = incoming.getParcelable("USER");

        setTextViews(view);

        view.findViewById(R.id.lp_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        return view;
    }

    /**
     * This method will take in the view object and then populate all the text views using
     * the user data that was passed into the Fragment
     * @param view
     */
    private void setTextViews(View view) {
        userName = view.findViewById(R.id.lp_name_text);
        userEmail = view.findViewById(R.id.lp_email_text);
        bioText = view.findViewById(R.id.lp_bio_text);
        monstersAmount = view.findViewById(R.id.lp_total_scans_text);
        monsterListAmount = view.findViewById(R.id.lp_num_codes_text);
        totalScoreSum = view.findViewById(R.id.lp_scores_sum_text);
        highestScore = view.findViewById(R.id.lp_highest_score_text);
        lowestScore = view.findViewById(R.id.lp_lowest_score_text);

        userName.setText(user.getName());
        userEmail.setText(user.getEmail());
        bioText.setText(user.getDescription());
        monstersAmount.setText(Integer.toString(user.getMonstersCount()));
        monsterListAmount.setText("Monsters("+Integer.toString(user.getMonstersCount())+")");
        totalScoreSum.setText(Integer.toString(user.getScoreSum()));
        highestScore.setText(Integer.toString(user.getScoreHighest()));
        lowestScore.setText(Integer.toString(user.getScoreLowest()));
    }

    /**
     * This method is for popping the fragment from the back stack and going back to the previous one
     */
    private void goBack() {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}