package cmput.app.catch_me_if_you_scan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
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
        UserController userController = new UserController(db);

        String deviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        User currentUser = userController.getUserByDeviceID(deviceId);

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
        commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Comment c = commentArrayAdapter.getItem(position);

                ProfileFragment pf = new ProfileFragment();
                pf.setUser(userController.getUser(c.getUsername()));
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_fragment_container, pf).addToBackStack(null);
                ft.commit();
            }
        });

        commentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Comment c = commentArrayAdapter.getItem(position);
                if(c.getUsername().equals(currentUser.getName())){
                    PopupMenu p = new PopupMenu(getContext(), view);
                    p.getMenuInflater().inflate(R.menu.comment_menu, p.getMenu());
                    p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getTitle().equals("Edit")){
                                Bundle commentBundle = new Bundle();
                                commentBundle.putString("hex", bundle.getString("id", "0"));
                                EditCommentDialog commentDialog = new EditCommentDialog();
                                commentDialog.setArguments(commentBundle);
                                commentDialog.setAdapter(commentArrayAdapter);
                                commentDialog.setComment(c);
                                commentDialog.show(getActivity().getSupportFragmentManager(), "Edit a comment");

                                p.dismiss();
                            } else if (item.getTitle().equals("Delete")){
                                commentController.deleteComment(c.getDbId());
                                commentArrayAdapter.remove(c);

                                p.dismiss();
                            }
                            return true;
                        }
                    });
                    p.show();
                } else {
                    Toast t = Toast.makeText(getContext(), "This is not your comment!", Toast.LENGTH_SHORT);
                    t.show();
                }
                return true;
            }
        });

        Button deleteButton = getView().findViewById(R.id.delete_button);
        boolean inList = false;

        if(currentUser.checkIfHashExist(monster.getHashHex())){
            inList = true;
        }

        if(inList){
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentUser.removeMonster(monster);
                    userController.deleteMonster(currentUser.getName(), mc.getMonsterDoc(monster.getHashHex()));
                    deleteButton.setVisibility(View.GONE);
                    Toast toast = Toast.makeText(getContext(), "Removed the monster from your account", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

        } else {
            deleteButton.setVisibility(View.GONE);
        }

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

        Button scans = getView().findViewById(R.id.userScans);
        scans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putString("id", monster.getHashHex());
                UsersScannedFragment sf = new UsersScannedFragment();
                sf.setArguments(b);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_fragment_container, sf).addToBackStack(null);
                ft.commit();
            }
        });
    }
}