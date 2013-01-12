/*
 * Copyright (C) 2012 OUYA, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package us.ouya.soundcheck;

import java.io.IOException;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SoundCheckActivity extends Activity {
	
	private SoundPool soundPool = null;
	Thread soundChannelThread = null;

	public HashMap loaded = new HashMap();
	public int loadedCount = 0;
	private int refnum1;
	private int refnum2;
	private int refnum3;
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

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        /* this should be done while the application is loading */
        try {
        	AudioManager aum = null;
        	           AssetManager am = getAssets();
		
		aum = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		
		
		
		AssetFileDescriptor afd1 = am.openFd("Bass/19 Bass.wav");
		AssetFileDescriptor afd2 = am.openFd("Breaks/Atlantis Amen.wav");
		AssetFileDescriptor afd3 = am.openFd("RiffsArpsHits/Awesome Piano.wav");
		// This needs to be done in a loading phase - not dynamic
		
			soundPool = new SoundPool(10, // num samples
					AudioManager.STREAM_MUSIC, 
					0); // quality 0 default 
			soundPool.setOnLoadCompleteListener(new SoundPoolLoaded());	
		
		
		refnum1 = soundPool.load(afd1, 1);
		refnum2 = soundPool.load(afd2, 1);
		refnum3 = soundPool.load(afd3, 1);
        
        float streamVolumeCurrent = aum.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = aum.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax; 
    	
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        
        Button newGame = (Button) findViewById(R.id.soundpool_button);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            soundPool.stop(refnum1);
            soundPool.stop(refnum2);
            soundPool.stop(refnum3);
            
            soundChannelThread = new Thread(new SoundChannel());
    			soundChannelThread.start();
    		    	
            }
        });
        
        Button audiobutton = (Button) findViewById(R.id.audiotrack_button);
        audiobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	  final float frequency = 440;
                  float increment = (float)(2*Math.PI) * frequency / 44100; // angular increment for each sample
                  float angle = 0;
                  AndroidAudioDevice device = new AndroidAudioDevice( );
                  float samples[] = new float[1024];
       
                  while( true )
                  {
                     for( int i = 0; i < samples.length; i++ )
                     {
                    	 	float t = angle;
                        //samples[i] = (float)Math.sin( angle );
                        samples[i] = (float) (t * (Math.sin(t*12)+Math.sin(t*8)));
                        angle += increment;
                     }
       
                     device.writeSamples( samples );
                  }        	
            }
        });

        
        Button quit = (Button) findViewById(R.id.quit_game_button);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    // http://www.badlogicgames.com/wordpress/?p=228
    public class AndroidAudioDevice
    {
       AudioTrack track;
       short[] buffer = new short[1024];
     
       public AndroidAudioDevice( )
       {
          int minSize =AudioTrack.getMinBufferSize( 44100, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT );        
          track = new AudioTrack( AudioManager.STREAM_MUSIC, 44100, 
                                            AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, 
                                            minSize, AudioTrack.MODE_STREAM);
          track.play();        
       }	   
     
       public void writeSamples(float[] samples) 
       {	
          fillBuffer( samples );
          track.write( buffer, 0, samples.length );
       }
     
       private void fillBuffer( float[] samples )
       {
          if( buffer.length < samples.length )
             buffer = new short[samples.length];
     
          for( int i = 0; i < samples.length; i++ )
             buffer[i] = (short)(samples[i] * Short.MAX_VALUE);;
       }		
    }
}
/*
 * mason song
 * c -style proc
 * main(t){for(t=0;;t++)putchar(t*(((t>>12)|(t>>8))&(63&(t>>4))));}
 * ((30 * sin(floor(t/1000))) * cos(t << 17^(t * 2))) + ((30 * sin(floor(t/1000))) * cos(t << 17^(t * 2|t>>5)) * ceil(sin(floor(t/(8000 * 6))))) + ((80 * sin(floor(t/1000))) * cos(t << 16^(t * 2)) * ceil(sin(floor(t/(8000 * 15)))))
 */
