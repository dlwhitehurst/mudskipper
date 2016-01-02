'use strict';

angular.module('mudskipperApp')
    .factory('RegistrationSearch', function ($resource) {
        return $resource('api/_search/registrations/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
