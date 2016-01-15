'use strict';

angular.module('mudskipperApp')
    .factory('GenacctSearch', function ($resource) {
        return $resource('api/_search/genaccts/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
