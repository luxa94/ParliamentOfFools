(function(angular) {
    'use strict';

    var PREFIX = 'http://www.fools.gov.rs/acts/';
    var STATUS = PREFIX + 'status';

    angular
        .module('parliament')
        .controller('actController', actController);

    actController.$inject = ['$stateParams', 'xhttpService', '$http', '$rootScope', 'Alertify', '$state'];

    function actController($stateParams, xhttpService, $http, $rootScope, Alertify, $state) {
        var ID = PREFIX + $stateParams.id;

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

        $http.get('/api/acts/export/json')
            .then(function (response) {
                vm.status = response.data[ID][STATUS][0].value;
            })
            .catch(function (reason) {
                console.log(reason);
            });

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