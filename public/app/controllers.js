/*global define */

'use strict';

define(function () {


    /* Controllers */

    var controllers = {};

    controllers.home = function ($scope, $rootScope, $cookies) {
        delete $rootScope.currentUser;
        if($cookies.get("user-name")){
            $rootScope.currentUser={name:$cookies.get("username")}
        }
    };
    controllers.home.$inject = ['$scope','$rootScope', '$cookies' ];

    controllers.devList = function ($scope, $cookies, $location,  $routeParams) {
          var username=$routeParams.username;
          var ws = new WebSocket('ws://localhost:9000/ws');
          ws.onopen =function () {
            ws.send('{ "event":"login_init", "username":"'+username+'"}'); // it sends the event 'hello' with data 'world'
          }
          ws.onmessage=function (message) { // it listents for 'incoming event'
            console.log('something incoming from the server: ' + message);
          };
        };
    controllers.devList.$inject = ['$scope', '$cookies', '$location', '$routeParams' ];

    controllers.qrCode = function ($scope , Device) {
//        require(['qrcode'], function() {
//            //      var currentUser = {"username": "Bob" , "id": 12345}
//            //      var currentUser = $scope.currentUser
//                var QRCode = new QRCode(document.getElementById("QRCode"), {
//                width : 100,
//                height : 100
//               });
//                            QRCode.makeCode("Bob");
//            }
//        );
    console.log("Make qr code")
    }

    controllers.qrCode.$inject = ["$scope", "Device"];

    controllers.login = function ($rootScope, $scope, $routeParams, $http, $window, $route, $location, User) {
        $scope.message=$routeParams.message;
        var redirect  =$routeParams.redirect;
        redirect= redirect || "/myAccount";
        var action="api/user/login?redirect="+redirect;

        $scope.$watch("user.username", function(newValue, oldValue) {
            if ($scope.user.username.length > 3) {
                User.get({username: $scope.user.username}, function (user) {
                    console.log(user);
                    $scope.passiveAuth=user.passiveAuth;
                });
            }
        });

        $scope.authenticate=function(){
            if($scope.passiveAuth){
                redirect="/devList";
                $location.search("username",$scope.user.username)
                $location.path(redirect);
            }
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





















