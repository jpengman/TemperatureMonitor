

angular.module('temperaturemonitor').controller('EditTemperatureController', function($scope, $routeParams, $location, TemperatureResource ) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.temperature = new TemperatureResource(self.original);
        };
        var errorCallback = function() {
            $location.path("/Temperatures");
        };
        TemperatureResource.get({TemperatureId:$routeParams.TemperatureId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.temperature);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.temperature.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/Temperatures");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/Temperatures");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.temperature.$remove(successCallback, errorCallback);
    };
    
    
    $scope.get();
});