(function (angular) {

    'use strict';

    angular.module('parliament')
        .config(config);

    function config($stateProvider) {
        $stateProvider
            .state('base.session', {
                url: '/session',
                controller: 'sessionController',
                controllerAs: 'vm',
                templateUrl: 'app/session/session.html'
            });
    }

}(angular));