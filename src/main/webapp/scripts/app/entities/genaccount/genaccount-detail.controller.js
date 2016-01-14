'use strict';

angular.module('mudskipperApp')
    .controller('GenaccountDetailController', function ($scope, $rootScope, $stateParams, entity, Genaccount) {
        $scope.genaccount = entity;
        $scope.load = function (id) {
            Genaccount.get({id: id}, function(result) {
                $scope.genaccount = result;
            });
        };
        var unsubscribe = $rootScope.$on('mudskipperApp:genaccountUpdate', function(event, result) {
            $scope.genaccount = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
