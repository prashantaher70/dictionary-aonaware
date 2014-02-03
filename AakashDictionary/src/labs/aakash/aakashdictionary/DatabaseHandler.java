package labs.aakash.aakashdictionary;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHandler {

  // Database fields
  private SQLiteDatabase database;
  private MyDbHelper dbHelper;
  private String[] allColumns = { MyDbHelper.COLUMN_ID,
      MyDbHelper.COLUMN_WORD, MyDbHelper.COLUMN_MEANING};

  public DatabaseHandler(Context context) {
    dbHelper = new MyDbHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public WordListItem addWord(String word,String meaning) {
    ContentValues values = new ContentValues();
    values.put(MyDbHelper.COLUMN_WORD, word);
    values.put(MyDbHelper.COLUMN_MEANING, meaning);
    long insertId = database.insert(MyDbHelper.TABLE_WORD, null,
        values);
    Cursor cursor = database.query(MyDbHelper.TABLE_WORD,
        allColumns, MyDbHelper.COLUMN_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    WordListItem newWord = cursorToWord(cursor);
    cursor.close();
    return newWord;
  }

  public void deleteWord(WordListItem word) {
    String Word = word.toString();
    System.out.println("Word deleted: " + Word);
    database.delete(MyDbHelper.TABLE_WORD, MyDbHelper.COLUMN_WORD
        + " = '" + word+"'", null);
  }

  public List<WordListItem> getAllWords() {
    List<WordListItem> words = new ArrayList<WordListItem>();

    Cursor cursor = database.query(MyDbHelper.TABLE_WORD,
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
    	WordListItem comment = cursorToWord(cursor);
      words.add(comment);
      cursor.moveToNext();
    }
    // Make sure to close the cursor
    cursor.close();
    return words;
  }

  private WordListItem cursorToWord(Cursor cursor) {
    WordListItem word = new WordListItem();
    word.setWord(cursor.getString(1));
    word.setMeaning(cursor.getString(2));
    return word;
  }
  
  public boolean wordExist(String word)
  {
	  Cursor cursor = database.query(MyDbHelper.TABLE_WORD,
		        allColumns, MyDbHelper.COLUMN_WORD + " ='" + word+"'", null,
		        null, null, null);
	  cursor.moveToLast();
	  if(cursor.getCount()==0)
	  {
		  return false;
	  }
	  return true;
  }
}
