package pmd.di.ubi.pt.mainmenu;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class partida extends Activity {

    private ConstraintLayout CL;
    private TextView labeltimer;
    private TextView timertext;
    private TextView onScreenWord;
    private long backpress;
    private Toast outToast;
    private boolean PRflag = false;

    // Essenciais para o jogo
    private CountDownTimer ctTimer;
    private long timeinmls = 60000; // 1 minuto
    private String rightword;
    private char butCH;
    private char rightCH;
    private Button skipWord;
    private Button quit;
    private boolean over = false;


    // Tracking essencials
    private int total_presses = 0;
    private TextView oTVscore;
    private double pontos = 0;
    private int tryCounter = 1;
    private int tryRight = 0;
    private String user;
    private String gamemode;
    private int butoes;

    // os 10 butoes
    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    private Button b5;
    private Button b6;
    private Button b7;
    private Button b8;
    private Button b9;
    private Button b10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida);

        quit = findViewById(R.id.safequit);
        onScreenWord = findViewById(R.id.palavra);
        timertext = findViewById(R.id.secs);
        oTVscore = findViewById(R.id.points);
        CL = findViewById(R.id.fundo);

        // Butões necessários
        skipWord = findViewById(R.id.nextword);
        skipWord.setEnabled(false);

        b1 = findViewById(R.id.but1);
        b2 = findViewById(R.id.but2);
        b3 = findViewById(R.id.but3);
        b4 = findViewById(R.id.but4);
        b5 = findViewById(R.id.but5);
        b6 = findViewById(R.id.but6);
        b7 = findViewById(R.id.but7);
        b8 = findViewById(R.id.but8);
        b9 = findViewById(R.id.but9);
        b10 = findViewById(R.id.but10);

        Intent i = getIntent();
        user = i.getStringExtra("active");
        gamemode = i.getStringExtra("mode");
        butoes = i.getIntExtra("buttons", -1);

        chosenGamemode();
    }

    public void chosenGamemode() {

        switch (gamemode) {

            case "Normal":

                CL.setBackgroundResource(R.color.normal);
                labeltimer = findViewById(R.id.timer);
                labeltimer.setEnabled(false);
                timertext.setEnabled(false);

                quit.setEnabled(true);

                b6.setEnabled(false);
                b7.setEnabled(false);
                b8.setEnabled(false);
                b9.setEnabled(false);
                b10.setEnabled(false);

                gameset();

                break;

            case "Contra-Relógio":

                CL.setBackgroundResource(R.color.cr);
                countdown();

                b8.setEnabled(false);
                b9.setEnabled(false);
                b10.setEnabled(false);

                gameset();

                break;

            case "Hardcore":

                CL.setBackgroundResource(R.color.hardcore);
                timeinmls = 30000;
                countdown();

                gameset();

                break;
        }
    }

    public void countdown() {

        ctTimer = new CountDownTimer(timeinmls, 1000) {
            @Override
            public void onTick(long secs) {
                timeinmls = secs;
                timertext.setText(Long.toString(timeinmls/1000));
            }

            @Override
            public void onFinish() {
                disableAll();
                onScreenWord.setText("Game!");
                timertext.setText("done");
                quit.setEnabled(true);


            }
        }.start();
    }

    protected void disableAll() {
        b1.setEnabled(false);
        b2.setEnabled(false);
        b3.setEnabled(false);
        b4.setEnabled(false);
        b5.setEnabled(false);
        b6.setEnabled(false);
        b7.setEnabled(false);
        b8.setEnabled(false);
        b9.setEnabled(false);
        b10.setEnabled(false);
        skipWord.setEnabled(false);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.go_in_esq, R.anim.go_out_dir);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if(backpress + 2000 > System.currentTimeMillis()) {
            outToast.cancel();
            super.onBackPressed();
            overridePendingTransition(R.anim.go_in_esq, R.anim.go_out_dir);
        }
        else {
            outToast = Toast.makeText(getBaseContext(), "Queres mesmo sair sem guardar?", Toast.LENGTH_LONG);
            outToast.show();
        }
        backpress = System.currentTimeMillis();
    }



    // FUNÇÕES IMPORTANTES


    // Método que avalia o "toque" do jogador
    public void letterTap(View v) {

        total_presses++;
        int id = v.getId();
        Button b = findViewById(id);
        butCH = b.getText().charAt(0);

        if(butCH == rightCH) {
            switch(gamemode) {
                case "Contra-Relógio":
                    pontos += calculateScore()*1.75;
                    oTVscore.setText(String.format("%.2f", pontos));
                    break;
                case "Hardcore":
                    pontos += calculateScore()*2.5;
                    oTVscore.setText(String.format("%.2f", pontos));
                    timeinmls += 3000;
                    ctTimer.cancel();
                    countdown();
                    break;
                default:
                    pontos += calculateScore();
                    oTVscore.setText(String.format("%.2f", pontos));
                    break;
            }
            skipWord.setEnabled(false);
            tryRight++;
            tryCounter = 1;
            gameset();
        }
        else {
            tryCounter++;
            if(tryCounter == 6)
                skipWord.setEnabled(true);

            if(gamemode.equals("Hardcore")) {
                if(timeinmls < 6000) {
                    ctTimer.cancel();
                    ctTimer.onFinish();
                }
                else {
                    timeinmls -= 6000;
                    ctTimer.cancel();
                    countdown();
                }
            }
        }
    }

    // Passar a palavra à frente
    public void passarPalavra(View v) {
        tryCounter = 1;
        skipWord.setEnabled(false);
        gameset();
    }

    // Chamar para começar a jogar
    public void gameset() {
        rightword = getDictWord().toUpperCase();
        char rightchar = RightChar(rightword);
        rightCH = rightchar;
        distribuirChars(rightchar);
    }

    // Cálculo da pontuação atual numa partida
    public double calculateScore() { return (100/tryCounter); }


    // Retira o char da string que o jogador tem de preencher
    public char RightChar(String s) {

        StringBuilder sb = new StringBuilder(s);
        int N = (int)(Math.random()*sb.length());
        char theCHAR = sb.charAt(N);

        onScreenWord = findViewById(R.id.palavra);
        onScreenWord.setText(sb.replace(N,N+1,"_"));

        return theCHAR;
    }


    // Créditos ao dário por me ajudar nesta parte fulcral
    // Retira uma palavra random do dicionário
    // 998431 linhas no dicionário com 998430 palavras
    public String getDictWord() {

        String line = "anti-erro";
        int N = (int)(Math.random()*998431);
        int i;

        try {
            FileInputStream dict = openFileInput("dicionario_big.txt");
            BufferedReader buf = new BufferedReader(new InputStreamReader(dict));

            for(i=0; i<N; i++)
                buf.readLine();

            line = buf.readLine();
            buf.close();
            dict.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    // Obter um array de chars que serão as letras a aparecer no ecrã
    // Algumas vezes é capaz de aparecer uma letra repetida que possa ser a correta, ainda não ser como aparecer todas únicas
    public char[] getChars(ArrayList<Character> list, char c) {

        StringBuilder buff = new StringBuilder();
        Collections.shuffle(list);

        for(int i=0; i<butoes; i++) {
            buff.append(list.get(i));
        }

        //Para nao haver butoes com a mesma letra
        String temp = buff.toString();
        if(temp.contains(String.valueOf(c)))
            return buff.toString().toCharArray();
        else {
            int rand = (int) (Math.random() * buff.length());
            buff.replace(rand, rand + 1, String.valueOf(c));
            return buff.toString().toCharArray();
        }
    }

    // Distribuir chars pelos butões dependendo do modo de jogo
    public void distribuirChars(char ch) {

        // Lista dos caracteres válidos
        char[] validchars = "ABCÇDEFGHIJKLMNOPQRSTUVWXYZÀÁÂÃÉÈÊÌÍÎÒÓÕÔÙÚÛ-".toCharArray();
        char[] chars;


        // Gerar um array list para poder usar o shuffle(faz aleatoriedade única, pelo que percebi)
        ArrayList<Character> charlist = new ArrayList<>();
        for(char c: validchars)
            charlist.add(c);

        switch(butoes) {

            case 5:

                chars = getChars(charlist, ch);

                b1.setText(String.valueOf(chars[0]));
                b2.setText(String.valueOf(chars[1]));
                b3.setText(String.valueOf(chars[2]));
                b4.setText(String.valueOf(chars[3]));
                b5.setText(String.valueOf(chars[4]));

                break;

            case 7:

                chars = getChars(charlist, ch);

                b1.setText(String.valueOf(chars[0]));
                b2.setText(String.valueOf(chars[1]));
                b3.setText(String.valueOf(chars[2]));
                b4.setText(String.valueOf(chars[3]));
                b5.setText(String.valueOf(chars[4]));
                b6.setText(String.valueOf(chars[5]));
                b7.setText(String.valueOf(chars[6]));

                break;

            case 10:

                chars = getChars(charlist, ch);

                b1.setText(String.valueOf(chars[0]));
                b2.setText(String.valueOf(chars[1]));
                b3.setText(String.valueOf(chars[2]));
                b4.setText(String.valueOf(chars[3]));
                b5.setText(String.valueOf(chars[4]));
                b6.setText(String.valueOf(chars[5]));
                b7.setText(String.valueOf(chars[6]));
                b8.setText(String.valueOf(chars[7]));
                b9.setText(String.valueOf(chars[8]));
                b10.setText(String.valueOf(chars[9]));

                break;
        }
    }

    public void insert_and_updateData() {

        dbHelper oDBH = new dbHelper(this);
        SQLiteDatabase DB = oDBH.getReadableDatabase();

        // Adquirir dados do current user
        Cursor oC = DB.rawQuery("SELECT * FROM " + oDBH.USER_TABLE + " WHERE " + oDBH.USER_COL1 + " ='" + user + "';", null);
        if(oC.moveToFirst()) {
            double user_xp = (oC.getDouble(2));
            int user_wordsR = (oC.getInt(3));
            int user_wordsT = (oC.getInt(4));

            int new_userlvl;
            double new_userxp = user_xp+pontos;


            if(new_userxp < 1000)
                new_userlvl = 1;
            else
                new_userlvl = (int)(new_userxp/1000)+1;


            int new_userwordsR = user_wordsR+tryRight;
            int new_userwordsT = user_wordsT+total_presses;

            ContentValues cv = new ContentValues();
            cv.put(oDBH.USER_COL2, new_userlvl);
            cv.put(oDBH.USER_COL3, new_userxp);
            cv.put(oDBH.USER_COL4, new_userwordsR);
            cv.put(oDBH.USER_COL5, new_userwordsT);

            switch (gamemode) {

                case "Normal":
                    double user_PRnormal = (oC.getDouble(5));
                    if (pontos > user_PRnormal) {
                        cv.put(oDBH.USER_COL6, pontos);
                        PRflag = true;
                    }
                    break;

                case "Contra-Relógio":
                    double user_PRCR = (oC.getDouble(6));
                    if (pontos > user_PRCR) {
                        cv.put(oDBH.USER_COL7, pontos);
                        PRflag = true;
                    }
                    break;

                case "Hardcore":
                    double user_PRhardcore = (oC.getDouble(7));
                    if(pontos > user_PRhardcore) {
                        cv.put(oDBH.USER_COL8, pontos);
                        PRflag = true;
                    }
                    break;
            }

            // Achievements - muito clutter i hate it mas assim é cómodo devido às variáveis acima
            // im really sorry whoever reads this
            Cursor oC2;

            for(int i=1; i<19; i++) {

                oC2 = DB.rawQuery("SELECT * FROM " + oDBH.PROEZAS_TABLE + " WHERE "
                        + oDBH.PROEZAS_COL1 + "='" + user +"' AND "
                        + oDBH.PROEZAS_COL2 + "=" + i +";", null);

                if(!oC2.moveToFirst()) {

                    ContentValues pr_cv = new ContentValues();

                    switch(i) {
                            // lvl 5
                            case 1:
                                if(new_userlvl >= 5) {
                                    pr_cv.put(oDBH.PROEZAS_COL1, user);
                                    pr_cv.put(oDBH.PROEZAS_COL2, i);
                                    DB.insert(oDBH.PROEZAS_TABLE, null, pr_cv);
                                }
                                break;
                            // lvl 15
                            case 2:
                                if(new_userlvl >= 15) {
                                    pr_cv.put(oDBH.PROEZAS_COL1, user);
                                    pr_cv.put(oDBH.PROEZAS_COL2, i);
                                    DB.insert(oDBH.PROEZAS_TABLE, null, pr_cv);
                                }
                                break;
                            // lvl 30
                            case 3:
                                if(new_userlvl >= 30) {
                                    pr_cv.put(oDBH.PROEZAS_COL1, user);
                                    pr_cv.put(oDBH.PROEZAS_COL2, i);
                                    DB.insert(oDBH.PROEZAS_TABLE, null, pr_cv);
                                }
                                break;
                            // lvl 50
                            case 4:
                                if(new_userlvl >= 50) {
                                    pr_cv.put(oDBH.PROEZAS_COL1, user);
                                    pr_cv.put(oDBH.PROEZAS_COL2, i);
                                    DB.insert(oDBH.PROEZAS_TABLE, null, pr_cv);
                                }
                                break;
                            // acabar partida com > 4000
                            case 5:
                                if(pontos >= 4000) {
                                    pr_cv.put(oDBH.PROEZAS_COL1, user);
                                    pr_cv.put(oDBH.PROEZAS_COL2, i);
                                    DB.insert(oDBH.PROEZAS_TABLE, null, pr_cv);
                                }
                                break;
                            // acabar partida com > 6000
                            case 6:
                                if(pontos >= 6000) {
                                    pr_cv.put(oDBH.PROEZAS_COL1, user);
                                    pr_cv.put(oDBH.PROEZAS_COL2, i);
                                    DB.insert(oDBH.PROEZAS_TABLE, null, pr_cv);
                                }
                                break;
                            // acabar partida com > 8000
                            case 7:
                                if(pontos >= 8000) {
                                    pr_cv.put(oDBH.PROEZAS_COL1, user);
                                    pr_cv.put(oDBH.PROEZAS_COL2, i);
                                    DB.insert(oDBH.PROEZAS_TABLE, null, pr_cv);
                                }
                                break;
                            // acabar partida com > 10000
                            case 8:
                                if(pontos >= 10000) {
                                    pr_cv.put(oDBH.PROEZAS_COL1, user);
                                    pr_cv.put(oDBH.PROEZAS_COL2, i);
                                    DB.insert(oDBH.PROEZAS_TABLE, null, pr_cv);
                                }
                                break;
                            // acabar partida com > 20000
                            case 9:
                                if(pontos >= 20000) {
                                    pr_cv.put(oDBH.PROEZAS_COL1, user);
                                    pr_cv.put(oDBH.PROEZAS_COL2, i);
                                    DB.insert(oDBH.PROEZAS_TABLE, null, pr_cv);
                                }
                                break;
                            // PR 5000 normal
                            case 10:
                                if(pontos >= 6000 && gamemode.equals("Normal")) {
                                    pr_cv.put(oDBH.PROEZAS_COL1, user);
                                    pr_cv.put(oDBH.PROEZAS_COL2, i);
                                    DB.insert(oDBH.PROEZAS_TABLE, null, pr_cv);
                                }
                                break;
                            // PR 10000 cr
                            case 11:
                                if(pontos >= 10000 && gamemode.equals("Contra-Relógio")) {
                                    pr_cv.put(oDBH.PROEZAS_COL1, user);
                                    pr_cv.put(oDBH.PROEZAS_COL2, i);
                                    DB.insert(oDBH.PROEZAS_TABLE, null, pr_cv);
                                }
                                break;
                            // PR 15000 hardcore
                            case 12:
                                if(pontos >= 15000 && gamemode.equals("Harcore")) {
                                    pr_cv.put(oDBH.PROEZAS_COL1, user);
                                    pr_cv.put(oDBH.PROEZAS_COL2, i);
                                    DB.insert(oDBH.PROEZAS_TABLE, null, pr_cv);
                                }
                                break;
                            // completar 20 palavras em ct
                            case 13:
                                if(tryRight > 20 && gamemode.equals("Contra-Relógio")) {
                                    pr_cv.put(oDBH.PROEZAS_COL1, user);
                                    pr_cv.put(oDBH.PROEZAS_COL2, i);
                                    DB.insert(oDBH.PROEZAS_TABLE, null, pr_cv);
                                }
                                break;
                            // completar 40 palavras em ct
                            case 14:
                                if(tryRight > 40 && gamemode.equals("Contra-Relógio")) {
                                    pr_cv.put(oDBH.PROEZAS_COL1, user);
                                    pr_cv.put(oDBH.PROEZAS_COL2, i);
                                    DB.insert(oDBH.PROEZAS_TABLE, null, pr_cv);
                                }
                                break;
                            // completar 55 palavras em ct
                            case 15:
                                if(tryRight > 55 && gamemode.equals("Contra-Relógio")) {
                                    pr_cv.put(oDBH.PROEZAS_COL1, user);
                                    pr_cv.put(oDBH.PROEZAS_COL2, i);
                                    DB.insert(oDBH.PROEZAS_TABLE, null, pr_cv);
                                }
                                break;
                            // completar 10 palavras em hardcore
                            case 16:
                                if(tryRight > 10 && gamemode.equals("Harcore")) {
                                    pr_cv.put(oDBH.PROEZAS_COL1, user);
                                    pr_cv.put(oDBH.PROEZAS_COL2, i);
                                    DB.insert(oDBH.PROEZAS_TABLE, null, pr_cv);
                                }
                                break;
                            // completar 20 palavras em hardcore
                            case 17:
                                if(tryRight > 20 && gamemode.equals("Harcore")) {
                                    pr_cv.put(oDBH.PROEZAS_COL1, user);
                                    pr_cv.put(oDBH.PROEZAS_COL2, i);
                                    DB.insert(oDBH.PROEZAS_TABLE, null, pr_cv);
                                }
                                break;
                            // completar 30 palavras em hardcore
                            case 18:
                                if(tryRight > 30 && gamemode.equals("Harcore")) {
                                    pr_cv.put(oDBH.PROEZAS_COL1, user);
                                    pr_cv.put(oDBH.PROEZAS_COL2, i);
                                    DB.insert(oDBH.PROEZAS_TABLE, null, pr_cv);
                                }
                                break;
                    }
                }
            }

            // Actualizar a informação do User consoante os resultados do jogo
            DB.update(oDBH.USER_TABLE, cv, oDBH.USER_COL1+" = ?", new String[] {user});
        }

        // Fazer registo desta instancia do jogo nas classificações
        ContentValues cv = new ContentValues();
        cv.put(oDBH.PONTUACOES_COL1, user);
        cv.put(oDBH.PONTUACOES_COL2, gamemode);
        cv.put(oDBH.PONTUACOES_COL3, pontos);

        DB.insert(oDBH.PONTUACOES_TABLE, null, cv);

        //Verificar e inserir proezas

        DB.close();
    }

    // Sair e guardar dados, chamar nova atividade caso disso
    public void safequit(View v) {
        insert_and_updateData();
        Toast.makeText(getBaseContext(), "Dados atualizados, podes sair!", Toast.LENGTH_SHORT).show();
        if(PRflag) {
            Intent i = new Intent(this, highscorepopup.class);
            i.putExtra("active", user);
            i.putExtra("mode", gamemode);
            i.putExtra("newPR", pontos);
            startActivityForResult(i, 1);
        }
        finish();
    }



    /*
    Eventualmente esta será a função para aumentar a dificuldade??
    public String how_hard(String s) {
        int tam = s.length();
        int hifens = s.length() - s.replace("-", "").length();

        if(tam < 7)
            return "Easy";
        else if(tam < 14 || (tam < 14 && hifens == 1))
            return "Medium";
        else if(hifens > 1)
            return "VeryHard";
        else
            return "Hard";
    }*/
}
