/*global define */

'use strict';

define(['angular'], function (angular) {

    /* Filters */

    angular.module('myApp.filters', []).
        filter('interpolate', ['version', function (version) {
            return function (text) {
                return String(text).replace(/\%VERSION\%/mg, version);
            }
        }])
        .filter('escapeCharEntity', function () {
            return function (str) {
                return $('<div/>').text(str).html();
            }
        })
        .filter('trust', [
            '$sce',
            function ($sce) {
                return function (value, type) {
                    // Defaults to treating trusted text as `html`
                    return $sce.trustAs(type || 'html', value);
                }
            }
        ]);

});