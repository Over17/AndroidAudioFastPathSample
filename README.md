AndroidAudioFastPathSample
==========================

This project is a modified native-audio sample from Android NDK r10e, written by Glenn Kasten and copyrighted under The Android Open Source Project.

The initial sample nicely shows the possibilities of using OpenSL for audio input and output in Android, though it is quite old and does not utilize the audio Fast Path introduced in Jelly Bean.

I adopted the sample so that it utilizes the low-latency audio path:
- removed effects (reverb)
- set the sampling frequency to the native value reported by the device
- the buffer size should also match the one reported by the device, but it does not really seem to affect whether the audio subsystem selects fast mixer or not, so this is hardcoded. In a real-world app, you will need to have at least 2 buffers which match the device native value, and probably a thread that fills the buffers with the necessary audio data. Here we just submit the whole audio buffer to the buffer queue, for the matter of simplicity.
- left only the generated "sabretooth" clip and a sound clip in the assets
- the UI now contains only three buttons - the generated clip, the assets clip, and display the device native values - sampling frequency and buffer size

To rebuild the code
-------------------
1.	Build the native library by running ndk-build
2.	Build the APK in Eclipse, Android Studio or tool of your choice
3.	Enjoy
	
License
-------
Copyright (C) 2010 The Android Open Source Project

Modifications Copyright (C) 2016 Yury Habets

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

See also
--------
For more details about the low-latency audio on Android introduced in Jelly Bean and fast mixer please refer to the official docs:

https://source.android.com/devices/audio/latency_design.html

Great presentation of the features and requirements:

Google I/O 2013 - High Performance Audio: https://www.youtube.com/watch?v=d3kfEeMZ65c
