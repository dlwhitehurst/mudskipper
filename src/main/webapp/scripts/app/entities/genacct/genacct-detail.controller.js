'use strict';

angular.module('mudskipperApp')
    .controller('GenacctDetailController', function ($scope, $rootScope, $stateParams, entity, Genacct, Genacctentry) {
        $scope.genacct = entity;
        $scope.load = function (id) {
            Genacct.get({id: id}, function(result) {
                $scope.genacct = result;
            });
        };
        var unsubscribe = $rootScope.$on('mudskipperApp:genacctUpdate', function(event, result) {
            $scope.genacct = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
