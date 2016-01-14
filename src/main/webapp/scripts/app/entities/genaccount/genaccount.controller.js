'use strict';

angular.module('mudskipperApp')
    .controller('GenaccountController', function ($scope, $state, Genaccount, GenaccountSearch, ParseLinks) {

        $scope.genaccounts = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Genaccount.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.genaccounts = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            GenaccountSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.genaccounts = result;
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
            $scope.genaccount = {
                name: null,
                acctno: null,
                id: null
            };
        };
    });
