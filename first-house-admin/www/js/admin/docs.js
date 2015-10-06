'use strict';

angular.module('Common.Admin')
    .config(function ($stateProvider) {
        $stateProvider
            .state('docs', {
                parent: 'admin',
                url: '/docs',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'API'
                },
                views: {
                    'content@': {
                        templateUrl: 'templates/admin/docs.html'
                    }
                },
                resolve: {
                    
                }
            });
    });
