'use strict';

angular.module('mudskipperApp').controller('GenacctDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Genacct', 'Genacctentry',
        function($scope, $stateParams, $uibModalInstance, entity, Genacct, Genacctentry) {

        $scope.genacct = entity;
        $scope.genacctentrys = Genacctentry.query();
        $scope.load = function(id) {
            Genacct.get({id : id}, function(result) {
                $scope.genacct = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mudskipperApp:genacctUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.genacct.id != null) {
                Genacct.update($scope.genacct, onSaveSuccess, onSaveError);
            } else {
                Genacct.save($scope.genacct, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
