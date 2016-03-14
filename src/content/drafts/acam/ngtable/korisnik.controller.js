(function() {
  'use strict';

  angular
    .module('acam')
    .controller('KorisnikController', KorisnikController);
  /** @ngInject */
  function KorisnikController($log, $stateParams, $translate, KorisnikAPIService, toastr, NgTableParams) {
    var vm = this;
    vm.korisnik = {};
    vm.errorsFromServer = null;

    if ($stateParams.id) {
      KorisnikAPIService.get({
          id: $stateParams.id
        })
        .$promise
        .then(function(success) {
          vm.korisnik = success;
        })
        .catch(function(err) {
          $log.error(err);
        })
    }

    vm.tableParams = new NgTableParams({}, {
      getData: function(params) {
        $log.warn('params: ', params);
        $log.warn('filter: ', params.filter());
        // $log.warn('params URL: ', params.url());
        return KorisnikAPIService.search(params.filter()).$promise.then(function(success) {
          return success.results;
        });
      }
    });

    KorisnikAPIService
      .getAll()
      .$promise
      .then(function(success) {
        vm.korisniks = success;
      })
      .catch(function(err) {
        $log.error(err);
      })

    vm.save = function(isValid) {
      vm.errorsFromServer = null;
      if (isValid) {
        $log.warn('korisnik', vm.korisnik);
        KorisnikAPIService
          .save(vm.korisnik)
          .$promise
          .then(function(success) {
            toastr.success('record saved');
          })
          .catch(function(err) {
            $log.error(err);
            vm.errorsFromServer = $translate.instant(err.data.message);
          })
      }
    }

  }

})();
