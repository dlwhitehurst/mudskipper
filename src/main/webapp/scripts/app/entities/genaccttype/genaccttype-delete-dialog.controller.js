'use strict';

angular.module('mudskipperApp')
	.controller('GenaccttypeDeleteController', function($scope, $uibModalInstance, entity, Genaccttype) {

        $scope.genaccttype = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Genaccttype.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
