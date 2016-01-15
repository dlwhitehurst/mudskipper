'use strict';

angular.module('mudskipperApp')
    .controller('GenacctentryDetailController', function ($scope, $rootScope, $stateParams, entity, Genacctentry, Genacct) {
        $scope.genacctentry = entity;
        $scope.load = function (id) {
            Genacctentry.get({id: id}, function(result) {
                $scope.genacctentry = result;
            });
        };
        var unsubscribe = $rootScope.$on('mudskipperApp:genacctentryUpdate', function(event, result) {
            $scope.genacctentry = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
