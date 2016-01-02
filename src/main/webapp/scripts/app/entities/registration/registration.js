'use strict';

angular.module('mudskipperApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('registration', {
                parent: 'entity',
                url: '/registrations',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'mudskipperApp.registration.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/registration/registrations.html',
                        controller: 'RegistrationController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('registration');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('registration.detail', {
                parent: 'entity',
                url: '/registration/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'mudskipperApp.registration.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/registration/registration-detail.html',
                        controller: 'RegistrationDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('registration');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Registration', function($stateParams, Registration) {
                        return Registration.get({id : $stateParams.id});
                    }]
                }
            })
            .state('registration.new', {
                parent: 'registration',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/registration/registration-dialog.html',
                        controller: 'RegistrationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    entity: null,
                                    username: null,
                                    password: null,
                                    email: null,
                                    created: null,
                                    modified: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('registration', null, { reload: true });
                    }, function() {
                        $state.go('registration');
                    })
                }]
            })
            .state('registration.edit', {
                parent: 'registration',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/registration/registration-dialog.html',
                        controller: 'RegistrationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Registration', function(Registration) {
                                return Registration.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('registration', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('registration.delete', {
                parent: 'registration',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/registration/registration-delete-dialog.html',
                        controller: 'RegistrationDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Registration', function(Registration) {
                                return Registration.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('registration', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
