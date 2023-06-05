package pmd.di.ubi.pt.mainmenu;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;

public class infoPopup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infopopup);

        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        int c = display.widthPixels;
        int l = display.heightPixels;

        getWindow().setLayout((int)(c*0.8), (int)(l*0.8));
        infotext();
    }

    public void infotext() {

        TextView bigtext = findViewById(R.id.i_text);
        byte[] buf;

        try {
            FileInputStream info = openFileInput("info.txt");
            buf = new byte[info.available()];
            info.read(buf);
            info.close();
            bigtext.setText(new String(buf));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        super.finish();
    }
}
