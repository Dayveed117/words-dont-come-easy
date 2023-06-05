package pmd.di.ubi.pt.mainmenu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "WordsDontComeEasy";
    private static final int DB_VERSION = 1;


    //Tabela do que contém informação relativa ao USER
    protected static final String USER_TABLE = "User";
    protected static final String USER_COL1 = "Nome";
    protected static final String USER_COL2 = "Nível";
    protected static final String USER_COL3 = "XP";
    protected static final String USER_COL4 = "Letras_Certas";
    protected static final String USER_COL5 = "Total_Letras";
    protected static final String USER_COL6 = "PR_Normal";
    protected static final String USER_COL7 = "PR_CR";
    protected static final String USER_COL8 = "PR_Hardcore";
    //Script para criar tabela USER
    private static final String CREATE_USER = "CREATE TABLE " + USER_TABLE + " ("
            + USER_COL1 + " VARCHAR(30) PRIMARY KEY, "
            + USER_COL2 + " INTEGER DEFAULT 1, "
            + USER_COL3 + " DECIMAL DEFAULT 0, "
            + USER_COL4 + " INTEGER DEFAULT 0, "
            + USER_COL5 + " INTEGER DEFAULT 0, "
            + USER_COL6 + " DECIMAL DEFAULT 0, "
            + USER_COL7 + " DECIMAL DEFAULT 0, "
            + USER_COL8 + " DECIMAL DEFAULT 0);";




    //Tabela do que contém informação relativa a PONTUACOES
    protected static final String PONTUACOES_TABLE = "Pontuacoes";
    protected static final String PONTUACOES_COL1 = USER_COL1;
    protected static final String PONTUACOES_COL2 = "GameMode";
    protected static final String PONTUACOES_COL3 = "Pontuacao";
    protected static final String PONTUACOES_COL4 = "Datatempo";
    //Script para criar PONTUACOES
    private static final String CREATE_PONTUACOES = "CREATE TABLE " + PONTUACOES_TABLE + " ("
            + PONTUACOES_COL1 + " REFERENCES " + USER_TABLE + "(" + PONTUACOES_COL1 + "), "
            + PONTUACOES_COL2 + " VARCHAR(20), "
            + PONTUACOES_COL3 + " DECIMAL, "
            + PONTUACOES_COL4 + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + "PRIMARY KEY(" + PONTUACOES_COL1 + ", " + PONTUACOES_COL4 + "));";




    //Tabela do que contém informação relativa a PROEZAS
    protected static final String INFOP_TABLE = "infoProezas";
    protected static final String INFOP_COL1 = "ID_proeza";
    protected static final String INFOP_COL2 = "Descricao";

    private static final String CREATE_INFOP = "CREATE TABLE " + INFOP_TABLE + " ("
            + INFOP_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1, "
            + INFOP_COL2 + " VARCHAR(100));";




    //Tabela do que contém informação relativa a PROEZAS
    protected static final String PROEZAS_TABLE = "Proezas";
    protected static final String PROEZAS_COL1 = USER_COL1;
    protected static final String PROEZAS_COL2 = "ID_proeza";
    protected static final String PROEZAS_COL3 = "Dia";
    //Script para criar PROEZAS
    private static final String CREATE_PROEZAS = "CREATE TABLE " + PROEZAS_TABLE + " ("
            + PROEZAS_COL1 + " REFERENCES " + USER_TABLE + "(" + PROEZAS_COL1 + "), "
            + PROEZAS_COL2 + " REFERENCES " + INFOP_TABLE + "(" + INFOP_COL1 + "), "
            + PROEZAS_COL3 + " DATETIME DEFAULT CURRENT_DATE, "
            + "PRIMARY KEY(" + PROEZAS_COL1 + ", " + PROEZAS_COL2 + "));";




    public dbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA FOREIGN_KEYS=ON;");
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_PONTUACOES);
        db.execSQL(CREATE_INFOP);
        db.execSQL(CREATE_PROEZAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + INFOP_TABLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + PROEZAS_TABLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + PONTUACOES_TABLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE + ";");
        onCreate(db);
    }
}
