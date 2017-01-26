(function (angular) {

    'use strict';

    var BASE_URL = '/api/acts';
    var PREFIX = 'http://www.fools.gov.rs/acts/';
    var STATUS = PREFIX + 'status';
    var AMENDMENT_PREFIX = 'http://www.fools.gov.rs/amendments/';
    var AMENDMENT_STATUS = AMENDMENT_PREFIX + 'status';

    angular
        .module('parliament')
        .service('actService', ['$http', actService]);

    function actService($http) {
        return {
            create: create,
            generatePdf: generatePdf,
            vote: vote,
            getJsonData: getJsonData,
            getAmendmentJsonData: getAmendmentJsonData
        };

        function create(act) {
            return $http({
                method: 'POST',
                url: BASE_URL,
                data: act,
                headers: {"Content-Type": 'application/xml'}
            });
        }

        function generatePdf(actId) {
            return $http.get("api/acts/pdf/" + actId);
        }

        function vote(actId, vote) {
            return $http.put("api/acts/" + actId + "/vote", vote);
        }

        function getJsonData(actId) {
            var ID = PREFIX + actId;
            return $http.get('/api/acts/export/json')
                .then(function (response) {
                    return response.data[ID][STATUS][0].value;
                })
                .catch(function (reason) {
                    console.log(reason);
                });
        }

        function getAmendmentJsonData(amendmentId) {
            var ID = AMENDMENT_PREFIX + amendmentId;
            return $http.get('/api/amendments/export/json')
                .then(function (response) {
                    return response.data[ID][AMENDMENT_STATUS][0].value;
                })
                .catch(function (reason) {
                    console.log(reason);
                });
        }

    }

}(angular));
