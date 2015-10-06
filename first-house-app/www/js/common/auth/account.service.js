'use strict';

angular.module('Common.Auth')
    .factory('Account', function Account($resource) { 
        return $resource('api/account',{},{
        	'get':{method:'GET',
        		transformResponse:function(data){
        			data=angular.fromJson(data);
        			data.bindDate = new Date(data.bindDate);
        			return data;
        		}},
        });
    })
	.factory('ActivateUser', function($resource){
        return $resource('api/wx/activateUser');
	})
	.factory('ResetPassword', function($resource){
        return $resource('api/restPassword');
	})
	.factory('WeixinLogin', function($resource){
        return $resource('api/wx/auth');
	});

