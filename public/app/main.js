/*global require, requirejs */

'use strict';

requirejs.config({
    paths: {
        'angular': ['../lib/angularjs/angular'],
        'angular-route': ['../lib/angularjs/angular-route'],
        'angular-resource': ['../lib/angularjs/angular-resource'],
        'angular-cookies': ['../lib/angularjs/angular-cookies'],
        'jquery': ['../lib/jquery/jquery'],
        'qrcode': ['../lib/qrcode']
    },
    shim: {
        'angular': {
            deps: ['qrcode'],
            exports: 'angular'
        },
        'angular-route': {
            deps: ['angular'],
            exports: 'angular'
        },'angular-resource': {
            deps: ['angular'],
            exports: 'angular'
        },'angular-cookies': {
            deps: ['angular'],
            exports: 'angular'
        },
        'qrcode': {
            deps: ['jquery'],
            exports: 'qrcode'
        }
    }
});

require(['angular', './controllers', './directives', './filters', './services', 'angular-route', 'angular-resource','angular-cookies', 'jquery', 'qrcode'],
    function (angular, controllers) {

        // Declare app level module which depends on filters, and services

        angular.module('myApp', ['myApp.filters', 'myApp.services', 'myApp.directives', 'ngRoute', 'ngResource','ngCookies']).
            config(['$routeProvider', function ($routeProvider) {
                $routeProvider.when('/login', {templateUrl: 'partials/login.html', controller: controllers.home});
                $routeProvider.when('/register', {templateUrl: 'partials/register.html', controller: controllers.home});
                $routeProvider.when('/myAccount', {templateUrl: 'partials/account.html', controller: controllers.home});
                $routeProvider.when('/devRegQR', {templateUrl: 'partials/devRegQR.html', controller: controllers.qrCode});
                $routeProvider.when('/devList', {templateUrl: 'partials/devList.html', controller: controllers.home});
                $routeProvider.otherwise({redirectTo: '/login'});
            }]).
            run(function($rootScope, $location) {
                $rootScope.$on( "$routeChangeStart", function(event, next, current) {
                    controllers.intercept(event,next,current, $rootScope, $location);
                });
            }).controller("HomeController", controllers.home);

        angular.bootstrap(document, ['myApp']);

    });
