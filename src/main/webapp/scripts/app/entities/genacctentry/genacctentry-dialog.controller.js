'use strict';

angular.module('mudskipperApp').controller('GenacctentryDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Genacctentry', 'Genacct',
        function($scope, $stateParams, $uibModalInstance, entity, Genacctentry, Genacct) {

        $scope.genacctentry = entity;
        $scope.genaccts = Genacct.query();
        $scope.load = function(id) {
            Genacctentry.get({id : id}, function(result) {
                $scope.genacctentry = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mudskipperApp:genacctentryUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.genacctentry.id != null) {
                Genacctentry.update($scope.genacctentry, onSaveSuccess, onSaveError);
            } else {
                Genacctentry.save($scope.genacctentry, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForEntrydate = {};

        $scope.datePickerForEntrydate.status = {
            opened: false
        };

        $scope.datePickerForEntrydateOpen = function($event) {
            $scope.datePickerForEntrydate.status.opened = true;
        };
}]);
