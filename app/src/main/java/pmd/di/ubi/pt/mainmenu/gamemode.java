package pmd.di.ubi.pt.mainmenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class gamemode extends Activity {

    private TextView oTV;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamemode);

        Intent i = getIntent();
        user = i.getStringExtra("active");
        oTV = findViewById(R.id.gm_user);
        oTV.setText(user);
    }

    public void gotoPartida(View v) {
        int id = v.getId();
        Button b = findViewById(id);
        Intent i = new Intent(this, partida.class);
        String mode = b.getText().toString();
        i.putExtra("active", user);
        i.putExtra("mode", mode);
        switch (mode) {
            case "Normal":
                i.putExtra("buttons", 5);
                break;
            case "Contra-Relógio":
                i.putExtra("buttons", 7);
                break;
            case "Hardcore":
                i.putExtra("buttons", 10);
        }
        startActivity(i);
        overridePendingTransition(R.anim.go_in_dir, R.anim.go_out_esq);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.go_in_esq, R.anim.go_out_dir);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
