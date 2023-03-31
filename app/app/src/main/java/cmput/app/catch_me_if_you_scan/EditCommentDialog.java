package cmput.app.catch_me_if_you_scan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class EditCommentDialog extends DialogFragment {

    private CommentArrayAdapter adapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_comment, null);

        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Write a comment")
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        EditText commentEditText = view.findViewById(R.id.editCommentText);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if(commentEditText.getText().toString().trim().isEmpty()){
                            Toast toast = Toast.makeText(getContext(), "Your comment is empty!", Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            // Add a comment to the db
                            String deviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            UserController userController = new UserController(db);
                            User currentUser = userController.getUserByDeviceID(deviceId);
                            String username = currentUser.getName();
                            String hex = getArguments().getString("hex", "0");
                            Comment comment = new Comment(commentEditText.getText().toString(), new Timestamp(new Date()), hex, username);

                            CommentController cc = new CommentController(db);
                            cc.create(comment);

                            adapter.add(comment);
                            adapter.notifyDataSetChanged();

                            dismiss();
                        }

                    }
                });
            }
        });
        dialog.show();

        return dialog;

    }

    public void setAdapter(CommentArrayAdapter a){
        adapter = a;
    }
}

