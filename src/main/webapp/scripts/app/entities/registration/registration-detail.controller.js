'use strict';

angular.module('mudskipperApp')
    .controller('RegistrationDetailController', function ($scope, $rootScope, $stateParams, entity, Registration) {
        $scope.registration = entity;
        $scope.load = function (id) {
            Registration.get({id: id}, function(result) {
                $scope.registration = result;
            });
        };
        var unsubscribe = $rootScope.$on('mudskipperApp:registrationUpdate', function(event, result) {
            $scope.registration = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
