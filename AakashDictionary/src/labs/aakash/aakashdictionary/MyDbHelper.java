package labs.aakash.aakashdictionary;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class MyDbHelper extends SQLiteOpenHelper{

	public static final String TABLE_WORD = "WordTable";
	  public static final String COLUMN_ID = "ID";
	  public static final String COLUMN_WORD = "Word";
	  public static final String COLUMN_MEANING = "Meaning";
	  private static final String DATABASE_NAME = "AakashDictionary.db";
	  private static final int DATABASE_VERSION = 1;

	  // Database creation sql statement
	  private static final String DATABASE_CREATE = "create table "
	      + TABLE_WORD + "(" + COLUMN_ID
	      + " integer primary key autoincrement, " + COLUMN_WORD
	      + " text not null, "+ COLUMN_MEANING+" text not null);";

	  public MyDbHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(MyDbHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORD);
	    onCreate(db);
	  }

}
