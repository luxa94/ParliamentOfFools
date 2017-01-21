(function (angular) {
    'use strict';

    angular.module('parliament')
        .config(config);

    function config($stateProvider) {
        $stateProvider
            .state('base.act', {
                url: '/act/:id',
                controller: 'actController',
                controllerAs: 'vm',
                templateUrl: 'app/act/act.html'
            });
    }

}(angular));