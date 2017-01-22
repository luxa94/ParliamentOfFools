(function (angular) {

    'use strict';

    var BASE_URL = '/api/sessions';
    var ACTIVE_URL = BASE_URL + '/active';

    angular
        .module('parliament')
        .service('sessionService', ['$http', sessionService]);

    function sessionService($http) {
        return {
            create: create,
            findOne: findOne,
            findActive: findActive,
            activate: activate,
            edit: edit,
            delete: deleteOne
        };

        function create(sessionDTO) {
            return $http.post(BASE_URL, sessionDTO);
        }

        function findOne() {
            return $http.get(BASE_URL);
        }

        function findActive() {
            return $http.get(ACTIVE_URL);
        }

        function activate() {
            return $http.put(ACTIVE_URL);
        }

        function edit(sessionDTO) {
            return $http.put(BASE_URL, sessionDTO);
        }

        function deleteOne() {
            return $http.delete(BASE_URL);
        }

    }

}(angular));