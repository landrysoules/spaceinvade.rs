---
layout: post.html
title: Fix missing radiotray icon in KDE
tags: [Ubuntu]
---

Just installed radiotray in my brand new Kubuntu 14.10, and discover that its icon is missing from the taskbar.
By chance I found the solution [here](http://forums.netrunner-os.com/showthread.php?tid=478&page=2).
All you have to do is to modify ~/.local/share/radiotray as follows:

~~~ { xml }
<!-- <option name="gui_engine" value="appindicator"/> old value -->
<option name="gui_engine" value="systray"/> <!-- Correct value -->
~~~

