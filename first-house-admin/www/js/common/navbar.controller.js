'use strict';

function NavbarController($scope, $state, Auth) {

    $scope.logout = function() {
        Auth.logout();
        $state.go('login');
    };
}

function SidebarController($scope, $state, $log) {
    $scope.collapse = function(id) {
        $log.debug(id);
        if ($scope.currentGroup == id) {
            $scope.currentGroup = 0;
        } else {
            $scope.currentGroup = id;
        }
    };
}

angular.module('Common')
    .controller('NavbarController', NavbarController)
    .controller('SidebarController', SidebarController);
