package labs.aakash.aakashdictionary;

import java.util.List;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class History extends Fragment implements OnItemClickListener{

	private ListView historyList;
	private String[] test={"Hello","Hi","What"};
	private List<WordListItem> wordList;
	private DataDisplayFrag dataFrag;
	private DatabaseHandler dbHandler;
	private WordListItem word;
	@Override
	public void onPause() {
	    dbHandler.close();
	    super.onPause();
	  }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		dbHandler=new DatabaseHandler(this.getActivity());
		dbHandler.open();
		wordList=dbHandler.getAllWords();
		historyList=(ListView)this.getView().findViewById(R.id.searchList);
		historyList.setAdapter(new ArrayAdapter<WordListItem>(getActivity(), android.R.layout.simple_list_item_1, wordList));
		historyList.setOnItemClickListener(this);
		//historyList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		//historyList.setSelector(drawable.list_selector_background);
		//Log.i("ELSE","Activity created");
		dataFrag = new DataDisplayFrag();
		if(!historyList.getAdapter().isEmpty())
		{
			word=(WordListItem) historyList.getAdapter().getItem(0);
			Bundle args = new Bundle();
	        args.putString("data", word.getMeaning());
	        dataFrag.setArguments(args);
		}
        this.getChildFragmentManager().beginTransaction()
                .add(R.id.wordData, dataFrag).commit();
        registerForContextMenu(historyList);
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
		View rootView = inflater.inflate(R.layout.history_mode, container, false);
		return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		// TODO Auto-generated method stub
		
		WordListItem word=(WordListItem) historyList.getAdapter().getItem(position);
		if (dataFrag != null) {
            dataFrag.updateArticleView(word.getMeaning());

        } else {
        	dataFrag = new DataDisplayFrag();
            Bundle args = new Bundle();
            args.putString("data", word.getMeaning());
            dataFrag.setArguments(args);
            FragmentManager fragmentManager = this.getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            
			fragmentTransaction.add(R.id.wordData,dataFrag);  
			fragmentTransaction.commit();
            
			fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
		//historyList.setItemChecked(position,true);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		WordListItem word=(WordListItem) historyList.getAdapter().getItem(info.position);
		@SuppressWarnings("unchecked")
		ArrayAdapter<WordListItem> adapter = (ArrayAdapter<WordListItem>) historyList.getAdapter();
		switch(item.getItemId()){
	        case R.id.delete:
	        	dbHandler.deleteWord(word);
	        	adapter.remove(word);
	        	adapter.notifyDataSetChanged();
				Toast.makeText(this.getActivity(),"Entry deleted successfully",Toast.LENGTH_SHORT).show();
				break;
	            
		}
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = this.getActivity().getMenuInflater();
	    inflater.inflate(R.menu.context_menu, menu);

	}
	

}
