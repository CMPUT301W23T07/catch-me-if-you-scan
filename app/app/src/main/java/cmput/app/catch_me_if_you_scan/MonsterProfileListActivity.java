package cmput.app.catch_me_if_you_scan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MonsterProfileListActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UserController userController = new UserController(db);
    private ListView monsterList;
    private EditText searchbtn;
    private Button backbtn;
    private MonsterListActivityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster_profile_list);

        Intent intent = getIntent();
        String deviceId = intent.getStringExtra("key");
        User user = userController.getUserByDeviceID(deviceId);
        ArrayList<Monster> monstersListData =  user.getMonsters();

        adapter = new MonsterListActivityAdapter(this, monstersListData);

        monsterList = findViewById(R.id.monsterListActivity);
        backbtn = findViewById(R.id.backbtn);

        monsterList.setAdapter(adapter);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        monsterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Monster monster = monstersListData.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("id", monster.getHashHex());
                ViewMonsterFragment fragment = new ViewMonsterFragment();
                fragment.setArguments(bundle);

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.listActivityLayout, fragment);
                ft.commit();
            }
        });

    }
}