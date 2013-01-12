package cc.ram9.jungle.sound;

import java.io.IOException;

import cc.ram9.jungle.sound.dummy.DummyContent;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class SampleDetailActivity extends FragmentActivity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_sample_detail);
        
        
        
        AssetManager am = getAssets();
        System.out.println("hello");
        try {
        	// not work
			String[] list = am.list("Bass");
			
			for( String name : list) {
				System.out.println(name);				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(SampleDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(SampleDetailFragment.ARG_ITEM_ID));
            SampleDetailFragment fragment = new SampleDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.sample_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, SampleListActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
