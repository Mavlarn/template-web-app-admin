'use strict';

angular.module('firstHouseAdmin')
    .config(function ($stateProvider) {
        $stateProvider
            .state('dashboard', {
                parent: 'site',
                url: '/dashboard',
                data: {
                    roles: ['ROLE_MANAGER', 'ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'templates/dashboard.html',
                        controller: 'DashboardController'
                    }
                },
                resolve: {
                    // resolvedDashData: function(DashData) {
                    //     return DashData.get().$promise;
                    // }
                }
            });
    })
   .factory('DashData', function ($resource) {
        return $resource('api/dash/admin');
    });
