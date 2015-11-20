/*global define */

'use strict';
var wsCon;
define(function () {


    /* Controllers */

    var controllers = {};

    controllers.home = function ($scope, $rootScope, $cookies) {
        delete $rootScope.currentUser;
        if ($cookies.get("user-name")) {
            $rootScope.currentUser = {name: $cookies.get("username")}
        }
    };
    controllers.home.$inject = ['$scope', '$rootScope', '$cookies'];

    controllers.devList = function ($scope, $cookies, $location, $routeParams, $timeout, DeviceAPI) {
        var username = $routeParams.username;
        var ws = new WebSocket('ws://localhost:9000/ws');
        ws.onopen = function () {
            ws.send('{ "event":"OPEN", "username":"' + username + '"}'); //First call OPEN event to initilize for this client
            ws.send('{ "event":"LOGIN_INIT", "username":"' + username + '"}')
        }
        $scope.success = false;
        $scope.devices = []
        $scope.score = 0;


        ws.onmessage = function (message) { // it listents for 'incoming event'
            console.log('from server: ' + message.data);
            var event = JSON.parse(message.data);
            $scope.event = message.data;
            if (event.event == "UPDATE_CONF_SCORE") {
                $scope.score = event.score;
                changeSmile($scope.score)
                if (event.score > 2000) {
                    $scope.success = true;
                    $timeout(function () {
                        $location.path("/myAccount2")
                    }, 3000)
                }
                $scope.$apply()
            }
        };
        wsCon = ws;

        //smiley face
        var lips = document.getElementById('lips');
        function changeSmile(score) {

            console.log(score)
            score= score/20;
            var lh = lips.style.height, slide = 0;
            if ((50 - score) > 0) {
                slide = (50 - score);
                lips.style.borderTop = "2px black solid";
                lips.style.borderBottom = "none";
            }
            else {
                slide = (score - 50);
                lips.style.borderBottom = "2px black solid";
                lips.style.borderTop = "none";
            }
            lips.style.top = "calc(70% + " + (-slide) * 0.2 + "px";
        }

        //get Device id,type list for user
        DeviceAPI.query({username:username}, function success(devices){
            $scope.devices=devices;
            console.log(devices[0])
        })

    };
    controllers.devList.$inject = ['$scope', '$cookies', '$location', '$routeParams', '$timeout', 'Device'];

    controllers.qrCode = function ($scope, $cookies, Device) {
        var code = "{\"userId\":\"" + $cookies.get('username') + "\"}"
        console.log(code)
        var qrcode = new QRCode(document.getElementById("QRCode"), {
            width: 100,
            height: 100
        });
        qrcode.makeCode(code);
        console.log("Make qr code")
        //user = $scope.user
    }

    controllers.qrCode.$inject = ["$scope", '$cookies', "Device"];

    controllers.login = function ($rootScope, $scope, $routeParams, $http, $window, $route, $location, $cookies, User) {
        $scope.message = $routeParams.message;
        var redirect = $routeParams.redirect;
        redirect = redirect || "/myAccount";
        var action = "api/user/login?redirect=" + redirect;

        $scope.$watch("user.username", function (newValue, oldValue) {
            if ($scope.user.username.length > 3) {
                User.get({username: $scope.user.username}, function (user) {
                    console.log(user);
                    $scope.passiveAuth = user.passiveAuth;
                    $rootScope.currentUser = user;
                    $cookies.put('username', user.username);
                });
            }
        });

        $scope.authenticate = function () {
            if ($scope.passiveAuth) {
                redirect = "/devList/";
                $location.search("username", $scope.user.username)
                $location.path(redirect);
            }
            var user = $scope.user;
            $http.post(action, user).then(
                function success(response) {
                    $location.path(redirect);
                    $route.reload();
                    delete $rootScope.currentUser;
                    $rootScope.currentUser = response.data
                },
                function error(error) {
                    $scope.message = error;
                }
            );
        }

    }
    controllers.login.$inject = ['$rootScope', '$scope', '$routeParams', '$http', '$window', '$route', '$location', '$cookies', 'User'];

    controllers.register = function ($scope, $location, User) {
        $scope.createUser = function () {
            var user = $scope.user;
            User.save(user, function (success) {
                $location.path('mydashboard/' + success.id);
            });
        };
    }
    controllers.register.$inject = ['$scope', '$location', 'User'];

    /**
     * Interceptors for the controllers
     */

    controllers.intercept = function (event, next, current, $rootScope, $location) {


        if (next.loginRequired) {
            if (!$rootScope.currentUser) {
                var msg = null;
                var nextPath = '/#/' + $location.path()
                $location.path('login/').search({'redirect': nextPath, 'message': msg});
            }
        }
    }

    return controllers;

});





















