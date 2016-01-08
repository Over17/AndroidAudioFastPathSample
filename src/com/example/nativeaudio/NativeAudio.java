/*
 * Copyright (C) 2010 The Android Open Source Project
 * Modifications Copyright (C) 2016 Yury Habets
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.nativeaudio;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.media.AudioManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class NativeAudio extends Activity {
    static final String TAG = "NativeAudio";

    static AssetManager assetManager;
    static boolean isPlayingAsset = false;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);

        assetManager = getAssets();

		// Get the device native params
		AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		int sampleRate = Integer.parseInt(am.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE));
		int bufferSize = Integer.parseInt(am.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER));
		Log.d(TAG, "Device sample rate = " + sampleRate + ", bufferSize = " + bufferSize);

        // initialize native audio system
        createEngine();
        createBufferQueueAudioPlayer(sampleRate, bufferSize);

        ((Button) findViewById(R.id.sawtooth)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                // ignore the return value
                selectClip(3, 1);
            }
        });
		
        ((Button) findViewById(R.id.device_params)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
				AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				String sampleRate = am.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE);
				String bufferSize = am.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER);

                Toast.makeText(NativeAudio.this, "Device sample rate = " + sampleRate + ", bufferSize = " + bufferSize,
                        Toast.LENGTH_SHORT).show();
            }
        });

        ((Button) findViewById(R.id.embedded_soundtrack)).setOnClickListener(new OnClickListener() {
            boolean created = false;
            public void onClick(View view) {
                if (!created) {
                    created = createAssetAudioPlayer(assetManager, "background.mp3");
                }
                if (created) {
                    isPlayingAsset = !isPlayingAsset;
                    setPlayingAssetAudioPlayer(isPlayingAsset);
                }
            }
        });
    }

    /** Called when the activity is about to be destroyed. */
    @Override
    protected void onPause()
    {
        // turn off all audio
        selectClip(0, 0);
        super.onPause();
    }

    /** Called when the activity is about to be destroyed. */
    @Override
    protected void onDestroy()
    {
        shutdown();
        super.onDestroy();
    }

    /** Native methods, implemented in jni folder */
    public static native void createEngine();
    public static native void createBufferQueueAudioPlayer(int sampleRate, int bufferSize);
    public static native boolean createAssetAudioPlayer(AssetManager assetManager, String filename);
    // true == PLAYING, false == PAUSED
    public static native void setPlayingAssetAudioPlayer(boolean isPlaying);
    public static native boolean selectClip(int which, int count);
    public static native void shutdown();

    /** Load jni .so on initialization */
    static {
        System.loadLibrary("native-audio-jni");
    }

}
