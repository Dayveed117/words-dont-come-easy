package pmd.di.ubi.pt.mainmenu;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class perfil extends Activity {

    private dbHelper oDBH;
    private SQLiteDatabase DB;
    private String user;
    private TextView oTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        oTV = findViewById(R.id.p_user);

        Intent i = getIntent();
        user = i.getStringExtra("active");
        oTV.setText(user);

        oDBH = new dbHelper(this);
        DB = oDBH.getReadableDatabase();
        getStats();
    }

    public void verHistorico(View v) {
        Intent i = new Intent(this, timelinepopup.class);
        i.putExtra("active", user);
        startActivity(i);
    }

    public void finish() {
        oDBH.close();
        super.finish();
        overridePendingTransition(R.anim.go_in_esq, R.anim.go_out_dir);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DB = oDBH.getWritableDatabase();
    }

    @Override
    protected void onPause() {
        super.onPause();
        oDBH.close();
    }

    public void getStats() {

        // Adquirir todos as colunas (estatísticas) da tabela USER
        Cursor oC = DB.rawQuery("SELECT * FROM " + oDBH.USER_TABLE + " WHERE Nome='" + user + "';", null);
        if(oC.moveToFirst()) {

            TextView lvl = findViewById(R.id.p_lvl);
            lvl.setText("Level " + oC.getInt(1));

            ProgressBar xp = findViewById(R.id.p_progress);
            double totxp = oC.getDouble(2);
            xp.setProgress(calculateProgress(totxp));

            TextView nr_totxp = findViewById(R.id.nr_totalxp);
            nr_totxp.setText(String.format("%.2f", totxp));


            //ATENÇÃO eu quero um ratio
            TextView nr_certas = findViewById(R.id.nr_letrascertas);
            nr_certas.setText(oC.getString(3));

            double certas = oC.getDouble(3);
            double total = oC.getDouble(4);
            String s_ratio = String.format("%.2f", calc_ratio(certas, total));

            TextView dec_ratio = findViewById(R.id.nr_ratio);
            dec_ratio.setText(s_ratio + "%");


            TextView nr_prNormal = findViewById(R.id.nr_prnormal);
            nr_prNormal.setText(String.format("%.2f", oC.getDouble(5)));

            TextView nr_prCR= findViewById(R.id.nr_prCR);
            nr_prCR.setText(String.format("%.2f", oC.getDouble(6)));

            TextView nr_prHardcore= findViewById(R.id.nr_prhardcore);
            nr_prHardcore.setText(String.format("%.2f", oC.getDouble(7)));
        }
    }

    public static double calc_ratio(double a, double b) {
        if(a==0 || b==0)
            return 0;
        else if(a>=b)
            return 100;
        else {
            return 100*(a/b);
        }
    }

    public static int calculateProgress(Double xp) {
        if(xp <= 1000)
            return xp.intValue();
        else {
            return xp.intValue()%1000;
        }
    }
}
