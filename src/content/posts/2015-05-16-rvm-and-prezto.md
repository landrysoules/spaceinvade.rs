---
layout: post.swig
title: "rvm with prezto"
tags: [ruby]
date: 2015-05-16
excerpts: "Started hacking in Ruby recently, and immediately looked for the virtualenv equivalent. "
---
Started hacking in Ruby recently, and immediately looked for the virtualenv equivalent.  
[rvm](https://rvm.io/) looks like the standard, so I opted for it, but making it work in my configuration has been surprisingly difficult, while the solution was pretty obvious:  
If you use [prezto](https://github.com/sorin-ionescu/prezto), all you have to do is install rvm:

~~~
$ gpg --keyserver hkp://keys.gnupg.net --recv-keys 409B6B1796C275462A1703113804BB82D39DC0E3
$ \curl -sSL https://get.rvm.io | bash -s stable
~~~

Make sure  you have enabled ruby module in ~/.zpreztorc:

~~~
zstyle ':prezto:load' pmodule \
  'directory' \
  'environment' \
  'terminal' \
  'editor' \
  'history' \
  'directory' \
  'spectrum' \
  'utility' \
  'git'  \
  'completion' \
  'ssh' \
  'node' \
  'ruby' \
  'rails' \
  'syntax-highlighting' \
  'prompt'
~~~

This way, prezto will handle rvm settings for you, and you don't have to mess with your .zshrc.
