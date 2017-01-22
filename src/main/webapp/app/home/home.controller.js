(function (angular) {

    'use strict';

    angular.module('parliament')
        .controller('homeController', ['$rootScope', '$state', '$mdDialog', 'xhttpService', '$compile', '$document', '$scope', 'actService', homeController]);

    function homeController($rootScope, $state, $mdDialog, xhttpService, $compile, $document, $scope, actService) {
        var vm = this;
        $scope.vm = vm;
        vm.user = $rootScope.user;
        vm.acts = [];
        vm.getPDF = getPDF;
        activate();


        function activate() {

            var xml = xhttpService.get("/api/acts");
            var style = xhttpService.get("cdcatalog.xsl");
            if (document.implementation && document.implementation.createDocument)
            {
                var xsltProcessor = new XSLTProcessor();
                xsltProcessor.importStylesheet(style);
                var resultDocument = xsltProcessor.transformToFragment(xml, document);
                var allActs = document.getElementById('allActs');
                angular.element(allActs).append($compile(resultDocument)($scope));
            }
        }


        function getPDF(actId) {
            debugger;
            console.log(actId);
            actService.generatePdf(actId).then(function(response) {
                console.log('success');
            }).catch(function(error) {
                console.log(error);
            })
        }



    }

}(angular));