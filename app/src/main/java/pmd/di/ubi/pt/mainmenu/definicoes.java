package pmd.di.ubi.pt.mainmenu;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.processphoenix.ProcessPhoenix;

public class definicoes extends Activity {

    private TextView oTV;
    private dbHelper oDBH;
    private SQLiteDatabase DB;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definicoes);

        Intent i = getIntent();
        user = i.getStringExtra("active");
        oTV = findViewById(R.id.d_user);
        oTV.setText(user);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.go_in_esq, R.anim.go_out_dir);
    }

    public void delUserData(View v) {

        oDBH = new dbHelper(this);
        DB = oDBH.getWritableDatabase();

        DB.beginTransaction();
        try {
            DB.delete(oDBH.PONTUACOES_TABLE, oDBH.USER_COL1 + "='" + user + "'", null);
            DB.delete(oDBH.USER_TABLE, oDBH.USER_COL1 + "='" + user + "'", null);
            DB.setTransactionSuccessful();
        } finally {
            Toast.makeText(getBaseContext(), "Os teus dados foram apagados com sucesso!", Toast.LENGTH_SHORT);
            DB.endTransaction();
        }

        DB.close();
        Intent rebirth = new Intent(this, entryPoint.class);
        rebirth.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(rebirth);

        // Fazer restart da aplicação e adicionar flags para não fazer a transição estranha
        //ProcessPhoenix.triggerRebirth(this, rebirth);

    }

    public void info(View v) {
        Intent infopop = new Intent(this, infoPopup.class);
        startActivity(infopop);
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
