'use strict';

angular.module('mudskipperApp').controller('RegistrationDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Registration',
        function($scope, $stateParams, $uibModalInstance, entity, Registration) {

        $scope.registration = entity;
        $scope.load = function(id) {
            Registration.get({id : id}, function(result) {
                $scope.registration = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mudskipperApp:registrationUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.registration.id != null) {
                Registration.update($scope.registration, onSaveSuccess, onSaveError);
            } else {
                Registration.save($scope.registration, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForCreated = {};

        $scope.datePickerForCreated.status = {
            opened: false
        };

        $scope.datePickerForCreatedOpen = function($event) {
            $scope.datePickerForCreated.status.opened = true;
        };
        $scope.datePickerForModified = {};

        $scope.datePickerForModified.status = {
            opened: false
        };

        $scope.datePickerForModifiedOpen = function($event) {
            $scope.datePickerForModified.status.opened = true;
        };
}]);
