---
layout: post.html
title: Protractor tests problem with angular
tags: [angular.js]
---

I'm actively learning angular.js, and after having completed the brilliant [codeschools video tutorial](https://www.codeschool.com/courses/shaping-up-with-angular-js), I'm diving into [the official tutorial](https://docs.angularjs.org/tutorial).  
When running my first end-to-end protractor test at the begining of the tutorial, I was quickly stopped by an error: "cannot call method 'forEach' of null". Furstrating, isn't it ? In those cases, stackoverflow is your best friend, and [this solution](http://stackoverflow.com/questions/24391818/protractor-failing-on-windows-with-cannot-call-method-foreach-of-null) worked perfectly:  

edit protractor-conf.js and change the property "chrome-only" to false.
