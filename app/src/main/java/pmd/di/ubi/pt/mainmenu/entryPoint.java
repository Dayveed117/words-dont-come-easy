package pmd.di.ubi.pt.mainmenu;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class entryPoint extends Activity {

    private long backpress;
    private Toast outToast;
    private ArrayList<String> users;
    private dbHelper oDBH;
    private SQLiteDatabase DB;
    private EditText oED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_point);
        welcomeText();

        oDBH = new dbHelper(this);
        DB = oDBH.getWritableDatabase();


        SharedPreferences oSP = getPreferences(0);

        // Armazenar a informação das proezas quando começamos a app pela primeira vez
        if(!oSP.getBoolean("loadProezas", false)) {

            SharedPreferences.Editor oED = oSP.edit();
            oED.putBoolean("loadProezas", true);
            oED.apply();

            try {
                FileInputStream proez = openFileInput("proezas.txt");
                BufferedReader buf = new BufferedReader(new InputStreamReader(proez));

                String linha;
                ContentValues cv = new ContentValues();

                while((linha = buf.readLine()) != null) {
                    cv.put(oDBH.INFOP_COL2, linha);
                    DB.insert(oDBH.INFOP_TABLE, null, cv);
                }
                buf.close();
                proez.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void toMainMenu(View v) {

        users = new ArrayList<>();
        userList();

        oED = findViewById(R.id.ep_user);
        String user = oED.getText().toString();
        ContentValues cv = new ContentValues();
        Intent i = new Intent(this, mainmenu.class);

        if(user.length() < 12) {
            if(user.equals(""))
                user = "Guest";

            cv.put(oDBH.USER_COL1, user);
            i.putExtra("active", user);

            if(users.isEmpty() || !users.contains(user))
                DB.insert(oDBH.USER_TABLE, null, cv);

            oED.setText("");
            startActivity(i);
            overridePendingTransition(R.anim.go_in_dir, R.anim.go_out_esq);
        } else {
            outToast = Toast.makeText(getBaseContext(), "Nome tem que ser menor que 12 caracteres", Toast.LENGTH_LONG);
            outToast.show();
        }
    }


    @Override
    public void finish() {
        DB.close();
        super.finish();
        overridePendingTransition(R.anim.go_in_esq, R.anim.go_out_dir);
    }

    @Override
    public void onPause() { super.onPause(); }

    @Override
    public void onResume() { super.onResume(); }

    @Override
    public void onBackPressed() {
        if(backpress + 2000 > System.currentTimeMillis()) {
            outToast.cancel();
            super.onBackPressed();
        }
        else {
            outToast = Toast.makeText(getBaseContext(), "Pressiona mais uma vez para sair", Toast.LENGTH_SHORT);
            outToast.show();
        }
        backpress = System.currentTimeMillis();
    }



    public void userList() {

        Cursor oC = DB.rawQuery("SELECT " + oDBH.USER_COL1 + " FROM " + oDBH.USER_TABLE + ";", null);
        if(oC.getCount() > 0) {
            boolean dbLinha = oC.moveToFirst();
            while (dbLinha) {
                users.add(oC.getString(0));
                dbLinha = oC.moveToNext();
            }
        }
    }

    public void welcomeText() {

        TextView bigtext = findViewById(R.id.ep_text);
        byte[] buf;

        try {
            FileInputStream welcome = openFileInput("welcome.txt");
            buf = new byte[welcome.available()];
            welcome.read(buf);
            welcome.close();
            bigtext.setText(new String(buf));
        } catch(IOException e) {
            e.printStackTrace();
          }
    }


}
