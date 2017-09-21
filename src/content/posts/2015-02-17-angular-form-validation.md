---
layout: post.swig
title: Angular 1.3.x form validation with bootstrap
tags: [angular]
date: 2015-02-17
---

This post will give you hints on angular 1.3+ form validation.  
Angular team has done a great job and brought up some cool improvements in the form handling area. Using bootstrap, managing forms has never been so easy.  
Let's take the following form as an example.

<iframe width="100%" height="400px" src="https://embed.plnkr.co/RCbd4dlEOn6yJLr7JLtB" ></iframe>
## The javascript part
As you can see, it is pretty light !

``` javascript
var app = angular.module('main', ['ngMessages', 'toaster']).
controller('FormCtrl', function ($scope, toaster) {
    $scope.person = {};
    $scope.ok = function (valid) {
        if (valid) {
            toaster.pop('success', "Congratulations!", "Your form is successfully submitted !");
        }
    };
});
```

**Make sure to inject ngMessages module!** It was introduced in angular 1.3 and eases validation handling.
The key here is in the "valid" argument. Indeed Angular creates a form object and put it in the scope. This objects receives properties like $valid, $invalid, $submitted that you can use in the template as well as in the controller in order to check the state of your form.  
## The HTML part

I will detail the various parts of the template.

``` markup
<form name="testForm" ng-submit="ok(testForm.$valid)" novalidate>
```

* The name you give to the form will also be used by angular in the scope.  
* We pass the value of $valid to our ok function.  
* The novalidate attribute is required in order to avoid conflicts with angular validation system

``` markup
<input type="text" class="form-control" name="lastName" required ng-model="person.lastName" ng-minlength="3" ng-maxlength="10" />
```

* Don't forget to use name attribute for every input !
* Here we use html5 required attribute, but we could have used ng-required directive in case an input is required only under certain conditions. Angular has several built-in validation directives (you can find the exhaustive list [here](https://code.angularjs.org/1.3.10/docs/api/ng/directive/input), and you can add your own (it will be the subject of a future post).

``` html
<div ng-if="testForm.$submitted" ng-messages="testForm.lastName.$error">
```

* The ng-if directive ensures error messages for this input will not be displayed until form submission, which makes a far better user experience !
* angular will store input's error messages in the testForm object. Hence the need to give a name to your form as well as to every input

``` html
<div ng-message="required" class="text-danger">Required!</div>
<div ng-message="minlength" class="text-danger">This field must be at least 3 chars long!</div>
<div ng-message="maxlength" class="text-danger">This field must be at most 10 chars long!</div>
```

* We repeat ng-message directive for every validation directive declared with our inputs. Here we want last name to be mandatory, and be at least 3 chars long and at most 10 chars long.

``` css
.ng-submitted .ng-invalid{
    border-color: #a94442;
    border-width: 2px;
    box-shadow:none;
}
```

* We added a style in order to display errors
* .ng-submitted and .ng-invalid are styles appended by Angular during validation proces
