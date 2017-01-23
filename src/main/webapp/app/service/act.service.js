(function (angular) {

    'use strict';

    var BASE_URL = '/api/acts';

    angular
        .module('parliament')
        .service('actService', ['$http', actService]);

    function actService($http) {
        return {
            create: create,
            generatePdf: generatePdf,
            searchByTerm: searchByTerm
        };

        function create(act) {
            return $http({
                method: 'POST',
                url: BASE_URL,
                data: act,
                headers: { "Content-Type": 'application/xml' }
            });
        }

        function generatePdf(actId) {
            return $http.get("api/acts/pdf/" + actId);
        }

        function searchByTerm(term) {
            return $http.get()
        }

    }

}(angular));
