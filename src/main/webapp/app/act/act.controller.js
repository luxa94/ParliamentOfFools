(function(angular) {
    'use strict';

    angular
        .module('parliament')
        .controller('actController', actController);

    actController.$inject = ['$stateParams', 'xhttpService'];

    function actController($stateParams, xhttpService) {

        var vm = this;

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
    }
})(angular);