'use strict';

angular.module('Common.Auth', ['http-auth-interceptor'])
    .factory('Auth', function ($rootScope, $http, authService, Account,
				localStorageService, Alertify) {
    var currentUser;
    var service = {
        weixinLogin: function (code, serviceId) {
            $rootScope.isLogining = true;
            var data = 'code=' + encodeURIComponent(code) + '&serviceId=' + serviceId;
            $http.post('api/wx/auth', data, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                ignoreAuthModule: 'ignoreAuthModule'
            })
            .success(function (data, status, headers, config) {
                $rootScope.isLogining = false;
                if (data.success) {
                    currentUser = data.user;
                    $rootScope.weixinOpenId = data.user.openId;
                    $rootScope.account = currentUser;
                    var xAuthTokenHeaderName = 'x-auth-token';
                    // weixin login token contain token and expires fields.
                    var tokenObj = data.token;
                    $http.defaults.headers.common[xAuthTokenHeaderName] = tokenObj.token;
                    localStorageService.set('authorizationToken', tokenObj.token);
                    // $cookieStore.put('authorizationToken', tokenObj.token);

                    authService.loginConfirmed(data.user, function (config) {
                        return config;
                    });
                } else {
                    $rootScope.weixinOpenId = data.openId;
                    $rootScope.$broadcast('event:auth-login-failed', null);
                    currentUser = null;
                    $rootScope.account = null;
                    // $rootScope.showMessage('登录失败:' + data.message, 1000);
                    // $rootScope.showMessage('openId:' + data.openId, 1000);
                }
            })
            .error(function (data, status, headers, config) {
                $rootScope.isLogining = false;
                $rootScope.$broadcast('event:auth-login-failed', status);
                currentUser = null;
                $rootScope.account = null;
            });

        },
        checkLogin: function () {
            // get user account with token, like auto-login. 
            var xAuthTokenHeaderName = 'x-auth-token';
            var userToken = localStorageService.get('authorizationToken');
            // Alertify.success("token from Storage:" + userToken);
            // if (!userToken || userToken.length === 0) {
            //     userToken = $cookieStore.get('authorizationToken');
            //     Alertify.success("token from cookie:" + userToken);
            // }

            if (userToken) {
                $rootScope.isLogining = true;
                $http.defaults.headers.common[xAuthTokenHeaderName] = userToken;

                Account.get(function (userData) {
                    $rootScope.isLogining = false;
                    currentUser = userData;
                    $rootScope.weixinOpenId = userData.openId;
                    $rootScope.account = currentUser;

                    authService.loginConfirmed(userData, function (config) { // Step 2 & 3
                        return config;
                    });
                }, function (resultError) {
                    $rootScope.isLogining = false;
                    localStorageService.remove('authorizationToken');
                    $rootScope.$broadcast('event:auth-login-failed', resultError);
                    currentUser = null;
                    $rootScope.account = null;
                    // $cookieStore.remove('authorizationToken');
                    delete $http.defaults.headers.common[xAuthTokenHeaderName];
                });
            }
        },
        login: function (user, callback) {
            var data = 'username=' + encodeURIComponent(user.login) + '&password=' + encodeURIComponent(user.password);

            $rootScope.isLogining = true;
            $http.post('api/authenticate', data, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                ignoreAuthModule: 'ignoreAuthModule'
            })
            .success(function (data, status, headers, config) {
                $rootScope.isLogining = false;
                var xAuthTokenHeaderName = 'x-auth-token';
                $http.defaults.headers.common[xAuthTokenHeaderName] = data.token; // Step 1
                localStorageService.set('authorizationToken', data.token);
                // $cookieStore.put('authorizationToken', data.token);

                // Need to inform the http-auth-interceptor that
                // the user has logged in successfully.  To do this, we pass in a function that
                // will configure the request headers with the authorization token so
                // previously failed requests(aka with status == 401) will be resent with the
                // authorization token placed in the header
                Account.get(function (userData) {
                    currentUser = userData;
                    $rootScope.weixinOpenId = userData.openId;
                    $rootScope.account = currentUser;
                    authService.loginConfirmed(userData, function (config) { // Step 2 & 3
                        return config;
                    });
                    if (callback) {
                        callback();
                    }
                });
            })
            .error(function (data, status, headers, config) {
                $rootScope.isLogining = false;
                $rootScope.$broadcast('event:auth-login-failed', status);
                currentUser = null;
                $rootScope.account = null;
            });
        },
        // this is used after activation, update user data in account.
        setLoginData: function(loginData) {
            currentUser = loginData.user;
            $rootScope.weixinOpenId = loginData.user.openId;
            $rootScope.account = currentUser;
            var xAuthTokenHeaderName = 'x-auth-token';
            // weixin login token contain token and expires fields.
            var tokenObj = loginData.token;
            $http.defaults.headers.common[xAuthTokenHeaderName] = tokenObj.token;
            localStorageService.set('authorizationToken', tokenObj.token);
            // $cookieStore.put('authorizationToken', tokenObj.token);

            authService.loginConfirmed(loginData.user, function (config) {
                return config;
            });
        },
        logout: function (user) {
            currentUser = null;
            localStorageService.remove('authorizationToken');
            // $cookieStore.remove('authorizationToken');
            delete $http.defaults.headers.common.Authorization;
            $rootScope.$broadcast('event:auth-logout-complete');
        },
        loginCancelled: function () {
            authService.loginCancelled();
        },
        isLoggedIn: function () {
            return currentUser && currentUser.roles && currentUser.login;
        },
        authorize: function (authorizedRoles) {
            if (!this.isLoggedIn()) {
                return false;
            }
            if (!angular.isArray(authorizedRoles)) {
                if (authorizedRoles == '*') {
                    return true;
                }
                authorizedRoles = [authorizedRoles];
            }

            var isAuthorized = false;
            angular.forEach(authorizedRoles, function (authorizedRole) {
                var authorized = (!!currentUser.login &&
                    currentUser.roles.indexOf(authorizedRole) !== -1);

                if (authorized || authorizedRole == '*') {
                    isAuthorized = true;
                }
            });

            return isAuthorized;
        },
        getUser: function () {
            return currentUser;
        },
        getToken: function () {
            var token = localStorageService.get('authorizationToken');
            // if (!token || token.length === 0) {
            //     token = $cookieStore.get('authorizationToken');
            // }
            return token;
        },
        hasValidToken: function () {
            var token = this.getToken();
            return token && token.expires && token.expires > new Date().getTime();
        }
    };
    return service;
});
