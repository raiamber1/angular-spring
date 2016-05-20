'use strict';

angular.module('newspringangularApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


