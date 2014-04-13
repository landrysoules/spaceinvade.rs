---
layout: post.html
title: Use a Git hook to update automatically your Mynt blog
tags: [Mynt]
---

I just discovered Mynt, and so far it's a wonderful experience. But I lacked a way of easily publish my posts. It took me a week-end, but I built a very satisfying and easy to use solution to publish your posts directly from Git. I found great inspiration from this [article](http://blog.jameskyle.org/2012/12/deploying-pelican-blog-with-bitbucket-commit-hooks) from James Kyle.
This method involves using a git post hook from [Bitbucket](http://www.bitbucket.org), but you should be able to easily adapt it for github or any other git host.
You will also need Gunicorn, as well as Supervisor, and the web server I use is Nginx.
Of course, you will set up a virtualenv, and install mynt, gunicorn and supervisor.
A small WSGI script will listen to a chosen url, and launch Mynt serve everytime git hook calls it.
Let's assume you run the script with your user account, lovely named roger. 

####Server script
Let's call it update_mynt.py

~~~ { python }
#!/home/roger/Dev/Virtualenvs/mynt/bin/python

from git import *
import subprocess
import os
import logging

def application(env, start_response):
        repo = Repo("/path/to/your/local/git/repo")
        origin = repo.remotes.origin
        origin.pull()
        print('Origin remote branch pulled')
        subprocess.call(["mynt", "gen",  "-f", "/path/to/your/local/git/repo", \
	  "/path/to/your/mynt/generated/files"], env=os.environ.copy())
        print('Site generated')
        start_response('200 OK', [('Content-Type', 'text/html')])
        return ['Changes processed successfully !']
~~~

####Launching gunicorn
Now we create a bash sript which will be in charge of launching gunicorn:

~~~ { bash }
#!/bin/bash
set -e
LOGFILE=/path/to/your/log/file.log
ERRORFILE=/path/to/your/error/file.log
LOGDIR=$(dirname $LOGFILE)
NUM_WORKERS=3
#The below address:port info will be used later to configure Nginx with Gunicorn
ADDRESS=127.0.0.1:9090
# user/group to run as
USER=your_unix_user
GROUP=your_unix_group

cd /path/to/your/blog/root

source /home/roger/Virtualenvs/myntvirtualenv/bin/activate

test -d $LOGDIR || mkdir -p $LOGDIR

exec gunicorn update_mynt  -w $NUM_WORKERS --bind=$ADDRESS \
--log-level=debug \

--log-file=$LOGFILE 2>>$LOGFILE  1>>$ERRORFILE  &
~~~

As this script is meant to always run, you should launch it as a service with supervisor. Here is the supervisor config

~~~ { cfg }
[program:chooseAName]

directory=/path/to/the/dir/your/script/lives/in
command=bash start_gunicorn.sh
user=landry
autostart=true
autorestart=true
stdout_logfile=/var/log/supervisor/blog.log
redirect_stderr=true
~~~

Now we configure nginx to redirect requests to gunicorn:

~~~ { nginx }
    location /urlofyourchoice {
        proxy_pass http://0.0.0.0:9090; #The port you set gunicorn to listen to
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
     }
~~~

Eventually, configure [Bitbucket POST hook](https://confluence.atlassian.com/display/BITBUCKET/POST+hook+management), which is really a two-click process, and you're done !

