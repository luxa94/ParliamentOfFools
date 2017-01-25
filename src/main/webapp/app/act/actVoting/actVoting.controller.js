(function (angular) {
    'use strict';

    angular
        .module('parliament')
        .controller('actVotingController', actVotingController);

    actVotingController.$inject = ['$stateParams', 'actService', 'Alertify'];

    function actVotingController($stateParams, actService, Alertify) {
        var vm = this;

        vm.actId = $stateParams.id;
        vm.votes = {};

        activate();

        function activate() {
            var act = xhttpService.get('/api/acts/' + $stateParams.id);
            var style = xhttpService.get("act.xsl");
            if (document.implementation && document.implementation.createDocument) {
                var xsltProcessor = new XSLTProcessor();
                xsltProcessor.importStylesheet(style);
                var resultDocument = xsltProcessor.transformToFragment(act, document);
                document.getElementById("votingAct").appendChild(resultDocument);
            }
        }

        function vote() {
            actService.vote(vm.actId, vm.votes).then(function(response) {
                Alertifyy.success("You've successfully entered votes fto act");
            }).catch(function(error) {
               Alertify.error("Something wrong happened.")
            });
        }
    }
})(angular);
