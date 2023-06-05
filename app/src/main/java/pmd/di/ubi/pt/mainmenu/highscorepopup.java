package pmd.di.ubi.pt.mainmenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.nio.channels.InterruptedByTimeoutException;

public class highscorepopup extends Activity {

    private String user;
    private String gamemode;
    private double pontos;

    private ConstraintLayout CL;
    private TextView pr_user;
    private TextView pr;
    private TextView decider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscorepopup);

        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        int c = display.widthPixels;
        int l = display.heightPixels;

        getWindow().setLayout((int)(c*0.8), (int)(l*0.7));

        Intent i = getIntent();

        user = i.getStringExtra("active");
        gamemode = i.getStringExtra( "mode");
        pontos = i.getDoubleExtra("newPR", -1);

        CL = findViewById(R.id.pr_fundo);
        pr = findViewById(R.id.newpr);
        decider = findViewById(R.id.frase1);
        pr_user = findViewById(R.id.mr_user);

        pr_user.setText(user);
        decider.setText("O teu novo Recorde Pessoal em " + gamemode + " é :" );
        pr.setText(Double.toString(pontos));

    }

    @Override
    public void finish() {
        super.finish();
    }

    public void share(View v) {

        String cabeca = "Novo recorde de " + user + " em WordsDontComeEasy!";
        String corpo = user + " fez " + pontos + " pontos!!";

        Intent shareI = new Intent(Intent.ACTION_SEND);
        shareI.setType("text/plain");
        shareI.putExtra(Intent.EXTRA_SUBJECT, cabeca);
        shareI.putExtra(Intent.EXTRA_TEXT, corpo);

        startActivity(Intent.createChooser(shareI, "Partilhar para:"));
    }
}
