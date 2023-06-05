package pmd.di.ubi.pt.mainmenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class mainmenu extends Activity {

    private TextView oTV;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        Intent i = getIntent();
        user = i.getStringExtra("active");

        oTV = findViewById(R.id.m_user);
        oTV.setText(user);
    }

    public void toGamemode(View v) {
        Intent i = new Intent(this, gamemode.class);
        i.putExtra("active", user);
        startActivity(i);
        overridePendingTransition(R.anim.go_in_dir, R.anim.go_out_esq);
    }

    public void toClassificacoes(View v) {
        Intent i = new Intent(this, classificacoes.class);
        i.putExtra("active", user);
        startActivity(i);
        overridePendingTransition(R.anim.go_in_dir, R.anim.go_out_esq);
    }

    public void toProezas(View v) {
        Intent i = new Intent(this, proezas.class);
        i.putExtra("active", user);
        startActivity(i);
        overridePendingTransition(R.anim.go_in_dir, R.anim.go_out_esq);
    }

    public void toPerfil(View v) {
        Intent i = new Intent(this, perfil.class);
        i.putExtra("active", user);
        startActivity(i);
        overridePendingTransition(R.anim.go_in_dir, R.anim.go_out_esq);
    }

    public void toDefinicoes(View v) {
        Intent i = new Intent(this, definicoes.class);
        i.putExtra("active", user);
        startActivity(i);
        overridePendingTransition(R.anim.go_in_dir, R.anim.go_out_esq);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.go_in_esq, R.anim.go_out_dir);
    }
}
