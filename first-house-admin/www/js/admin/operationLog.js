'use strict';

angular.module('Common.Admin')
    .config(function ($stateProvider) {
        $stateProvider
            .state('operationLog', {
                parent: 'entity',
                url: '/operationLog',
                data: {
                    roles: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'templates/admin/operationLogs.html',
                        controller: 'OperationLogController'
                    }
                },
                resolve: {
                }
            });
    })
    .controller('OperationLogController', function ($scope, OperationLog) {
        $scope.operationLogs = [];
        $scope.loadAll = function() {
            OperationLog.query(function(result) {
               $scope.operationLogs = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            OperationLog.update($scope.operationLog,
                function () {
                    $scope.loadAll();
                    $('#saveOperationLogModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            OperationLog.get({id: id}, function(result) {
                $scope.operationLog = result;
                $('#saveOperationLogModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            OperationLog.get({id: id}, function(result) {
                $scope.operationLog = result;
                $('#deleteOperationLogConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            OperationLog.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteOperationLogConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.operationLog = {operatorId: null, operatorName: null, operation: null, createdTime: null, result: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });

