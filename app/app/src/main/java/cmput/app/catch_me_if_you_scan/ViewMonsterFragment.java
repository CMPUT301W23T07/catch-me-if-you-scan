package cmput.app.catch_me_if_you_scan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.firebase.firestore.FirebaseFirestore;

import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewMonsterFragment extends Fragment{

    private Monster monster;
    private ListView commentListView;
    private ArrayList<Comment> comments;
    private CommentArrayAdapter commentArrayAdapter;
    private CommentController commentController;
    private FirebaseFirestore db;
    private Bundle bundle;

    public ViewMonsterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO:
        // delete own comments
        // delete monster from user if they have it
        // see other users when comment clicked
        // view who scanned monster

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_view_monster, container, false);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        db = FirebaseFirestore.getInstance();
        MonsterController mc = new MonsterController(db);

        bundle = this.getArguments();

        monster = mc.getMonster(bundle.getString("id", "0"));

        commentController = new CommentController(db);
        comments = commentController.getCommentForMonster(monster.getHashHex());

        Collections.sort(comments, new Comparator<Comment>() {
            @Override
            public int compare(Comment o1, Comment o2) {
                return o1.getCommentDate().compareTo(o2.getCommentDate());
            }
        });

        commentListView = getView().findViewById(R.id.commentListView);
        commentArrayAdapter = new CommentArrayAdapter(this.getContext(), comments);

        commentListView.setAdapter(commentArrayAdapter);

        String deviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        UserController userController = new UserController(db);
        User currentUser = userController.getUserByDeviceID(deviceId);

        TextView score = (TextView) getView().findViewById(R.id.score);
        TextView name = (TextView) getView().findViewById(R.id.name);

        ImageView mv = (ImageView) getView().findViewById(R.id.monsterImg);
        ImageView bg = (ImageView) getView().findViewById(R.id.environment);

//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        name.setText(monster.getName());
        score.setText(Integer.toString(monster.getScore()));
        mv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mv.getViewTreeObserver().removeOnPreDrawListener(this);
                HashCode hash = monster.getHash();
                VisualSystem visual = new VisualSystem(hash, mv.getMeasuredHeight(), 9);
                visual.generate(mv.getMeasuredHeight()/9-1);
                mv.setImageBitmap(visual.getBitmap());
                return true;
            }
        });

        bg.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                bg.getViewTreeObserver().removeOnPreDrawListener(this);
                byte[] img = monster.getEnvPhoto();
                Bitmap bmp = BitmapFactory.decodeByteArray(img, 0, img.length);
                //Bitmap resizedBmp = Bitmap.createBitmap(bmp, 0, 200, bg.getMeasuredWidth(), bg.getMeasuredHeight());
                bg.setImageBitmap(bmp);
                return true;
            }
        });

        TextView comment = (TextView) getView().findViewById(R.id.editComment);
        comment.setHint("Write a comment...");

        comment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle commentBundle = new Bundle();
                commentBundle.putString("hex", bundle.getString("id", "0"));
                EditCommentDialog commentDialog = new EditCommentDialog();
                commentDialog.setArguments(commentBundle);
                commentDialog.setAdapter(commentArrayAdapter);
                commentDialog.show(getActivity().getSupportFragmentManager(), "Make a comment");
            }
        });
    }
}