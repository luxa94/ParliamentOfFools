(function (angular) {
    'use strict';

    angular.module('parliament')
        .config(config);

    function config($stateProvider) {
        $stateProvider
            .state('base.amendmentVoting', {
                url: '/amendmentVoting/:id',
                controller: 'amendmentVotingController',
                controllerAs: 'vm',
                templateUrl: 'app/amendment/amendmentVoting/amendmentVoting.html'
            });
    }

}(angular));
