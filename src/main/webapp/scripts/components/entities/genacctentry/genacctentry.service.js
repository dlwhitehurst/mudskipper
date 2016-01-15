'use strict';

angular.module('mudskipperApp')
    .factory('Genacctentry', function ($resource, DateUtils) {
        return $resource('api/genacctentrys/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.entrydate = DateUtils.convertLocaleDateFromServer(data.entrydate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.entrydate = DateUtils.convertLocaleDateToServer(data.entrydate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.entrydate = DateUtils.convertLocaleDateToServer(data.entrydate);
                    return angular.toJson(data);
                }
            }
        });
    });
