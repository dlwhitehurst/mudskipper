'use strict';

angular.module('mudskipperApp')
    .factory('Registration', function ($resource, DateUtils) {
        return $resource('api/registrations/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.created = DateUtils.convertLocaleDateFromServer(data.created);
                    data.modified = DateUtils.convertLocaleDateFromServer(data.modified);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.created = DateUtils.convertLocaleDateToServer(data.created);
                    data.modified = DateUtils.convertLocaleDateToServer(data.modified);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.created = DateUtils.convertLocaleDateToServer(data.created);
                    data.modified = DateUtils.convertLocaleDateToServer(data.modified);
                    return angular.toJson(data);
                }
            }
        });
    });
