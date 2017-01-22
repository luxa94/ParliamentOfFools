(function (angular) {

    'use strict';

    angular.module('parliament')
        .controller('sessionController', ['$rootScope', 'Alertify', 'sessionService', '$state', sessionController]);

    function sessionController($rootScope, Alertify, sessionService, $state) {
        var vm = this;

        vm.activeSession = $rootScope.activeSession;
        vm.user = $rootScope.user;
        vm.minDate = new Date();

        vm.cancel = function () {
            vm.newSession = {};
        };

        vm.newAct = function () {
            $state.go('base.newAct');
        };

        vm.save = function () {
            sessionService.create(vm.newSession)
                .then(function (response) {
                    vm.activeSession = $rootScope.activeSession = response.data;
                    Alertify.success('Session created.');
                })
                .catch(function () {
                    Alertify.error('Unable to create session.');
                })
        };

        vm.activate = function () {
            sessionService.activate()
                .then(function (response) {
                    vm.activeSession = $rootScope.activeSession = response.data;
                    Alertify.success('Session is now active.');
                })
                .catch(function () {
                    Alertify.error('Something went wrong.');
                });
        }
    }

}(angular));
