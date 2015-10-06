// Ionic Starter App

angular.module('firstHouseApp', ['ionic', 'LocalStorageModule', 'ngCacheBuster', 'ngResource',
  'ngAnimate-animate.css', 'angular-lodash', 'Alertify', 'angularMoment','ngSanitize',
  'Common', 'Common.utils', 'Common.Auth', 'firstHouseApp.controllers', 'firstHouseApp.services'])
.constant('SYS_CONFIG', {

})
.run(function ($rootScope, $state, $log, Auth, amMoment, Alertify) {
 	
    $log.log('in app run()' + (new Date()).getTime());
    $rootScope.isAppLoading = true;
    $rootScope.$state = $state;
    amMoment.changeLocale('zh-cn');
    
    $rootScope.$on('$stateChangeStart', function (event, toState, toStateParams, fromState, fromParams) {
        if (('data' in toState) && ('roles' in toState.data)) {
            if (!Auth.isLoggedIn()) {
                $log.log('Not log in');

                $rootScope.redirectName = toState;
                $rootScope.redirectParam = toStateParams;

                event.preventDefault();
                $rootScope.loginPopup(toState.name);
                return;
            }
            // has role based access defined, need to authorize.
            if (!Auth.authorize(toState.data.roles)) {
                $rootScope.showMessage("您没有权限访问该信息...");
                event.preventDefault();
                $state.go('accessdenied');
            }
        }
    });
    $rootScope.$on('$stateNotFound',function(event, unfoundState, fromState, fromParams){
        $log.log(unfoundState.to);
        Alertify.error('访问的路径没有定义:' + unfoundState.to);
    });
    
    // Call when the the client is confirmed
    $rootScope.$on('event:auth-loginConfirmed', function (config, userData) {
        $rootScope.authenticated = true;
        $rootScope.removeLoading();
        $rootScope.closeLogin();

        $rootScope.showMessage('登录成功', 1000);
        var redirectState = $rootScope.redirectName;
        var redirectParam = $rootScope.redirectParam;
        if (redirectState && $state.get(redirectState) ) {
            $state.go(redirectState, redirectParam);
        }
    });

    // Call when the 401 response is returned by the server
    $rootScope.$on('event:auth-loginRequired', function (rejection) {
        $rootScope.loginPopup();
      
    });

    // Call when the 403 response is returned by the server
    $rootScope.$on('event:auth-notAuthorized', function (rejection) {
        $rootScope.showMessage("您没有权限访问该信息...", 1000);
        $state.go('accessdenied');
    });

    // Call when the user logs out
    $rootScope.$on('event:auth-loginCancelled', function () {
      // we use popup login, if login canceled, the modal will be cloded, no need to do anything.
    });
    $rootScope.$on('event:auth-login-failed', function () {
        $rootScope.showMessage("登录失败...", 1000);
    });
})
.config(function ($stateProvider, $urlRouterProvider, $httpProvider, httpRequestInterceptorCacheBusterProvider,
  $compileProvider, ConfirmClickProvider, $ionicConfigProvider, $sceDelegateProvider, SYS_CONFIG) {
  //Cache everything except rest api requests
  httpRequestInterceptorCacheBusterProvider.setMatchlist([/.*api.*/, /.*protected.*/], true);
  $compileProvider.imgSrcSanitizationWhitelist(/^\s*(https?|ftp|mailto|content|weixin|wxlocalresource|wxLocalResource):/);
  $sceDelegateProvider.resourceUrlWhitelist([
       // Allow same origin resource loads.
       'self'
       // Allow loading from our assets domain.  Notice the difference between * and **.
       // ,API_EndPoint.storage + '**'
       ]);
  
  $ionicConfigProvider.navBar.alignTitle('center'); 

  ConfirmClickProvider.ask(function (question) {
    var Alertify = angular.element(document.body).injector().get('Alertify');
    return Alertify.confirm(question);
  });

  $httpProvider.interceptors.push('authInterceptor');
  $httpProvider.interceptors.push('httpErrorHandler');

  $stateProvider
    .state('app', {
    url: "/app",
    abstract: true,
    templateUrl: "templates/common/menu.html",
    resolve: {
    }
  })
  .state('debug', {
    parent: 'app',
    url: "/debug",
    views: {
      'menuContent': {
        templateUrl: "templates/common/debug.html"
      }
    }
  })
  .state('home', {
    parent: 'app',
    url: '/home',
    views: {
        'menuContent@app': {
            templateUrl: 'templates/common/home.html',
            controller: 'HomeController'
        }
    },
    resolve: {
    }
  });
  
  // if none of the above states are matched, use this as the fallback
  $urlRouterProvider.otherwise('/app/home');
});
