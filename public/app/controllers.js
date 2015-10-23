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

    controllers.devList = function ($scope, $cookies,$websocket ) {
          var ws = $websocket.$new('ws://localhost:9000/ws');
          ws.$on('$open', function () {
            ws.$emit('hello', 'world'); // it sends the event 'hello' with data 'world'
          })
          .$on('incoming event', function (message) { // it listents for 'incoming event'
            console.log('something incoming from the server: ' + message);
          });
        };
    controllers.devList.$inject = ['$scope', '$cookies' ];

    controllers.qrCode = function ($scope , Device) {
//        require(['qrcode'], function() {
//            //      var currentUser = {"userName": "Bob" , "id": 12345}
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





















