'use strict';

angular.module('datatalkApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


