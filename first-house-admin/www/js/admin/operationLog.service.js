'use strict';

angular.module('Common.Admin')
    .factory('OperationLog', function ($resource) {
        return $resource('api/operationLogs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.createdTime = new Date(data.createdTime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
