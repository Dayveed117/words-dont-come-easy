package pmd.di.ubi.pt.mainmenu;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.TextView;

public class timelinepopup extends Activity {

    private dbHelper oDBH;
    private SQLiteDatabase DB;
    private TextView oTV;
    private String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timelinepopup);

        Intent i = getIntent();
        user = i.getStringExtra("active");
        oTV = findViewById(R.id.tm_user);
        oTV.setText(user);

        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        int c = display.widthPixels;
        int l = display.heightPixels;

        getWindow().setLayout((int)(c*0.8), (int)(l*0.8));

        oDBH = new dbHelper(this);
        DB = oDBH.getReadableDatabase();

        showTimeline();
    }

    public void showTimeline() {

        LinearLayout llparent = findViewById(R.id.tm_LL);
        Cursor oC = DB.rawQuery("SELECT * FROM " + oDBH.PONTUACOES_TABLE + " WHERE "
                + oDBH.PONTUACOES_COL1 + "='" + user + "' ORDER BY " + oDBH.PONTUACOES_COL4 + " DESC;", null);

        if(oC.getCount() > 0) {

            boolean dbLinha = oC.moveToFirst();
            while(dbLinha) {
                LinearLayout llchild = (LinearLayout) getLayoutInflater().inflate(R.layout.timeline_ll, null);

                TextView oNome = llchild.findViewById(R.id.tmll_nome);
                oNome.setText(oC.getString(0));

                TextView oMode = llchild.findViewById(R.id.tmll_gamemode);
                oMode.setText(oC.getString(1));

                TextView oScore = llchild.findViewById(R.id.tmll_score);
                oScore.setText(String.format("%.2f", oC.getDouble(2)));

                TextView oDate = llchild.findViewById(R.id.tmll_date);
                oDate.setText(oC.getString(3));

                llparent.addView(llchild);
                dbLinha = oC.moveToNext();
            }
        }
    }

    @Override
    public void finish() {
        oDBH.close();
        super.finish();
    }
}
