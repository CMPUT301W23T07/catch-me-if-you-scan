package cmput.app.catch_me_if_you_scan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * This is the dialog that is shown when a user wants to view a monsters environment photo
 */
public class EnvPhotoDialogFragment extends DialogFragment {
    private Bitmap img;
    public EnvPhotoDialogFragment(Bitmap img){
        this.img = img;
    }

    /**
     * This method is called when the dialog is created, it will inflate the view with
     * the desired elements
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_environment_photo, null);

        ImageView image = view.findViewById(R.id.imageView3);
        image.setImageBitmap(img);
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        dismiss();

                    }
                });
            }
        });
        dialog.show();

        return dialog;

    }

}

