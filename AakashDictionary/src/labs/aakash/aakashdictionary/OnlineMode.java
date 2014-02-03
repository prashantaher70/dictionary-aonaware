package labs.aakash.aakashdictionary;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import labs.aakash.aakashdictionary.DictionaryParser.Definitions;

import org.apache.http.conn.ConnectTimeoutException;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class OnlineMode extends Fragment  implements OnClickListener{

	EditText searchWord;
	TextView searchResult;
	Button searchButton;
	private static String url="http://services.aonaware.com/DictService/DictService.asmx/Define?word=";
	private String searchKey=null;
	Fragment current;
	private DatabaseHandler handler=null;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		current=this;
		searchButton=(Button)this.getView().findViewById(R.id.searchBt);
		searchWord=(EditText)this.getView().findViewById(R.id.searchWord);
		searchResult=(TextView)this.getView().findViewById(R.id.searchResult);
		searchButton.setOnClickListener(this);
		handler=new DatabaseHandler(current.getActivity());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.activity_dictionary_online, container, false);
		return rootView;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		searchKey=searchWord.getText().toString();
		searchKey=searchKey.trim();
		if(!searchKey.equals(""))
		{
			StringBuilder URL=new StringBuilder(url);
			URL.append(searchKey);
			String fullURL=URL.toString();
			new DownloadXmlTask().execute(fullURL);
		}
		else{
			Toast.makeText(current.getActivity(),"Hey dumbo,you forgot to enter the word.",Toast.LENGTH_SHORT).show();
		}
	}
	
	private class DownloadXmlTask extends AsyncTask<String, Void, String>
	{
		ProgressDialog progressBar=null;
		@Override
		protected String doInBackground(String... urls) {
			// TODO Auto-generated method stub
			String result=null;
			result=getData(urls[0]);
			return result;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
			progressBar=new ProgressDialog(current.getActivity());
			progressBar.setCancelable(false);
			progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressBar.setTitle("Loading definition.Please wait..");
			progressBar.show();
		}

		@Override
		protected void onPostExecute(String result) {
			searchResult.setText(result+"\n");
			Log.i("MAIN","ON POST RESULT " +result);
			if(progressBar!=null) progressBar.dismiss();
		}
		
	}
	private String getData(String url)
	{
		InputStream stream=null;
		DictionaryParser parser=new DictionaryParser();
		List<Definitions> definitions=null;
		try {
			stream=getStream(url);
			definitions=parser.parse(stream);
		}catch(ConnectTimeoutException e){
			current.getActivity().runOnUiThread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(current.getActivity(),"Connection Timeout",Toast.LENGTH_SHORT).show();
				}
				
			});
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			current.getActivity().runOnUiThread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(current.getActivity(),"Please Check Your Internet Connection",Toast.LENGTH_LONG).show();
				}
				
			});
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			current.getActivity().runOnUiThread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(current.getActivity(),"Unable to parse the received information.",Toast.LENGTH_LONG).show();
				}
			});
		}
		finally
		{
			if(stream !=null)
			{
				try {
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		Log.i("MAIN","GOT DATA");
		StringBuilder dataString = new StringBuilder();
		if(definitions !=null)
		{
			if(definitions.size()!=0)
			{
				int i=0;
				for(i=0;i<definitions.size();i++)
				{
					if(definitions.get(i).dictionary.equals("WordNet (r) 2.0"))
					{
						Definitions definitionOne=definitions.get(i);
						//dataString.append("Source:WordNet\n");
						dataString.append(definitionOne.wordDefinition);
						Log.i("MAIN","FIRST ENTRY RESULT "+dataString.toString());
						handler.open();
						Log.i("dchgfhj","chgfvhyjghjgjkhkjh");
						if(!handler.wordExist(searchKey))
						{
							//Log.i("dchgfhj","chgfvhyjghjgjkhkjh");
							handler.addWord(searchKey, dataString.toString());
						}
						handler.close();
						break;
					}
				}
				if(dataString.length()==0)
				{
					dataString.append("Not found on WordNet.\nYou may try another sources");
				}
			}
			else
			{
				dataString.append("\nNo such word found");
			}
		}
		return dataString.toString();
	}
	private InputStream getStream(String fullURL) throws IOException
	{
		Log.i("MAIN","GET STREAM");
		URL sourceWebsite=null;
		HttpURLConnection conn=null;
		sourceWebsite=new URL(fullURL);
		conn = (HttpURLConnection) sourceWebsite.openConnection();
		conn.setReadTimeout(100000 /* milliseconds */);
	    conn.setConnectTimeout(150000 /* milliseconds */);
	    conn.setRequestMethod("GET");
	    conn.setDoInput(true);
	    // Starts the query
	    conn.connect();
	    Log.i("MAIN","GET STREAM CONNECTED");
	    return conn.getInputStream();
	}
}
