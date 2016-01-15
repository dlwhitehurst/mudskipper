'use strict';

angular.module('mudskipperApp')
    .controller('GenacctentryController', function ($scope, $state, Genacctentry, GenacctentrySearch, ParseLinks) {

        $scope.genacctentrys = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Genacctentry.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.genacctentrys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            GenacctentrySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.genacctentrys = result;
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
            $scope.genacctentry = {
                entrytext: null,
                entrydate: null,
                debit: null,
                credit: null,
                id: null
            };
        };
    });
