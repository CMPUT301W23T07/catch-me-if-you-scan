package cmput.app.catch_me_if_you_scan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

/**
 * Dialog for creating/editing comments
 * Pass in the Adapter for the list view using setAdapter.
 * To edit an existing comment, use setComment.
 */
public class EditCommentDialog extends DialogFragment {

    private CommentArrayAdapter adapter;
    private Comment comment;

    /**
     * Used to pass in an existing comment to edit.
     * @param c Existing Comment to edit
     */
    public void setComment(Comment c){
        comment = c;
    }

    /**
     * Populates dialog - fill EditText with previous message if editing an old comment.
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return the new EditCommentDialog.
     */
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
        if(comment != null){
            commentEditText.setText(comment.getCommentMessage());
        }

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // Disallow adding comment if empty
                        if (commentEditText.getText().toString().trim().isEmpty()) {
                            Toast toast = Toast.makeText(getContext(), "Your comment is empty!", Toast.LENGTH_SHORT);
                            toast.show();
                        } else if (comment != null) {
                            // Edit existing comment
                            //adapter.remove(comment);
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            comment.setCommentMessage(commentEditText.getText().toString());
                            CommentController cc = new CommentController(db);
                            cc.updateComment(comment.getDbId(), commentEditText.getText().toString(), comment.getCommentDate());

                            //adapter.add(comment);
                            adapter.notifyDataSetChanged();

                            dismiss();
                        } else {
                            // Add a comment to the db
                            String deviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            UserController userController = new UserController(db);
                            User currentUser = userController.getUserByDeviceID(deviceId);
                            String username = currentUser.getName();
                            String hex = getArguments().getString("hex", "0");
                            comment = new Comment(commentEditText.getText().toString(), new Timestamp(new Date()), hex, username);

                            CommentController cc = new CommentController(db);
                            String id = cc.create(comment);
                            comment.setDbId(id);

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

    /**
     * Pass the fragment the adapter so it can update the list view.
     * @param a CommentArrayAdapter to add/edit comment in
     */
    public void setAdapter(CommentArrayAdapter a){
        adapter = a;
    }
}

