'use strict';

angular.module('mudskipperApp')
	.controller('RegistrationDeleteController', function($scope, $uibModalInstance, entity, Registration) {

        $scope.registration = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Registration.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
