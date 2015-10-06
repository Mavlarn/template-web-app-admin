'use strict';
angular.module('firstHouseAdmin')
    .config(function($stateProvider) {
        $stateProvider
            .state('user', {
                parent: 'site',
                abstract: true,
                data: {
                    roles: ['ROLE_ADMIN', 'ROLE_MANAGER']
                }
            })
            .state('user.list', {
                parent: 'user',
                url: '/user/list',
                views: {
                    'content@': {
                        templateUrl: 'templates/user/user-list.html',
                        controller: 'UserListController'
                    }
                }
            })
            .state('user.form', {
                parent: 'user',
                url: '/user/form',
                views: {
                    'content@': {
                        templateUrl: 'templates/user/user-form.html',
                        controller: 'UserFormController'
                    }
                }
            });
    })
    .factory('UserService', function($resource) {
        return $resource('api/users/:login', {}, {
            'query': { method: 'GET', isArray: true },
            'get': { method: 'GET' },
            'update': { method: 'PUT' }
        });
    })
    .factory('UserDataHolder', function () {
      var currentData;
      return {
        getCurrent: function() {
          return currentData;
        },
        setCurrent: function (theDate) {
          currentData = theDate;
        }
      };
    });
