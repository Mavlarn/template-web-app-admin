angular.module('firstHouseAdmin.controllers', ['firstHouseAdmin'])
    .controller('AppCtrl', function($rootScope, $scope, $state, $log, $http, Auth, Principal, pageHeightHandler, SYS_CONFIG) {
        $log.debug('Init App Ctrl');
        // $rootScope.some_config = SYS_CONFIG.some_config;

        pageHeightHandler.setPageHeight();
        $rootScope.isAuthenticated = Principal.isAuthenticated;
        $rootScope.isInRole = Principal.isInRole;
        $rootScope.$state = $state;

    })
    .config(['$logProvider', function($logProvider) {
        $logProvider.debugEnabled(true);
    }]);
