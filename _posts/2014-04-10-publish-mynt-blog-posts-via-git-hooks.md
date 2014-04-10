---
layout: post.html
title: Use a Git hook to update automatically your Mynt blog
tags: [Mynt]
---

I just discovered Mynt, and so far it's a wonderful experience. But I lacked a way of easily publish my posts. It took me awwek-end, but I built a very satisfying and easy to use solution to publish your posts directly from Git. I found great inspiration from this [article](http://blog.jameskyle.org/2012/12/deploying-pelican-blog-with-bitbucket-commit-hooks) from James Kyle.

:::bash
[program:spaceinvaders]

directory=/home/landry/Dev/Projects/blog
command=bash start_gunicorn.sh
user=landry
autostart=true
autorestart=true
stdout_logfile=/var/log/supervisor/blog.log
redirect_stderr=true


