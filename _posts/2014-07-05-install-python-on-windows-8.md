---
layout: post.html
title: Install Python 2.7 on Windows 8
tags: [Python]
---

![Python logo](https://ef65e426d0abf9418992e271986035a1945d7865.googledrive.com/host/0Byy3K2j5Zp_TeXByVnlqTS10UUU/Python_logo-large.png)
After wasting hours trying to make pip install packages depending on C compiler, I finally found a solution !  
While installing Python and Pip is not a big deal, making Pip work is another story: I spent hours installing [Mynt](http://mynt.uhnomoli.com), with no success, banging my head on various error messages, among others, the infamous ```Unable to find vcvarsall.bat```. Several posts suggest to install Microsoft Visual Studio 2008, and although it solved the vcvarsall problem, it brought other errors.  
Here is the procedure I followed to make it work:
### Install Python 32 bits ###
Altghough trivial, it's the essential part of the solution ! If you already had Python 64 bits installed, uninstall it, and delete its installation folder.
### Install setup tools ###
The detailed procedure is well explained on [pypi's site] (https://pypi.python.org/pypi/setuptools#windows-8-powershell). Don't hesitate to skip the whole procedure in favour of the [install script](https://bootstrap.pypa.io/ez_setup.py) provided.  
After that, add your Python's install Scipts folder (by default c:/Python27/Scripts) to your Windows path.  
Then type ```easy_install pip``` in the console.
### Install MinGW ###
You can find it [here](http://sourceforge.net/projects/mingw/files/Installer/). Follow the installation wizard, and make sure you install the MinGW Developer Toolkit, mingw32-base and C++ packages. Make sure you add MinGW/bin folder to your path.  
![MingW packages](https://ef65e426d0abf9418992e271986035a1945d7865.googledrive.com/host/0Byy3K2j5Zp_TeXByVnlqTS10UUU/mingw.png)  
### Force Pip to use MinGW as C compiler ###
By default, Pip will try to use the MSVS 2008 C compiler, so you have to force it to use MinGW's compiler instead. It's done by creating a file called distutils.cfg in PYTHON_INSTALL_FOLDER/Lib/distutils repository, and editing it as follow:  

~~~ nginx
[build]
compiler=mingw32 
~~~
Et voilà! Now you should be able to install any package with Pip.
