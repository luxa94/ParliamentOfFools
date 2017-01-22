(function (angular) {

    'use strict';

    angular.module('parliament')
        .config(config);

    function config($stateProvider) {
        $stateProvider
            .state('base.newAct', {
                url: '/newAct',
                controller: 'newActController',
                controllerAs: 'vm',
                templateUrl: 'app/act/newAct/newAct.html'
            });
    }

}(angular));
