'use strict';

function UserListController($scope, $rootScope, UserService, UserDataHolder) {

    $scope.users = [];
    $scope.displayedUsers = [];

    var getUserList = function() {
    	UserService.query({}, function(users) {
    		$scope.users = users;
    		$scope.displayedUsers = [].concat(users);
    	});
    };
    getUserList();

    $scope.createUser = function() {
        $rootScope.$state.go('user.form');
    };
    $scope.editUser = function(user) {
        UserDataHolder.setCurrent(user);
        $rootScope.$state.go('user.form');
    };

}

function UserFormController($scope, $state, $stateParams, Alertify, UserService, UserDataHolder) {
    $scope.data = {
        user: UserDataHolder.getCurrent()
    };
    $scope.isNew = $scope.data.user == null || $scope.data.user.login == null;
    $scope.saveUser = function() {
        UserService.save($scope.data.user, function() {
            $scope.$state.go('user.list');
        })
    }
}
angular.module('firstHouseAdmin')
    .controller('UserFormController', UserFormController)
    .controller('UserListController', UserListController);
