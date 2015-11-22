---
layout: post.html
title: Test your Angular app with json server
tags: [Angular]
---

Description de jsonserver, puis problème (authentification) et comment le résoudre:
En supprimant le header Authorization (qu'on génère automatiquement avec chaque requête => voir la définition du interceptor dans app.js), on évite que le browser génère préalablement une requete OPTIONS, et le tour est joué.

~~~ { javascript }
'use strict';

angular.module('sdopApp')
        .factory('TrapviewAPI', function ($resource) {
            return $resource('http://localhost:3000/images/:measuringSiteId',
                    {},
                    {
                        get: {
                            method: 'GET',
                            isArray: true,
                            cache: false
                        },
                        getImages: {
                            method: 'GET',
                            isArray: true,
                            transformRequest: function (data, headersGetter) {
                                var headers = headersGetter();
                                delete headers['Authorization']; //jshint ignore:line
                                return data;
                            }
                        }
                    });
        });
~~~


