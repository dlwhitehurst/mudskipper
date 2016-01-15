'use strict';

angular.module('mudskipperApp')
	.controller('GenacctentryDeleteController', function($scope, $uibModalInstance, entity, Genacctentry) {

        $scope.genacctentry = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Genacctentry.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
