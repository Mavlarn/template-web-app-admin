angular.module('firstHouseApp.controllers', [])
.controller('AppCtrl', function($scope, $rootScope, $log, $location, $ionicModal, $timeout, Auth, Alertify,
          SYS_CONFIG, $ionicLoading, $interval, $ionicHistory, $window, localStorageService,
          $ionicPopup, DebugLog) {
	
	$log.log('init AppCtrl' + (new Date()).getTime());

	// $rootScope.some_config = SYS_CONFIG.some_config;

	// check auto-login with weixin code, or token. 
	var codeParam = $location.search().code;
	var serviceParam = $location.search().serviceId;
	var isDebugLog = $location.search().debugLog;

	if (isDebugLog) {
	    DebugLog.bind(function (msg) {
	      return Alertify.success(msg);
	    });
	} else {
	    DebugLog.bind(function (msg) {
	      return $log.log(msg);
	    });
	}

	$rootScope.weixinCode = codeParam;
	$rootScope.weixinServiceId = serviceParam;
	var theLocation = $location.absUrl();

	// save token from url, save leter will auto-login
	var tokenParam = $location.search().token;
	if (tokenParam) {
		// some url has token, which can be used to authenticate.
	    localStorageService.set('authorizationToken', tokenParam);
	}
	if (theLocation.lastIndexOf('#/app/debug') > 0) {
	    $rootScope.debugUrl = theLocation;
	    Alertify.success('In Debug.');
	} else {
	    if (codeParam && serviceParam) {
	        Auth.weixinLogin(codeParam, serviceParam);
	    } else {
	    	var userToken = localStorageService.get('authorizationToken');
	    	// Alertify.success('Has token in storage.');
	    	if (userToken) {
		        Auth.checkLogin();
		    }
	    }
	}

  
  $rootScope.showMessage = function (message, mstime) {
      if (!mstime) {
          mstime = 15000;
      }
      $ionicLoading.show({
        template: message,
        duration: mstime
      });
  };
  $rootScope.hideMessage = function() {
      $ionicLoading.hide();
  };
  $rootScope.isInRole = function(role) {
      return Auth.authorize(role);
  };
  $rootScope.showLoadingIconOnly= function (type,mstime) {
  	  //type:'small','big'
  	  type = (type =='small')?type:'big';
      if (!mstime) {
          mstime = 15000;
      }
      $ionicLoading.show({
        templateUrl: 'templates/common/'+type+'IconOnly.html' ,
        duration: mstime
      });
  };  
  
  // Form data for the login modal
  $scope.loginData = {};

  // Create the login modal that we will use later
  $ionicModal.fromTemplateUrl('templates/common/login.html', {
    scope: $scope
  }).then(function(modal) {
    $scope.loginModal = modal;
  });
  $scope.$on('$destroy', function() {
      if ($scope.loginModal) {
          $scope.loginModal.remove();
      }
  });

  // Triggered in the login modal to close it
  $rootScope.closeLogin = function() {
  	$log.log('closeLonin');
    if ($scope.loginModal) {
      $scope.loginModal.hide();
    }
  };

  // Open the login modal
  $rootScope.loginPopup = function(redirectStateName, redirectParam) {
  	if ($rootScope.isLogining) {
  		// is logining, not popup login.
  		return;
  	}

    if ($scope.loginModal) {
      $scope.loginModal.show(); 
    } else {
      // if the first page needs login, in this time, the getting account request is not returned,
      // it will request to login. But later, after login data returned, the loginConfirm will redirect
      // to the previous page. So, just noy popup the modal in this kind of scenario.
  	  $log.log('Skip login popup during initializing.'); 
    }
  };


  // Perform the login action when the user submits the login form
  $scope.doLogin = function() {
    $log.log('Doing login', $scope.loginData);
    $ionicLoading.show({
      template: '正在登录...'
    });
    Auth.login($scope.loginData, function () {
      $ionicLoading.hide();
      Alertify.success('登录成功');
      $rootScope.closeLogin();
    });
  };
  $scope.doLogout = function () {
    Auth.logout();
    $window.location.reload(true);
  };

})
.controller('HomeController', function ($rootScope,$scope) {
	$scope.data = {
	};

});
