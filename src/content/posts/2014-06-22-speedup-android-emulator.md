---
layout: post.swig
title: "Speed up Android emulator in Windows"
tags: [Android]
date: 2014-06-22
---

I've been complaining for years about the incredibly slow startup and bad responsiveness of the Android emulator.  
I eventually started using [genymotion](http://www.genymotion.com/), after having lost any hope to see Android emulator's performance increase. And two weeks ago, I accidentally found the secret to make the emulator much, much faster.  
Here is this ancient secret revealed:  

Open Android SDK Manager app, and install Intel x86 Emulator accelerator:

![SDK manager](https://ef65e426d0abf9418992e271986035a1945d7865.googledrive.com/host/0Byy3K2j5Zp_TeXByVnlqTS10UUU/sdk_manager.png)

Actually, this step downloads the emulator accelerator, **but it doesn't install it !**  
In order to install the accelerator, go to your Android sdk installation folder, and then launch extras\intel\Hardware_Accelerated_Execution_Manager\intelhaxm.exe:

![SDK manager](https://ef65e426d0abf9418992e271986035a1945d7865.googledrive.com/host/0Byy3K2j5Zp_TeXByVnlqTS10UUU/haxm.png)  


Next time you start Android emulator, you will feel a huge difference !
