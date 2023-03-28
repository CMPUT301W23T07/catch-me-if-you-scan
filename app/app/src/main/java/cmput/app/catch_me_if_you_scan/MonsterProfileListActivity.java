package cmput.app.catch_me_if_you_scan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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






    }
}