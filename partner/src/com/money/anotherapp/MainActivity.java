package com.money.anotherapp;

import java.util.HashMap;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity {
	
	private static int REQUEST_CODE_MEASURE = 1;
	static MainActivity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        
        mActivity = this;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    static HashMap<Integer, String> map = new HashMap<Integer, String>();
    
    

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            Button measure = (Button) rootView.findViewById(R.id.start_measure);
            final RadioGroup group = (RadioGroup)rootView.findViewById(R.id.radioGroup);
            map.put(R.id.radioBP, "bp");
            map.put(R.id.radioBO, "bo");
            map.put(R.id.radioBS, "bs");
            map.put(R.id.radioOTHER, "other");
            measure.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					try{
						 int radioButtonId = group.getCheckedRadioButtonId();
						String module = map.get(radioButtonId);
						Intent intent = new Intent();
						Uri path = Uri.parse("mCloud://measure?module="+module+"&appid=1234&openid=1234&serial=1234&signature=1234");
						intent.setData(path);
						startActivityForResult(intent, REQUEST_CODE_MEASURE);
					}
					catch(Exception ex){
						ex.printStackTrace();
						Toast.makeText(mActivity, "请安装心云APP", Toast.LENGTH_SHORT).show(); 
					}
				}
			});
            return rootView;
        }
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) { 
        if ((requestCode&0xFF) == REQUEST_CODE_MEASURE) { 
            if (resultCode == RESULT_OK) { 
            	Uri path = data.getData();
				String scheme = path.getScheme();
				if(!scheme.equals("mc80132"))
					return;
				
				String result = "";
				String module = path.getQueryParameter("module");
				if(module.startsWith("bp")){
					String high = path.getQueryParameter("high");
					String low = path.getQueryParameter("low");
					String heart = path.getQueryParameter("rate");
					result ="您的测量结果:"+"高压："+ high +"低压："+low +"心率"+heart;
				}
				else if(module.startsWith("bo")){
					String oxygen = path.getQueryParameter("oxygen");
					String heart = path.getQueryParameter("rate");
					result ="您的测量结果:"+"血氧："+ oxygen  +"心率"+heart;
				}
 
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show(); 
            } 
            else if(resultCode == RESULT_CANCELED){
            	Toast.makeText(this, "您已取消测量", Toast.LENGTH_SHORT).show(); 
            }
        } 
    }

}
