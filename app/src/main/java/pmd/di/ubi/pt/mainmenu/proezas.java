package pmd.di.ubi.pt.mainmenu;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class proezas extends Activity {

    private TextView oTV;
    private dbHelper oDBH;
    private SQLiteDatabase DB;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proezas);

        Intent i = getIntent();
        user = i.getStringExtra("active");
        oTV = findViewById(R.id.ach_user);
        oTV.setText(user);

        showProezas();

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

    public void showProezas() {

        oDBH = new dbHelper(this);
        DB = oDBH.getReadableDatabase();

        // Fazer como na ficha8, Incrementar uma linear view em que o utilizador pode fazer scroll das várias pontuações
        // Bem como limpar a view sempre que é chamada
        LinearLayout llparent = findViewById(R.id.ach_LL);

        Cursor oC = DB.rawQuery("SELECT " + oDBH.INFOP_COL2 + ", " + oDBH.PROEZAS_COL3 + " FROM "
                + oDBH.INFOP_TABLE + ", " + oDBH.PROEZAS_TABLE
                + " NATURAL JOIN " + oDBH.PROEZAS_TABLE
                + " WHERE " + oDBH.PROEZAS_COL1 + "='" + user + "';", null);

        if(oC.getCount() > 0) {

            boolean dbLinha = oC.moveToFirst();
            while(dbLinha) {
                LinearLayout llchild = (LinearLayout) getLayoutInflater().inflate(R.layout.proezas_ll, null);

                TextView oNome = llchild.findViewById(R.id.ach_desc);
                oNome.setText(oC.getString(0));

                TextView oDate = llchild.findViewById(R.id.ach_data);
                oDate.setText(oC.getString(1));

                llparent.addView(llchild);
                dbLinha = oC.moveToNext();
            }
        }

        DB.close();
    }
}
