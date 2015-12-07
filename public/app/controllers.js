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
        var ws = new WebSocket('ws://citimesh.herokuapp.com/ws');
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
                $scope.success = event.authSuccess;
                $scope.score = event.score;
                $scope.scoreItems = event.subScores;
                updateScoreList($scope);
                changeSmile2($scope.score);
                if (event.authSuccess == true) {
                    $timeout(function () {
                        $location.path("/myAccount2")
                    }, 500)
                }
                $scope.$apply()
            }
        };
        wsCon = ws;

        $('#smile_img').click(function (event) {
            wsCon.send('{ "event":"LOGIN_INIT", "username":"' + username + '"}')
        })

        function calcDistance(distance) {
            var dist = Math.max(0, (distance - 2) * 70)
            return dist;
        }

        function calcColor(distance) {
            var r, g, b;
            if (distance == 1) {
                r = 24;
                g = 188;
                b = 156;
            } else if ((distance > 1) && (distance <= 2)) {
                r = 255;
                g = 255
                b = 0;
            } else if (distance <= 3) {
                r = 198;
                g = 31;
                b = 31;
            }

            return "rgb(" + r + "," + g + "," + b + ")";
        }

        function changeBeacon(distance) {
            var beacon = document.getElementById("beacon");
            var person = document.getElementById("person");
            beacon.style.paddingLeft = calcDistance(distance) + 'px';
//            console.log(beacon.style.paddingLeft);
            beacon.style.color = calcColor(distance);
            person.style.color = calcColor(distance);

        }

        function changeSmile2(score) {
            if (score <= 900) {
                var dv = document.getElementById('smile_img');
                dv.src = "./img/uhoh.png";
            } else if ((score > 900) && (score <= 1000)) {
                var dv = document.getElementById('smile_img');
                dv.src = "./img/neutral.png";
            } else if ((score > 1000) && (score < 1400)) {
                var dv = document.getElementById('smile_img');
                dv.src = "./img/ok.png";
            } else if (score >= 1400) {
                var dv = document.getElementById('smile_img');
                dv.src = "./img/cool-smiley.png";
            }
        }

        function updateScoreList($scope) {
            var scoreItems = $scope.scoreItems;
            for (var i in scoreItems) {
                var item = scoreItems[i];
                $('#' + item.key).addClass('active').removeClass('inactive')
                if (item.scoreType == 'PROX') {
                    changeBeacon(item.proximity);
                }

            }
        }

        //get Device id,type list for user
        DeviceAPI.query({username: username, scorecriteria: true}, function success(devices) {
            $scope.devices = devices;
            for (var i in devices) {
                var device = devices[i];
                $('[id^='+device.type+']').each(function(i, element){
                    element.id=element.id.replace(device.type, device.deviceId)
                })
            }
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





















