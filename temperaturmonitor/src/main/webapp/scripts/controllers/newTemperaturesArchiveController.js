
angular.module('temperaturemonitor').controller('NewTemperaturesArchiveController', function ($scope, $location, locationParser, TemperaturesArchiveResource ) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.temperaturesArchive = $scope.temperaturesArchive || {};
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/TemperaturesArchives/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        TemperaturesArchiveResource.save($scope.temperaturesArchive, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/TemperaturesArchives");
    };
});