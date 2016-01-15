'use strict';

angular.module('mudskipperApp').controller('GenaccttypeDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Genaccttype',
        function($scope, $stateParams, $uibModalInstance, entity, Genaccttype) {

        $scope.genaccttype = entity;
        $scope.load = function(id) {
            Genaccttype.get({id : id}, function(result) {
                $scope.genaccttype = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mudskipperApp:genaccttypeUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.genaccttype.id != null) {
                Genaccttype.update($scope.genaccttype, onSaveSuccess, onSaveError);
            } else {
                Genaccttype.save($scope.genaccttype, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
