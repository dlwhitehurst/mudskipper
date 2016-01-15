'use strict';

angular.module('mudskipperApp')
    .factory('Genacct', function ($resource, DateUtils) {
        return $resource('api/genaccts/:id', {}, {
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
