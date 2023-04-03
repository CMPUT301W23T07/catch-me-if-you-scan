package cmput.app.catch_me_if_you_scan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Adapter for list view of user scans of a monster.
 */
public class ScansArrayAdapter extends ArrayAdapter<User> {

    /**
     * constructor
     * @param context
     * @param users
     */
    ScansArrayAdapter(Context context, ArrayList<User> users){
        super(context, 0, users);
    }

    /**
     * Populate user scan list item with username
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return View for user scans
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.user_scanned_item, parent, false);
        } else {
            view = convertView;
        }

        User user = getItem(position);
        TextView name = view.findViewById(R.id.username);
        ImageView avatar = view.findViewById(R.id.avatar);

        name.setText(user.getName());

        return view;
    }
}
