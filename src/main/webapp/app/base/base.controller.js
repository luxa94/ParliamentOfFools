(function (angular) {

    'use strict';

    angular.module('parliament')
        .controller('baseController', ['$rootScope', '$state', 'authorizationService', baseController]);

    function baseController($rootScope, $state, authorizationService) {
        var vm = this;

        vm.activeSession = $rootScope.activeSession;

        vm.logout = function () {
            authorizationService.logout();
        };

        vm.home = function () {
            $state.go('base.home');
        };

        vm.session = function () {
            $state.go('base.session');
        };
    }

}(angular));
