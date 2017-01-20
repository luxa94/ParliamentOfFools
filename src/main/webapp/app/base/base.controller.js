(function (angular) {

    'use strict';

    angular.module('parliament')
        .controller('baseController', ['$state', 'authorizationService', baseController]);

    function baseController($state, authorizationService) {
        var vm = this;

        vm.logout = function () {
            authorizationService.logout();
        };

        vm.home = function () {
            $state.go('base.home');
        };
    }

}(angular));
