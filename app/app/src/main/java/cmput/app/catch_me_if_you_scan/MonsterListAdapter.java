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
 * This is class is the adapter for the monster list used in the profile fragment
 */
public class MonsterListAdapter extends ArrayAdapter<Monster> {

    /**
     * This is the constructor for the adapter
     * @param context
     * @param Monsters
     */
    public MonsterListAdapter(Context context, List<Monster> Monsters) {
        super(context, 0, Monsters);
    }

    /**
     * This method is used for creating the list view and inflating it with the views within
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
//        customize each item in the list by using visit_content layout
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.monster_list_customize,
                    parent, false);
        } else {
            view = convertView;
        }

        Monster monster = getItem(position);


        TextView monsterName = view.findViewById(R.id.MonsterNameList);
        TextView monsterScore = view.findViewById(R.id.MonsterScoreList);



        ImageView mv = (ImageView) view.findViewById(R.id.monsterImg);
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