package cmput.app.catch_me_if_you_scan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.hash.HashCode;

import java.util.List;

/**
 * An adapter to show the customization of monsters in the list
 */
public class MonsterListfragmentAdapter extends ArrayAdapter<Monster> {
    /**
       a custom adapter the design each item in ViewList view individually.
    */
    public MonsterListfragmentAdapter(Context context, List<Monster> Monsters) {
//        creates the adapter
        super(context, 0, Monsters);
    }

    @NonNull
    @Override
    /**
     * customize each item in the list by using visit_content layout
     */
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.monster_list_fragment_customization,
                    parent, false);
        } else {
            view = convertView;
        }

        Monster monster = getItem(position);


        TextView monsterName = view.findViewById(R.id.MonsterNameListFragment);
        TextView monsterScore = view.findViewById(R.id.MonsterScoreListActivity);



        ImageView mv = (ImageView) view.findViewById(R.id.monsterImgFragment);
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
        monsterName.setText(monster.getName());
        monsterScore.setText(Integer.toString(monster.getScore())+"pts");

        return view;
    }
}

