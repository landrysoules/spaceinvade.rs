---
layout: post.swig
title: Access your Bitbucket Git repository from your Windows PC
tags: [Git]
---

In order to work with your Git repository on your Windows PC, you will have to follow these steps:

### 1/ Install putty
Be careful to download the complete **Windows installer**, and not only putty.exe.  
You can find it [here](http://www.chiark.greenend.org.uk/~sgtatham/putty/download.html).

### 2/ Generate SSH keys
Run puttygen.exe:

![Putty ssh key generator 1](https://ef65e426d0abf9418992e271986035a1945d7865.googledrive.com/host/0Byy3K2j5Zp_TeXByVnlqTS10UUU/ssh_generator_1.png)

Make sure that SSH-2 RSA is selected, click [generate], and as specified, move your mouse over the blank area.  
When the key is generated, you will be prompted to enter a passphrase. Then click [save public key] and [save private key], to save the public and private keys in the folder of your choice.  
Take note that you will always be able to generate a public key from your private key.

### 3/ Run pageant
Run pageant.exe (it has been installed along with putty). It will run in your task bar. Right click on its icon and select "Add key".

![Run pageant](https://ef65e426d0abf9418992e271986035a1945d7865.googledrive.com/host/0Byy3K2j5Zp_TeXByVnlqTS10UUU/pageant_1.png)

Select the private key you saved in the previous step, and type your pass phrase.

### 4/ Add your public key to your Bitbucket account
Log in to your Bitbucket account.  
Click on "manage account", then on "SSH keys", and "add key".

![Add SSH key](https://ef65e426d0abf9418992e271986035a1945d7865.googledrive.com/host/0Byy3K2j5Zp_TeXByVnlqTS10UUU/account.png)

Run puttygen again, and load your private key:

![Copy SSH key](https://ef65e426d0abf9418992e271986035a1945d7865.googledrive.com/host/0Byy3K2j5Zp_TeXByVnlqTS10UUU/ssh_copy.png)

Select everything in the "public key for pasting into Open SSH authorized_key file" box, and right click, select "copy".

### 4/ Run plink
This crucial step is often forgot, which causes git connection errors like this one:
```
Connection abandoned.
fatal: Could not read from remote repository.

Please make sure you have the correct access rights
and the repository exists.
```

Open a PowerShell console and type `path\to\pageant.exe path/to/your/bitbucket/repository`  
For example:
`"C:\Program Files (x86)\PuTTY\plink.exe" git@bitbucket.org:john_doe/myproject.git`

Now you should be able to work with your bitbucket repository !
