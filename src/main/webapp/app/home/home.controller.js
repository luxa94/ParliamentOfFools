(function (angular) {

    'use strict';

    angular.module('parliament')
        .controller('homeController', ['$rootScope', '$state', '$mdDialog', 'xhttpService', '$compile', '$document', '$scope', 'actService', homeController]);

    function homeController($rootScope, $state, $mdDialog, xhttpService, $compile, $document, $scope, actService) {
        var vm = this;
        $scope.vm = vm;
        vm.user = $rootScope.user;
        vm.acts = [];
        vm.searchTerm = "";

        vm.getPDF = getPDF;
        vm.searchByTerm = searchByTerm;
        activate();


        function activate() {

            var xml = xhttpService.get("/api/acts?term=" + vm.searchTerm);
            var style = xhttpService.get("cdcatalog.xsl");
            if (document.implementation && document.implementation.createDocument)
            {
                var xsltProcessor = new XSLTProcessor();
                xsltProcessor.importStylesheet(style);
                var resultDocument = xsltProcessor.transformToFragment(xml, document);
                var allActs = document.getElementById('allActs');
                angular.element(allActs).empty();
                angular.element(allActs).append($compile(resultDocument)($scope));
            }
        }

        function getPDF(actId) {
            debugger;
            console.log(actId);
            window.location.href = "api/acts/pdf/" + actId;
        }

        function searchByTerm() {
            activate();
        }



    }

}(angular));