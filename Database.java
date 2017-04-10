import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Iterator;
import java.util.Map;

class Database extends SQLiteOpenHelper {

    //private static final String DATABASE_PATH = "/data/data/YOUR PACKAGE NAME/databases/";
    private static int DATABASE_VERSION;
    private static String DATABASE_NAME;
    private static String TABLE_NAME;
    private static Map<String, String> COLUMN;
    private Context context;

    public Database(Context context, int DATABASE_VERSION, String DATABASE_NAME, String TABLE_NAME, Map<String, String> COLUMN) throws ClassNotFoundException {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.context = context;
        Database.DATABASE_VERSION = DATABASE_VERSION;
        Database.DATABASE_NAME = DATABASE_NAME;
        Database.TABLE_NAME = TABLE_NAME;
        Database.COLUMN = COLUMN;
    }

    public Database(Context context, SQLiteDatabase.CursorFactory factory, int DATABASE_VERSION, String DATABASE_NAME, String TABLE_NAME, Map<String, String> COLUMN) throws ClassNotFoundException {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

        this.context = context;
        Database.DATABASE_VERSION = DATABASE_VERSION;
        Database.DATABASE_NAME = DATABASE_NAME;
        Database.TABLE_NAME = TABLE_NAME;
        Database.COLUMN = COLUMN;
    }

    public Database(Context context, SQLiteDatabase.CursorFactory factory, int DATABASE_VERSION, String DATABASE_NAME, String TABLE_NAME, Map<String, String> COLUMN, DatabaseErrorHandler errorHandler) throws ClassNotFoundException {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION, errorHandler);

        this.context = context;
        Database.DATABASE_VERSION = DATABASE_VERSION;
        Database.DATABASE_NAME = DATABASE_NAME;
        Database.TABLE_NAME = TABLE_NAME;
        Database.COLUMN = COLUMN;
    }

    public int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public Map<String, String> getCOLUMN() {
        return COLUMN;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(";

        Iterator<Map.Entry<String, String>> iterator = COLUMN.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();

            CREATE_TABLE += entry.getKey() + " " + entry.getValue();

            if (iterator.hasNext()) {
                CREATE_TABLE += ",";
            }
        }

        CREATE_TABLE += " )";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        DATABASE_VERSION = newVersion;

        onCreate(db);
    }

    public long addItem(String... args) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        Iterator<Map.Entry<String, String>> iterator = COLUMN.entrySet().iterator();

        int i = 0;

        for (String arg : args) {
            Log.d("DATABASE", arg);
        }
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();


            values.put(entry.getKey(), args[i++]);
        }

        return db.insert(TABLE_NAME, null, values);

    }

    public Cursor getAllData() {
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from " + TABLE_NAME, null);
    }

    public long updateData(String... args) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        Iterator<Map.Entry<String, String>> iterator = COLUMN.entrySet().iterator();

        int i = 0;

        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();

            values.put(entry.getKey(), args[i]);
        }

        return db.update(TABLE_NAME, values, "id = ?", new String[]{args[0]});

    }

    public long deleteData(String id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(TABLE_NAME, "ID + ?", new String[]{id});

    }

    public void deleteAllData() {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("delete from " + TABLE_NAME);
    }

}
