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
 */
public class ViewMonsterFragment extends Fragment {

    private Monster monster;

    public ViewMonsterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

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
    }
}