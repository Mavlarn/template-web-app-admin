'use strict';

angular.module('Common.Admin')
    .config(function ($stateProvider) {
        $stateProvider
            .state('configuration', {
                parent: 'admin',
                url: '/configuration',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'configuration.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'templates/admin/configuration.html',
                        controller: 'ConfigurationController'
                    }
                },
                resolve: {
                }
            });
    })
    .controller('ConfigurationController', function ($scope, ConfigurationService) {
        ConfigurationService.get().then(function(configuration) {
            $scope.configuration = configuration;
        });
    });
