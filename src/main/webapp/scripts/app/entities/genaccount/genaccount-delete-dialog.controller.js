'use strict';

angular.module('mudskipperApp')
	.controller('GenaccountDeleteController', function($scope, $uibModalInstance, entity, Genaccount) {

        $scope.genaccount = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Genaccount.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
