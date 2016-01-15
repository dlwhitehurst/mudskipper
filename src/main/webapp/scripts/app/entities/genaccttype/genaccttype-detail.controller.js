'use strict';

angular.module('mudskipperApp')
    .controller('GenaccttypeDetailController', function ($scope, $rootScope, $stateParams, entity, Genaccttype) {
        $scope.genaccttype = entity;
        $scope.load = function (id) {
            Genaccttype.get({id: id}, function(result) {
                $scope.genaccttype = result;
            });
        };
        var unsubscribe = $rootScope.$on('mudskipperApp:genaccttypeUpdate', function(event, result) {
            $scope.genaccttype = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
