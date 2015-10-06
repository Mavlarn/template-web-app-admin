'use strict';

angular.module('Common.Auth')
    .factory('Activate', function ($resource) {
        return $resource('api/activate', {}, {
            'get': { method: 'GET', params: {}, isArray: false}
        });
    });


