package cc.ram9.jungle.sound;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import cc.ram9.jungle.sound.dummy.DummyContent;
import cc.ram9.jungle.sound.dummy.DummyContent.DummyItem;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class SampleListActivity extends FragmentActivity
        implements SampleListFragment.Callbacks {
	
	private SoundPool soundPool = null;
	Thread soundChannelThread = null;

	public HashMap loaded = new HashMap();
	public int loadedCount = 0;
	private class SoundPoolLoaded implements SoundPool.OnLoadCompleteListener {

		@Override
		public void onLoadComplete(SoundPool soundPool, int sampleId,
				int status) {
			System.out.print("Status is ");
			System.out.println(status);
			System.out.print("SampleId is");
			System.out.println(sampleId);
			if (status == 0 ) {
				loaded.put(loadedCount, sampleId);
				loadedCount++;
			}
		}
	}
	private class SoundChannel implements Runnable {
	
		
		
		
		public SoundChannel() {
						
		}
		public void run() {

			
			/* this is to ensure loading of all samples is complete */
			while(loadedCount < 3) {
				System.out.println("polling");
				try {
					Thread.currentThread().sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			/* this is actually playing the samples */
			/* if there is an error, the track is skipped and status -12 is visible in logcat */
			soundPool.play((Integer) loaded.get(0), 1.0f, 1.0f, 3, 1, 1.0f);
			soundPool.play((Integer) loaded.get(2), 1.0f, 1.0f, 2, 1, 1.0f);
			soundPool.play((Integer) loaded.get(1), 1.0f, 1.0f, 1, 2, 1.0f);
				
			
	 	
		}
	}
    private boolean mTwoPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_list);

        if (findViewById(R.id.sample_detail_container) != null) {
            mTwoPane = true;
            ((SampleListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.sample_list))
                    .setActivateOnItemClick(true);
        }
        
        /* this should be done while the application is loading */
        try {
        	AudioManager aum = null;
        	           AssetManager am = getAssets();
		
		aum = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		
		
		
		AssetFileDescriptor afd1 = am.openFd("Bass/19 Bass.wav");
		AssetFileDescriptor afd2 = am.openFd("Breaks/Atlantis Amen.wav");
		AssetFileDescriptor afd3 = am.openFd("RiffsArpsHits/Awesome Piano.wav");
		// This needs to be done in a loading phase - not dynamic
		
			soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
			soundPool.setOnLoadCompleteListener(new SoundPoolLoaded());	
		
		
		int refnum1 = soundPool.load(afd1, 1);
		int refnum2 = soundPool.load(afd2, 1);
		int refnum3 = soundPool.load(afd3, 1);
        
        float streamVolumeCurrent = aum.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = aum.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax; 
    	
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        
    }

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(SampleDetailFragment.ARG_ITEM_ID, id);
            SampleDetailFragment fragment = new SampleDetailFragment();
            fragment.setArguments(arguments);
            
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.sample_detail_container, fragment)
                    .commit();

        } else {
           System.out.println(id);
           	soundPool.stop(1);
           	soundPool.stop(2);
           	soundPool.stop(3);
			soundChannelThread = new Thread(new SoundChannel());
			soundChannelThread.start();
		    
			
        }
    }
}
