(function (angular) {

    'use strict';

    angular.module('parliament')
        .config(config);

    function config($stateProvider) {
        $stateProvider
            .state('login', {
                url: '/login',
                controller: 'logInController',
                controllerAs: 'vm',
                templateUrl: 'app/authorization/login/login.html'
            })
    }

}(angular));