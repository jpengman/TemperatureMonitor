
angular.module('temperaturemonitor').controller('NewSensorTypeController', function ($scope, $location, locationParser, SensorTypeResource ) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.sensorType = $scope.sensorType || {};
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/SensorTypes/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        SensorTypeResource.save($scope.sensorType, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/SensorTypes");
    };
});