 'use strict';

angular.module('newspringangularApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-newspringangularApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-newspringangularApp-params')});
                }
                return response;
            }
        };
    });
