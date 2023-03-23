package cmput.app.catch_me_if_you_scan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class EditCommentDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_comment, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        EditText commentEditText = view.findViewById(R.id.editCommentText);

        return builder.setView(view)
                .setTitle("Comment on this monster")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", (dialog, which) -> {
                    if(commentEditText.getText().toString().trim().isEmpty()){
                        Toast toast = Toast.makeText(getContext(), "Your comment is empty!", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        // Add a comment to the db
                    }
                })
                .create();

    }
}
