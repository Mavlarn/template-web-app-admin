 'use strict';

angular.module('datatalkApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-datatalkApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-datatalkApp-params')});
                }
                return response;
            }
        };
    });
