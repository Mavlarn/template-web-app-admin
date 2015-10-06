'use strict';

angular.module('Common.Admin')
    .config(function ($stateProvider) {
        $stateProvider
            .state('logs', {
                parent: 'admin',
                url: '/logs',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'logs.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'templates/admin/logs.html',
                        controller: 'LogsController'
                    }
                },
                resolve: {
                }
            });
    })
    .controller('LogsController', function ($scope, LogsService) {
        $scope.loggers = LogsService.findAll();

        $scope.changeLevel = function (name, level) {
            LogsService.changeLevel({name: name, level: level}, function () {
                $scope.loggers = LogsService.findAll();
            });
        };
    });
