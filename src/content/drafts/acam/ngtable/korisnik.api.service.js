(function() {
  'use strict';

  angular.module('acam')
    .factory('KorisnikAPIService', KorisnikAPIService)
    // @ngInject
  function KorisnikAPIService($resource, API_BASE) {
    return $resource(API_BASE + '/korisnik/:id', {}, {
      getAll: {
        isArray: true
      },
      search:{
        params: {
          id: 'search'
        }
      }
    });
  }
})()
