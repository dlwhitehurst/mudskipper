'use strict';

describe('Controller Tests', function() {

    describe('Genacctentry Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockGenacctentry, MockGenacct;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockGenacctentry = jasmine.createSpy('MockGenacctentry');
            MockGenacct = jasmine.createSpy('MockGenacct');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Genacctentry': MockGenacctentry,
                'Genacct': MockGenacct
            };
            createController = function() {
                $injector.get('$controller')("GenacctentryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mudskipperApp:genacctentryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
