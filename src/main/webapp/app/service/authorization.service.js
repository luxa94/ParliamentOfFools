(function (angular) {

    'use strict';

    angular
        .module('parliament')
        .service('authorizationService', ['$http', '$localStorage', '$state', '$rootScope', authorizationService]);

    function authorizationService($http, $localStorage, $state, $rootScope) {
        return {
            login: login,
            register: register,
            logout: logout
        };

        function login(loginDTO, callback) {
            $http.post('/api/authentication/login', loginDTO)
                .then(function (response) {
                    successfulAuthentication(response, callback);
                })
                .catch(function (reason) {
                    console.log(reason);
                    if (callback) {
                        callback(false);
                    }
                });
        }

        function register(registerDTO, callback) {
            $http.post('/api/authentication/register', registerDTO)
                .then(function (response) {
                    successfulAuthentication(response, callback);
                })
                .catch(function (reason) {
                    console.log(reason);
                    if (callback) {
                        callback(false);
                    }
                });
        }

        function logout() {
            delete $localStorage.parliamentUser;
            $http.defaults.headers.common.Authorization = '';
            $state.go('login');
        }

        function successfulAuthentication(response, callback) {
            var user = response.data;
            if (user) {
                $http.defaults.headers.common.Authorization = user.token;
                $localStorage.parliamentUser = user;
                $rootScope.user = $localStorage.parliamentUser;

                if (callback) {
                    callback(true);
                }
                $state.go('base.home');
            }
        }
    }

})(angular);