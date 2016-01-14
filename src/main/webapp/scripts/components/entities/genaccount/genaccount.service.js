'use strict';

angular.module('mudskipperApp')
    .factory('Genaccount', function ($resource, DateUtils) {
        return $resource('api/genaccounts/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
