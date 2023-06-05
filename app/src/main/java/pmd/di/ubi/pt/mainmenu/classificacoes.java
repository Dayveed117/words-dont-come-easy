package pmd.di.ubi.pt.mainmenu;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class classificacoes extends Activity {

    private dbHelper oDBH;
    private SQLiteDatabase DB;
    private Button oB;
    private TextView oTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classificacoes);

        Intent i = getIntent();
        String user = i.getStringExtra("active");
        oTV = findViewById(R.id.c_user);
        oTV.setText(user);

        //Abrir por default as classificações normais
        oB = findViewById(R.id.c_normal);
        showData(oB.getText().toString(), 20);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.go_in_esq, R.anim.go_out_dir);
    }

    @Override
    protected void onResume() { super.onResume(); }

    @Override
    protected void onPause() { super.onPause(); }

    public void leaderboard(View v) {
        oB = findViewById(v.getId());
        showData(oB.getText().toString(), 20);
    }

    public void showData(String gamemode, int limit) {

        oDBH = new dbHelper(this);
        DB = oDBH.getReadableDatabase();

        // Fazer como na ficha8, Incrementar uma linear view em que o utilizador pode fazer scroll das várias pontuações
        // Bem como limpar a view sempre que é chamada
        LinearLayout llparent = findViewById(R.id.c_LL);
        llparent.removeAllViews();

        Cursor oC = DB.rawQuery("SELECT " + oDBH.PONTUACOES_COL1 + "," + oDBH.PONTUACOES_COL3 + "," + oDBH.PONTUACOES_COL4 + " FROM " + oDBH.PONTUACOES_TABLE
                + " WHERE " + oDBH.PONTUACOES_COL2 + "='" + gamemode + "' ORDER BY " + oDBH.PONTUACOES_COL3 + " DESC " + " LIMIT " + limit + ";", null);

        if(oC.getCount() > 0) {

            boolean dbLinha = oC.moveToFirst();
            while(dbLinha) {
                LinearLayout llchild = (LinearLayout) getLayoutInflater().inflate(R.layout.linedb, null);

                TextView oNome = llchild.findViewById(R.id.ll_nome);
                oNome.setText(oC.getString(0));

                TextView oScore = llchild.findViewById(R.id.ll_score);
                oScore.setText(String.format("%.2f", oC.getDouble(1)));

                TextView oDate = llchild.findViewById(R.id.ll_date);
                oDate.setText(oC.getString(2));

                llparent.addView(llchild);
                dbLinha = oC.moveToNext();
            }
        }

        DB.close();
    }
}
