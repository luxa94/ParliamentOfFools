(function (angular) {

    'use strict';

    angular.module('parliament')
        .config(config);

    function config($stateProvider) {
        $stateProvider
            .state('base', {
                url: '',
                abstract: true,
                controller: 'baseController',
                controllerAs: 'vm',
                templateUrl: 'app/base/base.html'
            })
    }

}(angular));