(function (angular) {

    'use strict';

    angular.module('parliament')
        .controller('logInController', ['$state', 'Alertify', 'authorizationService', logInController]);

    function logInController($state, Alertify, authorizationService) {
        var vm = this;

        vm.user = {};

        vm.goToRegister = function () {
            $state.go('register');
        };

        vm.login = function () {
            authorizationService.login(vm.user, function (successful) {
                if (successful) {
                    Alertify.success('Welcome!');
                } else {
                    Alertify.error('Invalid credentials.');
                }
            });
        };
    }

}(angular));
