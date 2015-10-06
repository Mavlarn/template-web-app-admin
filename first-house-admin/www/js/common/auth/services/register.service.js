'use strict';

angular.module('Common.Auth')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


