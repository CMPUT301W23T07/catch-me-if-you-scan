package cmput.app.catch_me_if_you_scan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.firebase.firestore.FirebaseFirestore;

import java.nio.charset.StandardCharsets;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewMonsterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewMonsterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Monster monster;

    public ViewMonsterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewMonsterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewMonsterFragment newInstance(String param1, String param2) {
        ViewMonsterFragment fragment = new ViewMonsterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_monster, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        MonsterController mc = new MonsterController(db);

        Bundle bundle = this.getArguments();

        this.monster = mc.getMonster(bundle.getString("id", "0"));

        TextView score = (TextView) getView().findViewById(R.id.score);
        TextView name = (TextView) getView().findViewById(R.id.name);

        ImageView mv = (ImageView) getView().findViewById(R.id.monsterImg);
        ImageView bg = (ImageView) getView().findViewById(R.id.environment);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        name.setText(monster.getName());
        score.setText(monster.getScore());
        mv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mv.getViewTreeObserver().removeOnPreDrawListener(this);
                HashCode hash = monster.getHash();
                VisualSystem visual = new VisualSystem(hash, mv.getMeasuredHeight(), 9);
                visual.generate(10);
                mv.setImageBitmap(visual.getBitmap());
                return true;
            }
        });

        bg.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                bg.getViewTreeObserver().removeOnPreDrawListener(this);
                byte[] img = monster.getEnvPhoto();
//                byte[] img = env.getBytes(StandardCharsets.UTF_8);
                Bitmap bmp = BitmapFactory.decodeByteArray(img, 0, img.length);
                bg.setImageBitmap(bmp);
                return true;
            }
        });
    }
}