package labs.aakash.aakashdictionary;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class DictionaryMain extends FragmentActivity {

	 DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
	 ViewPager mViewPager;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_xml);
		
        mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager());

        // Set up action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dictionary_main, menu);
		return true;
		
	}
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_help:
	            Dialog help=new Dialog(this);
	            help.setTitle("Help");
	            help.setCancelable(true);
	            help.setContentView(R.layout.help_xml);
	            help.show();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	 public static class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {

	        public DemoCollectionPagerAdapter(FragmentManager fm) {
	            super(fm);
	        }

	        @Override
	        public android.support.v4.app.Fragment getItem(int i) {
	        	switch (i) {
	            case 0:
	                return new OnlineMode();
	            case 1:
	                return new History();
	            
	            default:
	                return null;
	            }
	        }

	        @Override
	        public int getCount() {
	            // For this contrived example, we have a 100-object collection.
	            return 2;
	        }
	        
	        @Override
	        public CharSequence getPageTitle(int i) {
	        	switch (i) {
	            case 0:
	                return "Search Online";
	            case 1:
	                return "History";
	            
	            default:
	                return null;
	            }
	        }
	    }

}
