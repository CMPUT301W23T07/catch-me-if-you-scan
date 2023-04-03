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

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Adapter for comment list view - shows message and username
 */
public class CommentArrayAdapter extends ArrayAdapter<Comment> {

    /**
     * This is the constructor for the comment array adapter
     * @param context
     * @param comments
     */
    CommentArrayAdapter(Context context, ArrayList<Comment> comments){
        super(context, 0, comments);
    }

    /**
     * Populate comment list item with relevant information.
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return the View for the comment.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.comment_content, parent, false);
        } else {
            view = convertView;
        }
        Comment comment = getItem(position);
        TextView contents = view.findViewById(R.id.comment_text);
        TextView username = view.findViewById(R.id.comment_user);
        TextView date = view.findViewById(R.id.comment_date);
        ImageView avatar = view.findViewById(R.id.comment_avatar);

        SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm");
        String dateString;
        dateString = df.format(comment.getCommentDate().toDate());

        contents.setText(comment.getCommentMessage());
        username.setText(comment.getUsername());
        date.setText("on "+dateString);
        return view;
    }
}
