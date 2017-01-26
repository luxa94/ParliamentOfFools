(function (angular) {

    'use strict';

    angular.module('parliament')
        .controller('sessionController', ['$scope', '$compile', '$rootScope', 'Alertify', 'sessionService', '$state', 'xhttpService', sessionController]);

    function sessionController($scope, $compile, $rootScope, Alertify, sessionService, $state, xhttpService) {
        var vm = this;

        vm.activeSession = $rootScope.activeSession;
        vm.user = $rootScope.user;
        vm.minDate = new Date();

        vm.cancel = function () {
            vm.newSession = {};
        };

        vm.newAct = function () {
            $state.go('base.newAct');
        };

        vm.save = function () {
            sessionService.create(vm.newSession)
                .then(function (response) {
                    vm.activeSession = $rootScope.activeSession = response.data;
                    Alertify.success('Session created.');
                })
                .catch(function () {
                    Alertify.error('Unable to create session.');
                });
        };

        vm.activate = function () {
            sessionService.activate()
                .then(function (response) {
                    $rootScope.activeSession = response.data;
                    vm.activeSession = response.data;
                    vm.showPendingActs();
                    Alertify.success('Session is now active.');
                })
                .catch(function () {
                    Alertify.error('Something went wrong.');
                });
        };

        vm.finish = function () {
            sessionService.finish()
                .then(function () {
                    vm.activeSession = undefined;
                    $rootScope.activeSession = undefined;
                })
                .catch(function () {
                    Alertify.error('Could not finish session.')
                });
        };

        vm.showPendingActs = function () {
            var xml = xhttpService.get('/api/acts/pending');
            var style = xhttpService.get("pendingActs.xsl");

            if (document.implementation && document.implementation.createDocument) {
                var xsltProcessor = new XSLTProcessor();
                xsltProcessor.importStylesheet(style);
                var resultDocument = xsltProcessor.transformToFragment(xml, document);
                var allActs = document.getElementById('allActs');
                angular.element(allActs).empty();
                angular.element(allActs).append($compile(resultDocument)($scope));
            }
        };

        if (vm.activeSession && vm.activeSession.status === 'ACTIVE') {
            vm.showPendingActs();
        }
    }

}(angular));
