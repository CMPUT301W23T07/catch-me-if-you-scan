/**
 * A DialogFragment for filtering the map view based on the selected tiers.
 */

package cmput.app.catch_me_if_you_scan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.ArrayList;

public class FilterMapFragment extends DialogFragment {


    /**
     * Creates the FilterMapFragment.
     * @param savedInstanceState The saved instance state of the FilterMapFragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Inflates the view for the FilterMapFragment.
     * @param inflater The LayoutInflater for the FilterMapFragment.
     * @param container The ViewGroup for the FilterMapFragment.
     * @param savedInstanceState The saved instance state of the FilterMapFragment.
     * @return The View for the FilterMapFragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_filter_map, container, false);

        Button confirm = v.findViewById(R.id.confirm_filter_button);
        RadioButton bronze = v.findViewById(R.id.bronze_radio);
        RadioButton silver = v.findViewById(R.id.silver_radio);
        RadioButton gold = v.findViewById(R.id.gold_radio);
        RadioButton platinum = v.findViewById(R.id.platinum_radio);
        RadioButton diamond = v.findViewById(R.id.diamond_radio);

        confirm.setOnClickListener(new View.OnClickListener() {


            /**
             * Handles the click event for the confirm filter button.
             * @param view The View that was clicked.
             */
            @Override
            public void onClick(View view) {
                ArrayList<Integer> tiers = new ArrayList<Integer>();
                if (bronze.isSelected()) {
                    tiers.add(1);
                }if (silver.isSelected()) {
                    tiers.add(2);
                }if (gold.isSelected()) {
                    tiers.add(3);
                }if (platinum.isSelected()) {
                    tiers.add(4);
                }if (diamond.isSelected()) {
                    tiers.add(5);
                }

                //Intent i = getActivity().getIntent();
                //i.putIntegerArrayListExtra("TIERS", tiers);

                FragmentManager fm = getParentFragmentManager();
                FragmentTransaction fragTrans = fm.beginTransaction();
                fm.popBackStackImmediate("TIERS", R.id.map);
                fragTrans.commit();

            }
        });

        return v;
    }
}