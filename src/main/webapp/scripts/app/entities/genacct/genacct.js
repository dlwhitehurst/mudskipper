'use strict';

angular.module('mudskipperApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('genacct', {
                parent: 'entity',
                url: '/genaccts',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'mudskipperApp.genacct.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/genacct/genaccts.html',
                        controller: 'GenacctController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('genacct');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('genacct.detail', {
                parent: 'entity',
                url: '/genacct/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'mudskipperApp.genacct.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/genacct/genacct-detail.html',
                        controller: 'GenacctDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('genacct');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Genacct', function($stateParams, Genacct) {
                        return Genacct.get({id : $stateParams.id});
                    }]
                }
            })
            .state('genacct.new', {
                parent: 'genacct',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/genacct/genacct-dialog.html',
                        controller: 'GenacctDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    type: null,
                                    acctno: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('genacct', null, { reload: true });
                    }, function() {
                        $state.go('genacct');
                    })
                }]
            })
            .state('genacct.edit', {
                parent: 'genacct',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/genacct/genacct-dialog.html',
                        controller: 'GenacctDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Genacct', function(Genacct) {
                                return Genacct.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('genacct', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('genacct.delete', {
                parent: 'genacct',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/genacct/genacct-delete-dialog.html',
                        controller: 'GenacctDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Genacct', function(Genacct) {
                                return Genacct.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('genacct', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
