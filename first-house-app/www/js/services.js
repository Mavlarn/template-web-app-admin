angular.module('firstHouseApp.services', [])
   .factory('Notification', function ($resource) {
        return $resource('api/notifications/:type/:schoolId/:classId/:notificationId', {}, {
            'save': { method: 'POST'},
            'query': { method: 'GET', isArray: true},
            'update': { method: 'PUT'},
            
        });
    })
	.factory('Dashboard', function($resource){
		return $resource('/api/dash/user', { 
		} );
	});
