'use strict';

angular.module('mudskipperApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('genaccount', {
                parent: 'entity',
                url: '/genaccounts',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'mudskipperApp.genaccount.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/genaccount/genaccounts.html',
                        controller: 'GenaccountController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('genaccount');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('genaccount.detail', {
                parent: 'entity',
                url: '/genaccount/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'mudskipperApp.genaccount.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/genaccount/genaccount-detail.html',
                        controller: 'GenaccountDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('genaccount');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Genaccount', function($stateParams, Genaccount) {
                        return Genaccount.get({id : $stateParams.id});
                    }]
                }
            })
            .state('genaccount.new', {
                parent: 'genaccount',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/genaccount/genaccount-dialog.html',
                        controller: 'GenaccountDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    acctno: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('genaccount', null, { reload: true });
                    }, function() {
                        $state.go('genaccount');
                    })
                }]
            })
            .state('genaccount.edit', {
                parent: 'genaccount',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/genaccount/genaccount-dialog.html',
                        controller: 'GenaccountDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Genaccount', function(Genaccount) {
                                return Genaccount.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('genaccount', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('genaccount.delete', {
                parent: 'genaccount',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/genaccount/genaccount-delete-dialog.html',
                        controller: 'GenaccountDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Genaccount', function(Genaccount) {
                                return Genaccount.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('genaccount', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
