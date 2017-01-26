(function (angular) {
    'use strict';

    angular
        .module('parliament')
        .controller('amendmentVotingController', amendmentVotingController);

    amendmentVotingController.$inject = ['$stateParams', 'xhttpService', 'Alertify', 'actService', '$http'];

    function amendmentVotingController($stateParams, xhttpService, Alertify, actService, $http) {
        var vm = this;

        vm.amendmentId = $stateParams.id;
        vm.status = "";
        vm.votes = {};
        vm.vote = vote;

        getStatus()
            .then(activate);

        function activate() {
            var act = xhttpService.get('/api/amendments/' + $stateParams.id);
            var style = xhttpService.get("amendment.xsl");
            if (document.implementation && document.implementation.createDocument) {
                var xsltProcessor = new XSLTProcessor();
                xsltProcessor.importStylesheet(style);
                var resultDocument = xsltProcessor.transformToFragment(act, document);
                document.getElementById("votingAmendment").appendChild(resultDocument);
            }
        }

        function vote() {
            $http.put("/api/amendments/" + vm.amendmentId + '/vote', vm.votes).then(function(response) {
                vm.status = response.data;
                Alertify.success("Uspešno ste dodali glasove za ovaj amandman.");
            }).catch(function(error) {
                Alertify.error("Desila se greška.")
            });
        }

        function getStatus() {
            return actService.getAmendmentJsonData(vm.amendmentId).then(function(response) {
                return vm.status = response;
            });
        }
    }
})(angular);
