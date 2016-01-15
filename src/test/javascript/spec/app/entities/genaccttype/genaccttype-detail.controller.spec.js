'use strict';

describe('Controller Tests', function() {

    describe('Genaccttype Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockGenaccttype;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockGenaccttype = jasmine.createSpy('MockGenaccttype');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Genaccttype': MockGenaccttype
            };
            createController = function() {
                $injector.get('$controller')("GenaccttypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mudskipperApp:genaccttypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
