package cmput.app.catch_me_if_you_scan;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EditProfileDialog extends DialogFragment {
    private User user;
    private EditText name;
    private EditText email;
    private EditText description;

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

        name = view.findViewById(R.id.updateName);
        email = view.findViewById(R.id.updateContact);
        description = view.findViewById(R.id.updateDescription);

        name.setText(user.getName());
        email.setText(user.getEmail());
        description.setText(user.getDescription());

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

            }
        });

        return dialog;
    }
    public void setUser(User u){
        user = u;
    }
}
