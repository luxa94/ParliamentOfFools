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
        vm.statusTerm = "";
        vm.searchText = "";

        vm.getPDF = getPDF;
        vm.searchByTerm = searchByTerm;
        vm.searchByText = searchByText;
        vm.onStatusChoice = onStatusChoice;

        activate("", "");


        function activate(term, text) {

            var xml = xhttpService.get("/api/acts?term=" + term + "&text=" + text);
            var style = xhttpService.get("acts.xsl");
            if (document.implementation && document.implementation.createDocument) {
                var xsltProcessor = new XSLTProcessor();
                xsltProcessor.importStylesheet(style);
                var resultDocument = xsltProcessor.transformToFragment(xml, document);
                var allActs = document.getElementById('allActs');
                angular.element(allActs).empty();
                angular.element(allActs).append($compile(resultDocument)($scope));
            }
        }

        function getPDF(actId) {
            window.location.href = "api/acts/pdf/" + actId;
        }

        function searchByTerm() {
            activate(vm.searchTerm, "");
        }

        function onStatusChoice() {
            activate(vm.statusTerm, "");
        }

        function searchByText() {
            activate("", vm.searchText)
        }

    }

}(angular));