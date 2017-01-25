(function (angular) {
    'use strict';

    angular
        .module('parliament')
        .controller('actVotingController', actVotingController);

    actVotingController.$inject = ['$stateParams', 'actService', 'xhttpService', 'Alertify'];

    function actVotingController($stateParams, actService, xhttpService, Alertify) {
        var vm = this;

        vm.actId = $stateParams.id;
        vm.status = "";
        vm.votes = {};
        vm.vote = vote;

        getStatus()
            .then(activate);

        function activate() {
            var act = xhttpService.get('/api/acts/' + $stateParams.id);
            var style = xhttpService.get("act.xsl");
            if (document.implementation && document.implementation.createDocument) {
                var xsltProcessor = new XSLTProcessor();
                xsltProcessor.importStylesheet(style);
                var resultDocument = xsltProcessor.transformToFragment(act, document);
                document.getElementById("votingAct").appendChild(resultDocument);
            }

            if (vm.status === 'ACCEPTED') {
                var amendments = xhttpService.get('api/acts/' + $stateParams.id + '/amendments/pending');
                style = xhttpService.get("votingActAmendments.xsl");
                var xsltProcessor = new XSLTProcessor();
                xsltProcessor.importStylesheet(style);
                var resultDocument = xsltProcessor.transformToFragment(amendments, document);
                document.getElementById("votingActAmendments").appendChild(resultDocument);

            }
        }

        function vote() {
            actService.vote(vm.actId, vm.votes).then(function(response) {
                vm.status = response.data;
                Alertify.success("Uspešno ste dodali glasove za ovaj akt.");
            }).catch(function(error) {
               Alertify.error("Desila se greška.")
            });
        }

        function getStatus() {
            return actService.getJsonData(vm.actId).then(function(response) {
                return vm.status = response;
            });
        }
    }
})(angular);
