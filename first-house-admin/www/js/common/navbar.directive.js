'use strict';

function activeLink(location) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            var clazz = attrs.activeLink;
            var path = attrs.href;
            path = path.substring(1); //hack because path does bot return including hashbang
            scope.location = location;
            scope.$watch('location.path()', function(newPath) {
                if (path === newPath) {
                    element.addClass(clazz);
                } else {
                    element.removeClass(clazz);
                }
            });
        }
    };
}


/**
 * pageTitle - Directive for set Page title - mata title
 */
function pageTitle($rootScope, $timeout) {
    return {
        link: function(scope, element) {
            var listener = function(event, toState, toParams, fromState, fromParams) {
                // Default title - load on Dashboard 1
                var title = 'Admin Home';
                // Create your own title pattern
                if (toState.data && toState.data.pageTitle) title = 'Home | ' + toState.data.pageTitle;
                $timeout(function() {
                    element.text(title);

                });
            };
            $rootScope.$on('$stateChangeStart', listener);
        }
    };
}

function collapseSideMenu($timeout) {
    return {
        link: function(scope, element) {
            element.bind('click', function() {
                var theUL = element.next();
                // theUL.removeClass('collapse');
                theUL.toggleClass('collapsing');
                theUL.toggleClass('collapse');
                theUL.toggleClass('in');
            });
        }
    };
}

/**
 * minimalizaSidebar - Directive for minimalize sidebar
*/
function minimalizaSidebar($timeout) {
    return {
        restrict: 'A',
        template: '<a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="" ng-click="minimalize()"><i class="fa fa-bars"></i></a>',
        controller: function ($scope, $element) {
            $scope.minimalize = function () {
                $('body').toggleClass('mini-navbar');
                if (!$('body').hasClass('mini-navbar') || $('body').hasClass('body-small')) {
                    // Hide menu in order to smoothly turn on when maximize menu
                    $('#side-menu').hide();
                    // For smoothly turn on menu
                    $timeout(function () {
                        $('#side-menu').fadeIn(500);
                        }, 100);
                } else {
                    // Remove all inline style from jquery fadeIn function to reset menu state
                    $('#side-menu').removeAttr('style');
                }
            };
        }
    };
}

angular
    .module('Common')
    .directive('activeLink', activeLink)
    .directive('pageTitle', pageTitle)
    .directive('collapseSideMenu', collapseSideMenu)
    .directive('minimalizaSidebar', minimalizaSidebar);

