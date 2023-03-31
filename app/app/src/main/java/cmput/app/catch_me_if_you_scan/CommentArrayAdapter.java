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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CommentArrayAdapter extends ArrayAdapter<Comment> {
    CommentArrayAdapter(Context context, ArrayList<Comment> comments){
        super(context, 0, comments);
    }

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

        SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm aa");
        String dateString;
        dateString = df.format(comment.getCommentDate().toDate());

        contents.setText(comment.getCommentMessage());
        username.setText(comment.getUsername());
        date.setText("on "+dateString);
        return view;
    }
}
