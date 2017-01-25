(function (angular) {
    'use strict';

    angular.module('parliament')
        .config(config);

    function config($stateProvider) {
        $stateProvider
            .state('base.actVoting', {
                url: '/actVoting/:id',
                controller: 'actVotingController',
                controllerAs: 'vm',
                templateUrl: 'app/act/actVoting/actVoting.html'
            });
    }

}(angular));