'use strict';

angular.module('firstHouseApp')
    .config(function ($stateProvider) {
    $stateProvider
        .state('activate', {
            parent: 'app',
            url: "/activate",
            abstract: true
        })
        .state('activate.employee', {
            url: "/employee",
            views: {
                'menuContent@app': {
                    templateUrl: "templates/activate/activate_employee.html",
                    controller: 'EmployeeActivateCtrl'
                }
            }
        })
        .state('activate.customer', {
            url: "/customer",
            views: {
                'menuContent@app': {
                    templateUrl: "templates/activate/activate_customer.html",
                    controller: 'CustomerActivateCtrl'
                }
            }
        });
        
    })
    
    .controller('CustomerActivateCtrl', function ($rootScope, $scope, $log, Auth, Alertify, $ionicPopup, $ionicHistory) {

        if (!$scope.theUser.openId || angular.isUndefined($scope.theUser.serviceId)) {
            var alertPopup = $ionicPopup.alert({
                title: '宝宝云提示',
                template: '<h4>微信数据错误，退出后请重试。</h4>',
                okText: '退出',
                okType: 'button-energized borderless'
            });
            alertPopup.then(function (res) {
				wx.closeWindow(); 
            });
        }
        $scope.activateCustomer = function () {
            if ($scope.theUser.childRealName === '') {
                Alertify.error("请输入宝宝的姓名。");
                return 0;
            }
            if ($scope.theUser.activationKey === '') {
                Alertify.error("请输入授权码。");
                return 0;
            }
            // $rootScope.showMessage('正在绑定...');

        };
    
    })
    .controller('EmployeeActivateCtrl', function ($rootScope, $scope, $log, Auth, ActivateUser, Alertify, $ionicPopup, $ionicHistory) {
        $scope.theUser = {
            openId: $rootScope.weixinOpenId,
            serviceId: $rootScope.weixinServiceId,
            realName: '',
            activationKey: '',
        };
        $log.log($scope.theUser);
        if (angular.isUndefined($scope.theUser.openId) || angular.isUndefined($scope.theUser.serviceId)) {
            var alertPopup = $ionicPopup.alert({
                title: '宝宝云提示',
                template: '<h4>微信数据错误，退出后请重试。</h4>',
                okText: '退出',
                okType: 'button-energized borderless'
            });
            alertPopup.then(function (res) {
                wx.closeWindow(); 
            });
        }
        $scope.activateUser = function () {
            if ($scope.theUser.realName == '') {
                Alertify.error("亲，请输入您的姓名。");
                return 0;
            }
            if ($scope.theUser.activationKey == '') {
                Alertify.error("亲，请输入授权码。");
                return 0;
            }
            $rootScope.showMessage('正在绑定...');
            ActivateUser.save($scope.theUser, function (resultData) {
                Alertify.success('绑定成功！');
                $rootScope.hideMessage();
                Auth.setLoginData(resultData);
                $ionicHistory.nextViewOptions({
                  disableBack: true
                });
                $rootScope.$state.go('userhome');
            }, function (error) {
                $rootScope.hideMessage();
                $log.log(error);
                var msg = '绑定错误';
                if (error.data && error.data.message) {
                    msg += ': ' + error.data.message;
                }
                Alertify.error(msg);
            });
        };
    });