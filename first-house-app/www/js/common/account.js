'use strict';

angular.module('Common')
    .config(function ($stateProvider) {
    $stateProvider
        .state('account', {
            parent: 'app',
            url: '/account',
            abstract: true,
            data: {
                roles: ['ROLE_MANAGER', 'ROLE_TEACHER', 'ROLE_FAMILY'],
            }
        })
        .state('account.setting', {
            url: '/setting',
            cache:false,
            data: {
                roles: ['ROLE_MANAGER', 'ROLE_TEACHER', 'ROLE_FAMILY'],
            },
            views: {
                'menuContent@app': {
                    templateUrl: 'templates/account/settings.html',
                    controller: 'SettingsController'
                }
            },
            resolve: {
            }
        });
    })

    .controller('SettingsController', function ($rootScope,$scope,$ionicPopover) {
    	$rootScope.showLoadingIconOnly('big');
    	$scope.gettingUserInfo = true;
    	$rootScope.loadAccount(function(){
    		$scope.gettingUserInfo = false;
    		$rootScope.hideMessage();
	    	$scope.userInfo = angular.copy($rootScope.account);
    	}); 
		// $ionicPopover.fromTemplateUrl("ez-popover.html", {
		// 	scope: $scope
		// })
		// .then(function(popover){
		// 	$scope.popover = popover;
		// });
		// $scope.openPopover = function($event,obj) {
		// 	$scope.theChild = angular.copy(obj);
		// 	$scope.popover.show($event);
		// };
		// $scope.closePopover = function() {
		// 	$scope.popover.hide();
		// };
		// //销毁事件回调处理：清理popover对象
		// $scope.$on("$destroy", function() {
		// 	$scope.popover.remove();
		// });
		// // 隐藏事件回调处理
		// $scope.$on("popover.hidden", function() {
		// 	// Execute action
		// });
		// //删除事件回调处理
		// $scope.$on("popover.removed", function() {
		// 	// Execute action
		// });		
    });

