angular.module('firstHouseAdmin.controllers', ['firstHouseAdmin'])
    .controller('AppCtrl', function($rootScope, $scope, $state, $log, $http, Auth, Principal, pageHeightHandler, SYS_CONFIG) {
        $log.debug('Init App Ctrl');
        // $rootScope.some_config = SYS_CONFIG.some_config;

        pageHeightHandler.setPageHeight();
        $rootScope.isAuthenticated = Principal.isAuthenticated;
        $rootScope.isInRole = Principal.isInRole;
        $rootScope.$state = $state;

        $rootScope.goback = function() {
            $log.debug('$rootScope.goback()');
            // If previous state is 'activate' or do not exist go to 'home'
            if ($rootScope.previousStateName === 'activate' || $state.get($rootScope.previousStateName) === null) {
                $state.go('home');
            } else {
                $state.go($rootScope.previousStateName, $rootScope.previousStateParams);
            }
        };

    })
    .config(['$logProvider', function($logProvider) {
        $logProvider.debugEnabled(true);
    }]);
