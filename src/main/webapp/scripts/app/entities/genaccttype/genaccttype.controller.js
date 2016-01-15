'use strict';

angular.module('mudskipperApp')
    .controller('GenaccttypeController', function ($scope, $state, Genaccttype, GenaccttypeSearch) {

        $scope.genaccttypes = [];
        $scope.loadAll = function() {
            Genaccttype.query(function(result) {
               $scope.genaccttypes = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            GenaccttypeSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.genaccttypes = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.genaccttype = {
                name: null,
                id: null
            };
        };
    });
