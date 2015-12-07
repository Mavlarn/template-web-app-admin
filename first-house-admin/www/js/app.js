
angular.module('firstHouseAdmin', ['LocalStorageModule', 'ngMessages', 'ngResource', 'ui.router', 'ngCookies',
    'ngCacheBuster', 'ngSanitize', 'angularFileUpload', 'angular-loading-bar', 'ui.bootstrap', 'ui.select',
    'ng.ueditor', 'smart-table', 'angular-lodash', 'Alertify', 'nvd3', 'Common', 'Common.utils', 'Common.Auth',
    'Common.Admin', 'firstHouseAdmin.controllers', 'firstHouseAdmin.services'
])

// for dev server.
.constant('SYS_CONFIG', {

})
.run(function($state, $rootScope, Auth, Principal, ENV, VERSION) {
    $rootScope.ENV = ENV;
    $rootScope.VERSION = VERSION;

    $rootScope.$on('$stateChangeStart', function(event, toState, toStateParams) {
        $rootScope.toState = toState;
        $rootScope.toStateParams = toStateParams;

        if (Principal.isIdentityResolved()) {
            Auth.authorize();
        }
    });
    $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams, $window) {
        var titleKey = 'DataTalk';

        $rootScope.previousStateName = fromState.name;
        $rootScope.previousStateParams = fromParams;

        // Set the page title key to the one configured in state or use default one
        if (toState.data.pageTitle) {
            titleKey = toState.data.pageTitle;
        }
        $window.document.title = titleKey;
    });
    
    $rootScope.back = function() {
        // If previous state is 'activate' or do not exist go to 'home'
        if ($rootScope.previousStateName === 'login' || $state.get($rootScope.previousStateName) === null) {
            $state.go('home');
        } else {
            $state.go($rootScope.previousStateName, $rootScope.previousStateParams);
        }
    };
})
.constant('datepickerConfig', {
    formatDay: 'dd',
    formatMonth: 'MMMM',
    formatYear: 'yyyy',
    formatDayHeader: 'EEE',
    formatDayTitle: 'MMMM yyyy',
    formatMonthTitle: 'yyyy',
    datepickerMode: 'month',
    minMode: 'month',
    maxMode: 'year',
    showWeeks: true,
    startingDay: 1,
    yearRange: 20,
    minDate: null,
    maxDate: null
})

.factory('pageHeightHandler', function($rootScope, $interval, $log) {
    $interval(function() {
        $rootScope.pageHeight = {
            "min-height": document.documentElement.clientHeight + 'px'
        };
    }, 500);

    return {
        'setPageHeight': function() {
            $rootScope.pageHeight = {
                "min-height": document.documentElement.clientHeight + 'px'
            };
        }
    };
})

.config(function($stateProvider, $urlRouterProvider, $httpProvider, uiSelectConfig,
    httpRequestInterceptorCacheBusterProvider, ConfirmClickProvider, $sceDelegateProvider, SYS_CONFIG) {

    //Cache everything except rest api requests
    httpRequestInterceptorCacheBusterProvider.setMatchlist([/.*api.*/, /.*protected.*/], true);

    $sceDelegateProvider.resourceUrlWhitelist([
        // Allow same origin resource loads.
        'self'
        // Allow loading from our assets domain.  Notice the difference between * and **.
        //,SYS_CONFIG.storage + '**'
    ]);

    ConfirmClickProvider.ask(function(question) {
        var Alertify = angular.element(document.body).injector().get('Alertify');
        return Alertify.confirm(question);
    });
    uiSelectConfig.theme = 'bootstrap';
    $httpProvider.interceptors.push('authInterceptor');
    $httpProvider.interceptors.push('httpErrorHandler');
    $httpProvider.interceptors.push('authExpiredInterceptor');

    $urlRouterProvider.otherwise('/dashboard');
    $stateProvider.state('site', {
        views: {
            'navbar@': {
                templateUrl: 'templates/common/navbar.html',
                controller: 'NavbarController'
            },
            'sidebar@': {
                templateUrl: 'templates/common/sidebar.html',
                controller: 'SidebarController'
            }
        },
        resolve: {
            authorize: ['Auth',
                function(Auth) {
                    return Auth.authorize();
                }
            ]
        }
    })
    .state('home', {
        parent: 'site',
        url: '/home',
        data: {
            roles: []
        },
        views: {
            'content@': {
                templateUrl: 'templates/main.html'
            }
        },
        resolve: {}
    });

});
