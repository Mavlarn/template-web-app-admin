'use strict';

angular.module('Common')
    .config(function ($stateProvider) {
        $stateProvider
            .state('login', {
                url: '/login',
                data: {
                    roles: [], 
                },
                views: {
                    'content@': {
                        templateUrl: 'templates/common/login.html',
                        controller: 'LoginController'
                    }
                },
                resolve: {
                }
            })
            .state('settings', {
                url: '/settings',
                data: {
                    roles: ['ROLE_USER'],
                },
                views: {
                    'content@': {
                        templateUrl: 'templates/common/user_settings.html',
                        controller: 'SettingsController'
                    }
                },
                resolve: {
                }
            })
            .state('password', {
            	parent: 'site',
                url: '/password',
                data: {
                    roles: ['ROLE_ADMIN', 'ROLE_MANAGER']
                },
                views: {
                    'content@': {
                        templateUrl: 'templates/common/password.html',
                        controller: 'PasswordController'
                    }
                },
                resolve: {
                }
            });
    })
	
    .controller('LoginController', function ($rootScope, $scope, $state, $timeout, Auth) {
        $scope.user = {};
        $scope.errors = {};

        $scope.rememberMe = true;
        $timeout(function (){angular.element('[ng-model="username"]').focus();});
        $scope.login = function () {
            Auth.login({
                username: $scope.username,
                password: $scope.password,
                rememberMe: $scope.rememberMe
            }).then(function () {
                $scope.authenticationError = false;
                $state.go('dashboard');
            }).catch(function () {
                $scope.authenticationError = true;
            });
        };
    })
    .controller('RegisterController', function ($scope, $timeout, Auth) {
        $scope.success = null;
        $scope.error = null;
        $scope.doNotMatch = null;
        $scope.errorUserExists = null;
        $scope.registerAccount = {};
        $timeout(function (){angular.element('[ng-model="registerAccount.login"]').focus();});

        $scope.register = function () {
            if ($scope.registerAccount.password !== $scope.confirmPassword) {
                $scope.doNotMatch = 'ERROR';
            } else {
                $scope.doNotMatch = null;
                $scope.error = null;
                $scope.errorUserExists = null;
                $scope.errorEmailExists = null;

                Auth.createAccount($scope.registerAccount).then(function () {
                    $scope.success = 'OK';
                }).catch(function (response) {
                    $scope.success = null;
                    if (response.status === 400 && response.data === 'login already in use') {
                        $scope.errorUserExists = 'ERROR';
                    } else if (response.status === 400 && response.data === 'e-mail address already in use') {
                        $scope.errorEmailExists = 'ERROR';
                    } else {
                        $scope.error = 'ERROR';
                    }
                });
            }
        };
    })
    .controller('SettingsController', function ($scope, Principal, Auth) {
        $scope.success = null;
        $scope.error = null;
        Principal.identity().then(function(account) {
            $scope.settingsAccount = account;
        });

        $scope.save = function () {
            Auth.updateAccount($scope.settingsAccount).then(function() {
                $scope.error = null;
                $scope.success = 'OK';
                Principal.identity().then(function(account) {
                    $scope.settingsAccount = account;
                });
            }).catch(function() {
                $scope.success = null;
                $scope.error = 'ERROR';
            });
        };
    })
    .controller('PasswordController', function ($scope,$rootScope, Auth, ChangePassword,Alertify) { 
 		$scope.data = {
 			password: '',
 			password2: '',
 		};
 		
        $scope.savePassword = function () {
        	if($scope.data.password == ''){
        		Alertify.error('请输入新密码!');
        		return 0;
        	}
        	if($scope.data.password2 == ''){
        		Alertify.error('请再次输入新密码!');
        		return 0;        		
        	}
        	if($scope.data.password2 != $scope.data.password){
        		Alertify.error('两次密码必须一致!');
        		return 0;        		
        	}
 			ChangePassword.save({password: $scope.data.password}, function(){
 				Alertify.success('修改密码成功!');
 			},function(){
 				Alertify.error('修改密码失败!');
 			});
        };
    })
    .factory('ChangePassword', function ($resource) {
        return $resource('api/account/change_password', {}, {
            'save': { method: 'POST', isArray: true}
        });
    })    
    ;

