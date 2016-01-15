'use strict';

angular.module('mudskipperApp')
    .factory('Genaccttype', function ($resource, DateUtils) {
        return $resource('api/genaccttypes/:id', {}, {
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
