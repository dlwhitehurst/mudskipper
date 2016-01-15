'use strict';

angular.module('mudskipperApp')
    .factory('GenaccttypeSearch', function ($resource) {
        return $resource('api/_search/genaccttypes/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
