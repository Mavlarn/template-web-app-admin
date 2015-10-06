'use strict';

angular.module('Common')
    .config(function ($stateProvider) {
        $stateProvider
            .state('error', {
                parent: 'app',
                url: '/error',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/common/error.html'
                    }
                },
                resolve: {
                }
            })
            .state('accessdenied', {
                parent: 'app',
                url: '/accessdenied',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/common/accessdenied.html'
                    }
                },
                resolve: {
                }
            });
    });
