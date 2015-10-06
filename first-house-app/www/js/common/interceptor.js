'use strict';

angular.module('firstHouseApp')
    .factory('authInterceptor', function($rootScope, $q, $location, localStorageService) {
        return {
            // Add authorization token to headers
            request: function(config) {
                config.headers = config.headers || {};
                var token = localStorageService.get('token');

                if (token && token.expires && token.expires > new Date().getTime()) {
                    config.headers['x-auth-token'] = token.token;
                }

                return config;
            }
        };
    })
    .factory('authExpiredInterceptor', function($rootScope, $q, $injector, localStorageService) {
        return {
            responseError: function(response) {
                // token has expired
                if (response.status === 401 && (response.data.error == 'invalid_token' || response.data.error == 'Unauthorized')) {
                    localStorageService.remove('token');
                    var Principal = $injector.get('Principal');
                    if (Principal.isAuthenticated()) {
                        var Auth = $injector.get('Auth');
                        Auth.authorize(true);
                    }
                }
                return $q.reject(response);
            }
        };
    })
    .factory('httpErrorHandler', function($q, $injector, $rootScope) {
        return {
            'responseError': function(rejection) {
                // do something on error
                var status = rejection.status;
                if (status === 500) {
                    var Alertify = $injector.get('Alertify');
                    var error = rejection.data.error;
                    var msg = rejection.data.message;
                    Alertify.error(error + ":<BR>" + msg);
                } else if (status == 401) {
                    var Auth = $injector.get('Auth');
                    var $state = $injector.get('$state');
                    var to = $rootScope.toState;
                    var params = $rootScope.toStateParams;
                    Auth.logout();
                    $rootScope.previousStateName = to;
                    $rootScope.previousStateParams = params;
                    $rootScope.loginPopup();
                } else if (status == 403) {
                    var $state = $injector.get('$state');
                    $state.go('accessdenied');
                }
                return $q.reject(rejection);
            }
        };
    })
    .factory('errorHandlerInterceptor', function($q, $rootScope) {
        return {
            'responseError': function(response) {
                if (!(response.status == 401 && response.data.path.indexOf("/api/account") == 0)) {
                    $rootScope.$emit('App.httpError', response);
                }
                return $q.reject(response);
            }
        };
    })
    .factory('notificationInterceptor', function($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-app-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, {
                        param: response.headers('X-app-params')
                    });
                }
                return response;
            },
        };
    });
