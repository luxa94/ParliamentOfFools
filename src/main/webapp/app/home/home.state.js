(function (angular) {

    'use strict';

    angular.module('parliament')
        .config(config);

    function config($stateProvider) {
        $stateProvider
            .state('base.home', {
                url: '/home',
                controller: 'homeController',
                controllerAs: 'vm',
                templateUrl: 'app/home/home.html'
            });
    }

}(angular));