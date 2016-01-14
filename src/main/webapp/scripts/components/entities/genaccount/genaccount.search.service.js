'use strict';

angular.module('mudskipperApp')
    .factory('GenaccountSearch', function ($resource) {
        return $resource('api/_search/genaccounts/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
