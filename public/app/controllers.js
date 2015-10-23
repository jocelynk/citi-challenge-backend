/*global define */

'use strict';

define(function () {


    /* Controllers */

    var controllers = {};



    controllers.home = function ($scope, $rootScope, $cookies) {
        delete $rootScope.currentUser;
        if($cookies.get("user-name")){
            $rootScope.currentUser={name:$cookies.get("userName")}
        }
    };
    controllers.home.$inject = ['$scope','$rootScope', '$cookies' ];


    controllers.login = function ($rootScope, $scope, $routeParams, $http, $window, $route, $location) {
        $scope.message=$routeParams.message;
        var redirect  =$routeParams.redirect;
        redirect= redirect || "/myAccount";
        var action="api/user/login?redirect="+redirect;

        $scope.$watch("user.userName", function(newValue, oldValue) {
            if ($scope.userName.length > 3) {
                var user=User.get({userName: $scope.user.userName});
            }
        });

        $scope.authenticate=function(){
            var user = $scope.user;
            $http.post(action, user).then(
                function success(response){
                    $location.path(redirect);
                    $route.reload();
                    delete $rootScope.currentUser;
                    $rootScope.currentUser=response.data
                },
                function error(error){
                    $scope.message=error;
                }
            );
        }

    }
    controllers.login.$inject = ['$rootScope', '$scope','$routeParams', '$http', '$window','$route', '$location', 'User'];

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





















