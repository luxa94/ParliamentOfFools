(function (angular) {

    'use strict';

    angular.module('parliament')
        .controller('registerController', ['$state', 'Alertify', 'authorizationService', registerController]);

    function registerController($state, Alertify, authorizationService) {
        var vm = this;

        vm.user = {};

        vm.goToLogin = function () {
            $state.go('login');
        };

        vm.register = function () {
            authorizationService.register(vm.user, function (successful) {
                if (successful) {
                    Alertify.success('Welcome!');
                } else {
                    Alertify.error('Registration failed.');
                }
            });
        };
    }

}(angular));
