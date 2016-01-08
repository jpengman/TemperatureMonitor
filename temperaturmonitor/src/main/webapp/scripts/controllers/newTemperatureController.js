
angular.module('temperaturemonitor').controller('NewTemperatureController', function ($scope, $location, locationParser, TemperatureResource ) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.temperature = $scope.temperature || {};
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/Temperatures/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        TemperatureResource.save($scope.temperature, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Temperatures");
    };
});