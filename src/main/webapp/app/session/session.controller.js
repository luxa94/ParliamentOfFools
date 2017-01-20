(function (angular) {

    'use strict';

    angular.module('parliament')
        .controller('sessionController', ['$rootScope', '$state', '$mdDialog', sessionController]);

    function sessionController($rootScope, $state, $mdDialog) {
        var vm = this;

        vm.user = $rootScope.user;

    }

}(angular));
