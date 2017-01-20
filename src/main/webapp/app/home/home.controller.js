(function (angular) {

    'use strict';

    angular.module('parliament')
        .controller('homeController', ['$rootScope', '$state', '$mdDialog', homeController]);

    function homeController($rootScope, $state, $mdDialog) {
        var vm = this;

        vm.user = $rootScope.user;

    }

}(angular));