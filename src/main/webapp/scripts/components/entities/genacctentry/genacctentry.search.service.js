'use strict';

angular.module('mudskipperApp')
    .factory('GenacctentrySearch', function ($resource) {
        return $resource('api/_search/genacctentrys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
