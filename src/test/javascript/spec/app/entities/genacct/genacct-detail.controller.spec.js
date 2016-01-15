'use strict';

describe('Controller Tests', function() {

    describe('Genacct Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockGenacct, MockGenacctentry;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockGenacct = jasmine.createSpy('MockGenacct');
            MockGenacctentry = jasmine.createSpy('MockGenacctentry');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Genacct': MockGenacct,
                'Genacctentry': MockGenacctentry
            };
            createController = function() {
                $injector.get('$controller')("GenacctDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mudskipperApp:genacctUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
