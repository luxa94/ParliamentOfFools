(function (angular) {

    'use strict';

    angular.module('parliament')
        .controller('homeController', ['$rootScope', '$state', '$mdDialog', 'xhttpService', '$compile', '$document', '$scope', homeController]);

    function homeController($rootScope, $state, $mdDialog, xhttpService, $compile, $document, $scope) {
        var vm = this;
        $scope.vm = vm;
        vm.user = $rootScope.user;
        vm.acts = [];
        vm.getPDF = getPDF;
        activate();


        function activate() {

            var xml = xhttpService.get("/api/acts");
            var style = xhttpService.get("cdcatalog.xsl");
            debugger;
            if (document.implementation && document.implementation.createDocument)
            {
                var xsltProcessor = new XSLTProcessor();
                xsltProcessor.importStylesheet(style);
                var resultDocument = xsltProcessor.transformToFragment(xml, document);
                var cdListDiv = document.getElementById('cd-list');
                angular.element(cdListDiv).append($compile(resultDocument)($scope));
            }
        }


        function getPDF() {
            console.log("heyy");
        }



    }

}(angular));