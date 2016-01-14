'use strict';

angular.module('mudskipperApp').controller('GenaccountDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Genaccount',
        function($scope, $stateParams, $uibModalInstance, entity, Genaccount) {

        $scope.genaccount = entity;
        $scope.load = function(id) {
            Genaccount.get({id : id}, function(result) {
                $scope.genaccount = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mudskipperApp:genaccountUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.genaccount.id != null) {
                Genaccount.update($scope.genaccount, onSaveSuccess, onSaveError);
            } else {
                Genaccount.save($scope.genaccount, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
