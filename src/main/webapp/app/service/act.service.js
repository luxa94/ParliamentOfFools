(function(angular) {
    'use strict';

    angular
        .module('parliament')
        .service('actService', actService);

    actService.$inject = ['$http'];

    function actService($http) {

        return {
            generatePdf: generatePdf
        };

        function generatePdf(actId) {
            return $http.get("api/acts/pdf/" + actId);
        }
    }
})(angular);