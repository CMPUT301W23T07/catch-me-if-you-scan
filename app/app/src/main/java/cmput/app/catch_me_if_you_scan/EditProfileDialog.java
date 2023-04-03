package cmput.app.catch_me_if_you_scan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * dialog to allow user to update their description and contact info
 */
public class EditProfileDialog extends DialogFragment {
    private User user;
    private EditText email;
    private EditText description;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UserController userController = new UserController(db);

    /**
     * This method is called when the dialog is created, it will inflate the view
     * with the desired elements
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.edit_profile_dialog, null);

        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Update Profile")
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        email = view.findViewById(R.id.updateContact);
        description = view.findViewById(R.id.updateDescription);

        email.setText(user.getEmail());
        description.setText(user.getDescription());

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button updateBtn = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (email.length() == 0){
                            Toast.makeText(getContext(), "Email invalid", Toast.LENGTH_SHORT).show();
                        }
                        if (description.length() == 0){
                            Toast.makeText(getContext(), "Description invalid", Toast.LENGTH_SHORT).show();
                        }

                        User dbUserEmail = userController.getUserByEmail(email.getText().toString());
                        if (dbUserEmail != null && !user.getName().equals(dbUserEmail.getName())){
                            Toast.makeText( getActivity(), "This email is taken", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            user.setEmail(email.getText().toString());
                            user.setDescription(description.getText().toString());
                            HashMap<String, Object> updatedUserDetails = new HashMap<String, Object>();
                            updatedUserDetails.put("email", email.getText().toString());
                            updatedUserDetails.put("description", description.getText().toString());
                            userController.updateUser(user.getName(), updatedUserDetails);
                            dismiss();
                            ProfileFragment nextFrag = new ProfileFragment();
                            nextFrag.setUser(user);
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.main_fragment_container, nextFrag, "userFragment")
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                });
            }
        });

        return dialog;
    }

    /**
     * sets the user passed to it as the user of the dialog
     * @param u the user to show data
     */
    public void setUser(User u){
        user = u;
    }
}
