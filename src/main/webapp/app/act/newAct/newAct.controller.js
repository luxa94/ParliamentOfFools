(function (angular) {

    'use strict';

    angular.module('parliament')
        .controller('newActController', ['$rootScope', '$state', 'Alertify', 'actService', newActController]);

    function newActController($rootScope, $state, Alertify, actService) {
        var vm = this;

        vm.activeSession = $rootScope.activeSession;

        vm.submitAct = function () {
            console.log(vm.act);
            actService.create(vm.act)
                .then(function () {
                    Alertify.success('Act created');
                    $state.go('base.home');
                })
                .catch(function () {
                    Alertify.error('Error creating act.');
                });
        }

    }

}(angular));