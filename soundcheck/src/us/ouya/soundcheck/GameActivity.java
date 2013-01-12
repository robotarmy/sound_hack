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

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import tv.ouya.console.api.OuyaController;

import static us.ouya.soundcheck.R.*;

public class GameActivity extends Activity {
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean handled = OuyaController.onKeyDown(keyCode, event);
        //event.getDeviceId() // what controller
        	System.out.println(keyCode);
        if (keyCode == OuyaController.BUTTON_A) {
            finish();
        }

        return handled || super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean handled = OuyaController.onKeyUp(keyCode, event);
        return handled || super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        int odid = event.getDeviceId();
        boolean handled = OuyaController.onGenericMotionEvent(event);

        OuyaController c = OuyaController.getControllerByDeviceId(event.getDeviceId());
        if (c != null) {
            
        	//        c.getAxisValue(OuyaController.AXIS_LS_X)
        	//       c.getAxisValue(OuyaController.AXIS_LS_Y) 
        	
        	//event.getDeviceId() // what controller
           
        }

        return handled || super.onGenericMotionEvent(event);
    }

}
