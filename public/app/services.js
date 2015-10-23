/*global define */

'use strict';

define(['angular'], function (angular) {

    /* Services */

// Demonstrate how to register services
// In this case it is a simple value service.
    angular.module('myApp.services', []).
        value('version', '0.21')
        .factory('Device', function ($resource) {
            return $resource('/api/device/:name', {name:'all'});
        })
        .factory('User',function($resource){
            return $resource('/api/user/:email', {email:'@id'});
        });

});