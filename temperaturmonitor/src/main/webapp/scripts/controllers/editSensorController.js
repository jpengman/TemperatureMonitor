

angular.module('temperaturemonitor').controller('EditSensorController', function($scope, $routeParams, $location, SensorResource , SensorTypeResource) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.sensor = new SensorResource(self.original);
            SensorTypeResource.queryAll(function(items) {
                $scope.sensorTypeSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        sensorTypeId : item.sensorTypeId
                    };
                    var labelObject = {
                        value : item.sensorTypeId,
                        text : item.description
                    };
                    if($scope.sensor.sensorType && item.sensorTypeId == $scope.sensor.sensorType.sensorTypeId) {
                        $scope.sensorTypeSelection = labelObject;
                        $scope.sensor.sensorType = wrappedObject;
                        self.original.sensorType = $scope.sensor.sensorType;
                    }
                    return labelObject;
                });
            });
        };
        var errorCallback = function() {
            $location.path("/Sensors");
        };
        SensorResource.get({SensorId:$routeParams.SensorId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.sensor);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.sensor.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/Sensors");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/Sensors");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.sensor.$remove(successCallback, errorCallback);
    };
    
    $scope.$watch("sensorTypeSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.sensor.sensorType = {};
            $scope.sensor.sensorType.sensorTypeId = selection.value;
        }
    });
    
    $scope.get();
});