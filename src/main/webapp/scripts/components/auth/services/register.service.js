'use strict';

angular.module('mudskipperApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


