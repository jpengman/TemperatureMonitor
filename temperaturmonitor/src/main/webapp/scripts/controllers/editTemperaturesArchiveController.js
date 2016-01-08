

angular.module('temperaturemonitor').controller('EditTemperaturesArchiveController', function($scope, $routeParams, $location, TemperaturesArchiveResource ) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.temperaturesArchive = new TemperaturesArchiveResource(self.original);
        };
        var errorCallback = function() {
            $location.path("/TemperaturesArchives");
        };
        TemperaturesArchiveResource.get({TemperaturesArchiveId:$routeParams.TemperaturesArchiveId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.temperaturesArchive);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.temperaturesArchive.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/TemperaturesArchives");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/TemperaturesArchives");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.temperaturesArchive.$remove(successCallback, errorCallback);
    };
    
    
    $scope.get();
});