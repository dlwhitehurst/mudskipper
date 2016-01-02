 'use strict';

angular.module('mudskipperApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-mudskipperApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-mudskipperApp-params')});
                }
                return response;
            }
        };
    });
