'use strict';

angular.module('Common.Auth')
    .factory('Password', function ($resource) {
        return $resource('api/account/change_password', {}, {
        });
    })
    .factory('PasswordResetInit', function ($resource) {
        return $resource('api/account/reset_password/init', {}, {
        });
    })
    .factory('PasswordResetFinish', function ($resource) {
        return $resource('api/account/reset_password/finish', {}, {
        });
    });
