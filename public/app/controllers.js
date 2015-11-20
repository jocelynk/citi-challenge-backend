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
        var ws = new WebSocket('ws://10.144.25.13:9000/ws');
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
            changeSmile2($scope.score);
//          TODO get distance
            changeBeacon($scope.score);
            if (event.event == "UPDATE_CONF_SCORE") {
                $scope.scope = event.score;
                changeSmile2($scope.score)
                //          TODO get distance
//                changeBeacon($scope.score);
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

        function calcDistance(distance){
            switch (distance) {
                case 0:
                    return 30;
                    break;
                case 1:
                    return 0;
                    break;
                case 2:
                    return 10;
                    break;
                case 3:
                    return 20;
                    break;
            }
        }

        function calcColor(distance){
//          assumes distance is a value between 0 and 30
            var max = 30;
//            a value between 0 and 10
            var norm_d = (distance/max);
            var r, g, b;
            if(distance <= 10){
                r = 24 + (15.6*norm_d);
                g = 188;
                b = 156 - (15.6*norm_d);
            } else if( (distance>10) && (distance<=20) ){
                r = 180 + (6.7*norm_d);
                g = 188 + (6.7*norm_d);
                b = 0;
            } else if( distance<=30 ){
                r = 247;
                g = 255 - (25.5*norm_d) ;
                b = 0;
            }

            return "rgb("+r+","+g+","+ b + ")";
        }

        function changeBeacon(distance){
            var beacon = document.getElementById("beacon");
            var person = document.getElementById("person");
            beacon.style.paddingLeft = calcDistance(distance)+'px';
//            console.log(beacon.style.paddingLeft);
            beacon.style.color = calcColor(calcDistance(distance));

        }

        function changeSmile2(score) {
            if (score <= 900) {
                var dv = document.getElementById('smile_img');
                dv.src =  "./img/uhoh.png";
            }else if (  (score > 900) && (score <= 1150) ){
                var dv = document.getElementById('smile_img');
                dv.src =  "./img/neutral.png";
            }else if (  (score > 1150) && (score <1500) ){
                var dv = document.getElementById('smile_img');
                dv.src =  "./img/ok.png";
            }else if( score >= 1500){
                var dv = document.getElementById('smile_img');
                dv.src =  "./img/cool-smiley.png";
            }
        }

        //get Device id,type list for user
//        DeviceAPI.query({username:username}, function success(devices){
//            $scope.devices=devices;
//            console.log(devices[0])
//        })

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





















