angular.module('temperaturemonitor').factory('SensorTypeResource', function($resource){
    var resource = $resource('rest/sensortypes/:SensorTypeId',{SensorTypeId:'@sensorTypeId'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});