

angular.module('temperaturemonitor').controller('EditSensorTypeController', function($scope, $routeParams, $location, SensorTypeResource ) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.sensorType = new SensorTypeResource(self.original);
        };
        var errorCallback = function() {
            $location.path("/SensorTypes");
        };
        SensorTypeResource.get({SensorTypeId:$routeParams.SensorTypeId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.sensorType);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.sensorType.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/SensorTypes");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/SensorTypes");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.sensorType.$remove(successCallback, errorCallback);
    };
    
    
    $scope.get();
});