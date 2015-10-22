/*global define */

'use strict';

define(function () {


    /* Controllers */

    var controllers = {};



    controllers.home = function ($scope, $rootScope, $cookies) {
        delete $rootScope.currentUser;
        if($cookies.get("user-name")){
            $rootScope.currentUser={name:$cookies.get("user-name")}
        }
    };
    controllers.home.$inject = ['$scope','$rootScope', '$cookies' ];


    controllers.login = function ($rootScope, $scope, $routeParams, $http, $window, $route, $location) {
        $scope.message=$routeParams.message;
        var redirect  =$routeParams.redirect;
        redirect= redirect || "/";
        var action="user/login?redirect="+redirect;

        $scope.authenticate=function(){
            var user = $scope.user;
            $http.post(action, user).then(
                function success(response){
                    $location.path(redirect);
                    $route.reload();
                    delete $rootScope.currentUser;
                    $rootScope.currentUser={name:response.data.name}
                },
                function error(error){
                    $scope.message=error;
                }
            );
        }

    }
    controllers.login.$inject = ['$rootScope', '$scope','$routeParams', '$http', '$window','$route', '$location'];

    controllers.register = function ($scope, $location, User) {
        $scope.createUser = function () {
            var user = $scope.user;
            User.save(user, function (success) {
                $location.path('mydashboard/' + success.id);
            });
        };
    }
    controllers.register.$inject = ['$scope','$location','User'];






    /**
     * Interceptors for the controllers
     */

    controllers.intercept= function(event, next, current, $rootScope, $location){


        if(next.loginRequired){
            if(!$rootScope.currentUser ){
                var msg=null;
                var nextPath='/#/'+$location.path()
                $location.path('login/').search({'redirect':nextPath, 'message': msg});
            }
        }







    }

    return controllers;

});





















