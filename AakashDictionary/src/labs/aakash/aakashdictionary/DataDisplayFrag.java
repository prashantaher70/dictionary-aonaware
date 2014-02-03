package labs.aakash.aakashdictionary;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("NewApi")
public class DataDisplayFrag extends Fragment{

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//Log.i("ELSE","In fragment");
		View rootView = inflater.inflate(R.layout.data_display, container, false);
		return rootView;
	}
	@Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        //Log.i("ELSE","OnStart");
        if (args != null) {
            // Set article based on argument passed in
            updateArticleView(args.getString("data"));
        }
    }
	public void updateArticleView(String data) {
		// TODO Auto-generated method stub
		//Log.i("afsdszdvcsdz","SET          nkzsdfjzc");
		TextView article = (TextView) this.getParentFragment().getActivity().findViewById(R.id.data);
        article.setText(data);
	}

}
