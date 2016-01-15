'use strict';

angular.module('mudskipperApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('genacctentry', {
                parent: 'entity',
                url: '/genacctentrys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'mudskipperApp.genacctentry.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/genacctentry/genacctentrys.html',
                        controller: 'GenacctentryController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('genacctentry');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('genacctentry.detail', {
                parent: 'entity',
                url: '/genacctentry/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'mudskipperApp.genacctentry.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/genacctentry/genacctentry-detail.html',
                        controller: 'GenacctentryDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('genacctentry');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Genacctentry', function($stateParams, Genacctentry) {
                        return Genacctentry.get({id : $stateParams.id});
                    }]
                }
            })
            .state('genacctentry.new', {
                parent: 'genacctentry',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/genacctentry/genacctentry-dialog.html',
                        controller: 'GenacctentryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    entrytext: null,
                                    entrydate: null,
                                    debit: null,
                                    credit: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('genacctentry', null, { reload: true });
                    }, function() {
                        $state.go('genacctentry');
                    })
                }]
            })
            .state('genacctentry.edit', {
                parent: 'genacctentry',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/genacctentry/genacctentry-dialog.html',
                        controller: 'GenacctentryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Genacctentry', function(Genacctentry) {
                                return Genacctentry.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('genacctentry', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('genacctentry.delete', {
                parent: 'genacctentry',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/genacctentry/genacctentry-delete-dialog.html',
                        controller: 'GenacctentryDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Genacctentry', function(Genacctentry) {
                                return Genacctentry.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('genacctentry', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
