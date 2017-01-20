(function (angular) {

    'use strict';

    angular.module('parliament')
        .config(config);

    function config($stateProvider) {
        $stateProvider
            .state('register', {
                url: '/register',
                controller: 'registerController',
                controllerAs: 'vm',
                templateUrl: 'app/authorization/register/register.html'
            })
    }

}(angular));