
angular.module('temperaturemonitor').controller('NewSensorController', function ($scope, $location, locationParser, SensorResource , SensorTypeResource) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.sensor = $scope.sensor || {};
    
    $scope.sensorTypeList = SensorTypeResource.queryAll(function(items){
        $scope.sensorTypeSelectionList = $.map(items, function(item) {
            return ( {
                value : item.sensorTypeId,
                text : item.description
            });
        });
    });
    $scope.$watch("sensorTypeSelection", function(selection) {
        if ( typeof selection != 'undefined') {
            $scope.sensor.sensorType = {};
            $scope.sensor.sensorType.sensorTypeId = selection.value;
        }
    });
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/Sensors/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        SensorResource.save($scope.sensor, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Sensors");
    };
});