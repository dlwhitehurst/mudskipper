'use strict';

angular.module('mudskipperApp')
	.controller('GenacctDeleteController', function($scope, $uibModalInstance, entity, Genacct) {

        $scope.genacct = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Genacct.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
