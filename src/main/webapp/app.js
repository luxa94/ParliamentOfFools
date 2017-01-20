(function (angular) {

    'use strict';

    var parliamentOfFools = angular.module('parliament', ['ngStorage', 'ngRoute', 'ui.router', 'ngMaterial', 'Alertify']);
    parliamentOfFools.factory('authenticationResponseInterceptor', ['$q', '$location', authenticationResponseInterceptor]);
    parliamentOfFools
        .config(config)
        .run(run);

    function authenticationResponseInterceptor($q, $location, authorizationService) {
        return {
            response: function (response) {
                if (response.status === 401) {
                    console.log("Response 401");
                }
                return response || $q.when(response);
            },
            responseError: function (rejection) {
                if (rejection.status === 401) {
                    console.log("Response Error 401", rejection);
                    authorizationService.logout();
                }
                return $q.reject(rejection);
            },
            request: function (config) {
                return config;
            }
        }
    }

    function config($httpProvider, $urlRouterProvider, $mdThemingProvider) {
        $urlRouterProvider.otherwise('/login');
        $mdThemingProvider.theme('default').primaryPalette('grey').accentPalette('brown').warnPalette('blue-grey');
        $httpProvider.interceptors.push('authenticationResponseInterceptor');
    }

    function run($http, $localStorage, $state, $rootScope) {
        if ($localStorage.parliamentUser) {
            $http.defaults.headers.common.Authorization = $localStorage.parliamentUser.token;
            $rootScope.user = $localStorage.parliamentUser;
        }

        var publicStates = ['login', 'register'];

        $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
            var restrictedState = publicStates.indexOf(toState.name) === -1;
            if (restrictedState && !$localStorage.parliamentUser) {
                $state.go('login');
            } else if (!restrictedState && $localStorage.parliamentUser) {
                $state.go('base.home');
            }
        });
    }

})(angular);