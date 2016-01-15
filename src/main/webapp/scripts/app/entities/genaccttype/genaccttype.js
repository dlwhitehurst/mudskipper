'use strict';

angular.module('mudskipperApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('genaccttype', {
                parent: 'entity',
                url: '/genaccttypes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'mudskipperApp.genaccttype.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/genaccttype/genaccttypes.html',
                        controller: 'GenaccttypeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('genaccttype');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('genaccttype.detail', {
                parent: 'entity',
                url: '/genaccttype/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'mudskipperApp.genaccttype.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/genaccttype/genaccttype-detail.html',
                        controller: 'GenaccttypeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('genaccttype');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Genaccttype', function($stateParams, Genaccttype) {
                        return Genaccttype.get({id : $stateParams.id});
                    }]
                }
            })
            .state('genaccttype.new', {
                parent: 'genaccttype',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/genaccttype/genaccttype-dialog.html',
                        controller: 'GenaccttypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('genaccttype', null, { reload: true });
                    }, function() {
                        $state.go('genaccttype');
                    })
                }]
            })
            .state('genaccttype.edit', {
                parent: 'genaccttype',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/genaccttype/genaccttype-dialog.html',
                        controller: 'GenaccttypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Genaccttype', function(Genaccttype) {
                                return Genaccttype.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('genaccttype', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('genaccttype.delete', {
                parent: 'genaccttype',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/genaccttype/genaccttype-delete-dialog.html',
                        controller: 'GenaccttypeDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Genaccttype', function(Genaccttype) {
                                return Genaccttype.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('genaccttype', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
