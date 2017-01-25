(function(angular) {
    'use strict';

    angular
        .module('parliament')
        .controller('actController', actController);

    actController.$inject = ['$stateParams', 'xhttpService', 'actService', '$http', '$rootScope', 'Alertify', '$state'];

    function actController($stateParams, xhttpService, actService, $http, $rootScope, Alertify, $state) {

        var vm = this;
        vm.activeSession = $rootScope.activeSession;
        vm.status = '';
        activate();

        function activate() {
            var act = xhttpService.get('/api/acts/' + $stateParams.id);
            var style = xhttpService.get("act.xsl");
            if (document.implementation && document.implementation.createDocument)
            {
                var xsltProcessor = new XSLTProcessor();
                xsltProcessor.importStylesheet(style);
                var resultDocument = xsltProcessor.transformToFragment(act, document);
                document.getElementById("act").appendChild(resultDocument);
            }
        }

        getJsonData();

        function getJsonData() {
            actService.getJsonData($stateParams.id).then(function(status) {
               vm.status =  status;
            });
        }

        vm.cancelAmendment = function () {
            vm.amendment = '';
        };

        vm.submitAmendment = function () {
            $http.post('/api/amendments/act/' + $stateParams.id, vm.amendment)
                .then(function (response) {
                    Alertify.success('Amendment created.');
                    $state.go('base.home');
                })
                .catch(function () {
                    Alertify.error('Could not create amendment.')
                });
        };
    }
})(angular);