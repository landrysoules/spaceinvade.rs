---
layout: post.swig
title: "Make your developer's life easier on Windows 8"
tags: [Windows8]
---

![Windows 8](https://ef65e426d0abf9418992e271986035a1945d7865.googledrive.com/host/0Byy3K2j5Zp_TeXByVnlqTS10UUU/Windows_8.jpg)  

Even though I've been a hardcore Linux user since a dozen years, I must admit that I enjoy Windows 8 !  
Here are some tips to make it even better !
## Install a real console

Windows console is a pain in the ass to use, and Microsoft engineers didn't feel the need to change it since Windows 3.1, back in the mid 1990's !  
I can understand that Windows is targeted to non-technical users, but still why didn't they make a usable console ? By chance there's a brilliant open source alternative: [ConEmu](http://sourceforge.net/projects/conemu).  
This is an incredible tool, allowing you to use tabs, to open cmd, powershell or putty sessions, and far, far more features !
![ConEMu](https://ef65e426d0abf9418992e271986035a1945d7865.googledrive.com/host/0Byy3K2j5Zp_TeXByVnlqTS10UUU/conemu.png)  
### MobaXterm

For accessing and working on my VPS, I've definitely left putty in favor of the excellent [MobaXterm](http://mobaxterm.mobatek.net)
## Install a decent system variables editor

I don't know for you but I've always been swearing while editing Windows path. Yes the interface comes directly from 1995 too... Again, a wonderful tool is gonna make your life much easier: [Rapid Environment Editor](http://www.rapidee.com/en/about)

![Rapid Environment Editor](http://www.rapidee.com/images/rapidee.png.pagespeed.ce.Jxihq-jte3.png)  

## Code editors
I surely wouldn't use something else than eclipse when coding anything Java related, but when I need to launch an editor to quickly edit a file, [notepad++](http://notepad-plus-plus.org) is my best choice !  
And if you can't live without [vim](http://www.vim.org), you'll find it working very well on windows.

## Screen captures
![pop](http://getgreenshot.org/wp-content/themes/greenshot/images/headers/greenshot_logo.gif)
When you are confronted to the painful task of writing some documentation, you need a solid screen capture tool. Don't look any further than [Greenshot](http://getgreenshot.org) !

## Copy/paste
[Ditto](http://ditto-cp.sourceforge.net) is a clipboard manager which basically keeps an history of your clipboard. Simple but so usefull !  

## Always on top apps
A thing that I missed from Linux was the ability to make a window stay always on top of others. [WindowOnTop](http://www.addictivetips.com/windows-tips/quickly-enable-always-on-top-behaviour-for-any-app-or-window) fills this gap.

## Startup programs
The functionality to make apps launch automatically during Windows startup is somehow hidden in Windows 8. The simpler method I found is:  

1.  Make a shortcut of the app you want to run at startup
1.  Press [winkey] + [r]
1.  Type ```shell:startup``` this will open a file explorer window at the correct place
1.  Paste the shortcut in the open folder

That's all for now. If you have some tips, please share in the comments !
