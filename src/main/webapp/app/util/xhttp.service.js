(function(angular) {
    'use strict';

    angular
        .module('parliament')
        .service('xhttpService', xhttpService);

    function xhttpService() {

        return {
            get: get,
            post: post,
            put: put,
            delete: deleteReq,
        };

        function get(url) {
            var xhttpRequest = new XMLHttpRequest();
            xhttpRequest.open('GET', url, false);
            xhttpRequest.send();
            return xhttpRequest.responseXML;
        }

        function post() {
            var xhttpRequest = new XMLHttpRequest();
            xhttpRequest.open('GET', "/api/acts", false);
            xhttpRequest.send();
            return xhttpRequest.responseXML;
        }

        function put() {

        }

        function deleteReq() {

        }
    }
})(angular);