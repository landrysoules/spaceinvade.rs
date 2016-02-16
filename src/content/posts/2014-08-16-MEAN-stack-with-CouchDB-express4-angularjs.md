---
layout: post.swig
title: "MEAN stack application with CouchDB - Express 4 - Angular.js"
tags: [MEAN, express, angular]
date: 2014-08-16
---

### Note: you can find the source code for this tutorial on [github](https://github.com/landrysoules/trag)
What a powerful collection of trendy acronyms, ain't it ? I hope this article will help you have a better vision of these techniques and build your first app.
This post will detail how to build a simple RESTful application, with complete authentication mechanism.
I will show you how you can bootstrap such an app very fast, with the help of the amazing [yeoman](http://yeoman.io).  
RESTful applications have many advantages over other techniques. My personal favourites are:

*  build your services once, call them from anywhere (browser, native phone app, whatever)
*  test your server application easily with a REST client (I'm personally very fond of [Postman](http://www.getpostman.com/)).

## JSON Web Tokens

Once you're convinced that a REST API on the server side is the best way to build a modern app, what is the best way to implement it ? This tutorial will use node.js, but of course you can replace it with python, ruby or even java.  
However, your application, as great as it is, is useless since you are not able to authenticate its users. In this field too, several solutions are available, but the most appealing to me is JSON web tokens (JWT).  
In short:

1.  The client sends an authentication request, with credentials.
1.  Based on the credentials received, the server generates an encrypted token containing user credentials, and sends it back to the client.
1.  With every request to a protected API, the client sends the received token in the "Authorization" header
1.  The server decodes the token and instantly gets user credentials  

You can get much more detailed explanation of JWT [here](https://auth0.com/blog/2014/01/07/angularjs-authentication-with-cookies-vs-token).

## Required software
In order to follow this tutorial, you will have to install [node.js](http://nodejs.org/download) and [couchDB](http://couchdb.apache.org/). Nothing too complicated here, just refer to their official documentation.
To be able to use SASS (which is optional), you must have ruby installed on your computer, as well as sass: ```gem install sass```
Next, node.js will allow us to install all we need to build our apps (server and client).  
**If using linux, you may have to precede every "npm install" instruction with sudo**
First we install Yeoman, a brilliant tool that generates skeleton projects, and provides code generation from command-line (scaffolding) for many languages and frameworks. Thus with the help of [the angular full-stack generator](https://www.npmjs.org/package/generator-angular-fullstack), Yeoman will generate an express server application as well as an angulars.js application !  
```npm install -g yeoman``` then ```npm install -g generator-angular-fullstack```  
Now create a new folder for your application, and cd into it. Let's call our app trag.
```mkdir trag``` ```cd trag```  
Now the fun begin: let's generate our app !  
```yo angular-fullstack trag```
Keep default answer for every question asked by yeoman (if you haven't installed ruby/SASS, replace SASS with other option), but mongodb use (indeed we will use couchDB, so we don't need mongodb scaffolding).
In case generation fails, type ```npm cache clean```, then ```bower cache clean``` and again  ```yo angular-fullstack trag```.  
And now type ```grunt serve``` and let the magic happen ! Grunt will start node's http server, and open your default browser ! You should see something similar to this:  
![trag homepage](https://ef65e426d0abf9418992e271986035a1945d7865.googledrive.com/host/0Byy3K2j5Zp_TeXByVnlqTS10UUU/yeoman.png)
Pretty cool, isn't it ? And even cooler when you know that this page isn't static at all: the features part is filled with a REST call to /api/things ! You can run [Fiddler](http://www.telerik.com/fiddler) to have a look at what's happening under the hood.  

## Secure your API with JWT
We will know secure our API with JWT, assuming that everything under /api is secure but any route needed to authenticate or see default content. This way, users can freely signup but must be authenticated to see their profile, for example.

First we install express-JWT and jsonwebtoken plugins: ```npm install express-jwt --save```, ```npm install jsonwebtoken --save```. Now configure it in /server/config/express.js:  

~~~ javascript
/**
 * Express configuration
 */

'use strict';

var express = require('express');
var favicon = require('static-favicon');
var morgan = require('morgan');
var compression = require('compression');
var bodyParser = require('body-parser');
var methodOverride = require('method-override');
var cookieParser = require('cookie-parser');
var errorHandler = require('errorhandler');
var path = require('path');
var config = require('./environment');
//declare express-jwt plugin
var expressJwt = require('express-jwt');
//specify the salt passphrase JWT will use for its token encryption
var SECRET = 'mysecret';

module.exports = function (app) {
	var env = app.get('env');

	app.set('views', config.root + '/server/views');
	//Make your passphrase accessible from app object
	app.set('secret', SECRET);
	app.engine('html', require('ejs').renderFile);
	app.set('view engine', 'html');
	app.use(compression());
	app.use(bodyParser.urlencoded({
			extended : false
		}));
	app.use(bodyParser.json());
	app.use(methodOverride());
	app.use(cookieParser());

//specify the path where will reside all your JWT secured apis, and tell it which passphrase encryption to use  
//Thanks to unless, you can also specify exception: routes that will not be restricted
	app.use('/api', expressJwt({
			secret : SECRET
		}).unless({path: ['/api/auth','/api/things','/api/auth/signup']}));

	if ('production' === env) {
		app.use(favicon(path.join(config.root, 'public', 'favicon.ico')));
		app.use(express.static(path.join(config.root, 'public')));
		app.set('appPath', config.root + '/public');
		app.use(morgan('dev'));
	}

	if ('development' === env || 'test' === env) {
		app.use(require('connect-livereload')());
		app.use(express.static(path.join(config.root, '.tmp')));
		app.use(express.static(path.join(config.root, 'client')));
		app.set('appPath', 'client');
		app.use(morgan('dev'));
		app.use(errorHandler()); // Error handler - has to be last
	}
};
~~~
Add a (open) user endpoint: ```yo angular-fullstack:endpoint user```  
Edit /api/users/index.js:  

~~~ javascript
'use strict';

var express = require('express');
var controller = require('./user.controller');

var router = express.Router();

router.get('/profile', controller.profile);

module.exports = router;
~~~
You guessed it, we will add our methods to the controller, but first we need to add couchDB support to our app: ```npm install nano --save```.  
Open [futon](http://localhost:5984/_utils) and create a new database named trag. Modify our (non-secure) users controller:  
Here, we create a method "profile" that returns profile infos, only if the request contains a valid token.
/api/users/user.controlller.js

~~~ javascript
'use strict';

var jwt = require('jsonwebtoken');
var nano = require('nano')('http://localhost:5984/trag');
var _ = require('lodash');

exports.profile = function (req, res) {

	var token = null;
	var parts = req.headers.authorization.split(' ');
	if (parts.length == 2) {
		var scheme = parts[0];
		var credentials = parts[1];

		if (/^Bearer$/i.test(scheme)) {
			token = credentials;
			var decoded = jwt.decode(token, req.app.secret);
			console.log(req.headers.authorization);
			var username = decoded.username;
			console.log("username to retrieve: " + username);
			nano.get(username, null, function (err, body) {
				if (err) {
					res.send(400, err);
					return;
				} else {
					console.log(body);
					res.json({
						profile : body
					});
				}
			});
		}
	} else {
		return next(new UnauthorizedError('credentials_bad_format', {
				message : 'Format is Authorization: Bearer [token]'
			}));
	}

};

~~~

Now we will create an auth endpoint:  ```yo angular-fullstack:endpoint auth```  

api/auth/index.js

~~~ javascript
'use strict';

var express = require('express');
var controller = require('./auth.controller');

var router = express.Router();

router.post('/', controller.authenticate);
router.post('/signup', controller.signup);

module.exports = router;
~~~


api/auth/auth.controller.js  

~~~ javascript
'use strict';
//Declare URL of your database
var nano = require('nano')('http://localhost:5984/trag');
var _ = require('lodash');
//We will use jsonwebtoken to actually generate the token
var jwt = require('jsonwebtoken');
var _ = require('lodash');

exports.authenticate = function(req, res) {
    //Client will call this method to query a token
    var username = req.body.username;
    var password = req.body.password;
    nano.get(username, null, function(err, body) {
        if (err) {
            res.send(401, 'Wrong user or password');
            return;
        } else {
            console.log(body);
			//properties contained in the token, of course you can add first name, last name and so on
            var profile = {
                username: username
            };
            // We are encoding the profile inside the token
            var token = jwt.sign(profile, req.app.get('secret'), {
                expiresInMinutes: 60 * 5
            });
            res.json({
                token: token
            });
        }
    });
};

exports.signup = function(req, res) {
//when signup method is called, we insert a new user in couchDB, username being the id of the record
    nano.insert({
        'password': req.body.password
    }, req.body.username, function(err, body, header) {
        if (err) {
			//if for example username already exists in database, an error will be thrown
            console.log('user insertion error', err.message);
            res.send(400, err.message);
        }else{
        console.log('you have inserted a new user: ' + req.body.username);
        console.log(req.body);
		           var profile = {
                username: req.body.username
            };
            // We are encoding the profile inside the token
            var token = jwt.sign(profile, req.app.get('secret'), {
                expiresInMinutes: 60 * 5
            });
            res.json({
                token: token
            });

		}
    });
};
~~~

Now we can start testing our api with postman:  
![postman signup test](https://ef65e426d0abf9418992e271986035a1945d7865.googledrive.com/host/0Byy3K2j5Zp_TeXByVnlqTS10UUU/signup-Postman.png)  

Copy the token received, we will use it next.  
In order to test our token, we will now call our user service:
![postman signup-2 test](https://ef65e426d0abf9418992e271986035a1945d7865.googledrive.com/host/0Byy3K2j5Zp_TeXByVnlqTS10UUU/postman3.png)  

Don't forget to add an authentication header, and paste the token the server just generated.  

## Angular client
Now that we have checked with postman that our API works as expected, it's time to implement our angular client.
Form validation the angular way is brilliantly explained in this article: [http://tutorials.jenkov.com/angularjs/forms.html](http://tutorials.jenkov.com/angularjs/forms.html).
Let's first edit client/components/navbar/navbar.html, in order to add sign-in/sign-up form (we will add it in the navigation bar):

``` html
<html>
  <head>
    <meta name="generator"
    content="HTML Tidy for HTML5 (experimental) for Windows https://github.com/w3c/tidy-html5/tree/c63cc39" />
    <title></title>
  </head>
  <body>
    <div class="navbar navbar-default navbar-static-top" ng-controller="NavbarCtrl">
      <div class="container">
        <div class="navbar-header">
			<button class="navbar-toggle" type="button" ng-click="isCollapsed = !isCollapsed">
			  <span class="sr-only">Toggle navigation</span>
			</button>
			<a href="/" class="navbar-brand">trag</a></div>
			<div collapse="isCollapsed" class="navbar-collapse collapse" id="navbar-main">
			  <ul class="nav navbar-nav">
				<li ng-repeat="item in menu" ng-class="{active: isActive(item.link)}">
				  <a ng-href="{{item.link}}">{{item.title}}</a>
				</li>
			  </ul>
			  <form class="navbar-form navbar-right" role="search" name="user_form" novalidate="">
			  <div>
				  <div class="form-group" ng-class="{'has-error has-feedback': user_form.username.$invalid && user_form.username.$dirty}" >
					<label for="email_field">email</label>
					  <input id="email_field" type="email" class="form-control" name="username" ng-model="user.username" required="" />
					 <span class="glyphicon glyphicon-remove form-control-feedback"></span>
						  </div>
				  <div class="form-group" ng-class="{'has-error has-feedback': user_form.password.$invalid && user_form.password.$dirty}">
				  <label for="password_field">password</label>
					<input id="password_field" type="password" class="form-control" name="password" ng-model="user.password" required="" />
					<span class="glyphicon glyphicon-remove form-control-feedback"></span>
				  </div>
				  <button ng-click="signin(user)" class="btn btn-default">Sign In</button>
				  <button ng-click="signup(user)" class="btn btn-default">Sign Up</button>
			<div class="alert alert-danger" ng-show="(user_form.username.$invalid && user_form.username.$dirty)||(user_form.password.$invalid && user_form.password.$dirty)">
                <ul>
                    <li ng-show="user_form.username.$error.required">email address is required</li>
					<li ng-show="user_form.username.$error.email">email address is not valid</li>
					<li ng-show="user_form.password.$error.required">password is required</li>
                </ul>
            </div>
			<div class="alert" ng-class="{'alert-danger': message.status=='ko', 'alert-success': message.status=='ok'}" ng-show="(message)">
               {{message.text}}
            </div>
				  </div>

				</form>



			</div>
      </div>

    </div>

  </body>
</html>

```
We will need to enlarge body width, to allow our form to fit in the navigation bar. It is done very easily by editing app.scss:
change max-width to 790px and run ```grunt sass```.

For authentication, we will create an auth service which will say if user is authenticated or not, and a token interceptor which will modify header on every request.  
As usual yeoman can generate skeleton code with ```yo angular:factory authenticationService``` and edit the file authenticationService.service.js:  

```javascript
'use strict';

angular.module('tragApp')
.factory('authenticationService', function() {
    var auth = {
        isLogged: false
    }

    return auth;
});
```  
Let's now create a token interceptor:

```javascript
'use strict';

angular.module('tragApp')
.factory('tokenInterceptor', function ($q, $window, authenticationService) {
	return {
		request : function (config) {
			config.headers = config.headers || {};
			if ($window.sessionStorage.token) {
				config.headers.Authorization = 'Bearer ' + $window.sessionStorage.token;
			}
			return config;
		},

		response : function (response) {
			return response || $q.when(response);
		}
	};
});
```
We have to add our newly created tokenInterceptor to the list of interceptors:  

app.js:

```javascript
'use strict';

angular.module('tragApp', [
  'ngCookies',
  'ngResource',
  'ngSanitize',
  'ngRoute',
  'ui.bootstrap'
])
  .config(function ($routeProvider, $locationProvider) {
    $routeProvider
      .otherwise({
        redirectTo: '/'
      });

    $locationProvider.html5Mode(true);
  })

.config(function ($httpProvider) {
    $httpProvider.interceptors.push('tokenInterceptor');
});
```
Now we can use our signin/signup form.  

This tutorial was quite long, but I hope it wasn't too hard to follow and understand.
