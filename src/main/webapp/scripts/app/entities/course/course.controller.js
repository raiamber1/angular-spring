'use strict';

angular.module('newspringangularApp')
    .controller('CourseController', function ($scope, $state, Course) {

        $scope.courses = [];
        $scope.loadAll = function() {
            Course.query(function(result) {
               $scope.courses = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.course = {
                courseId: null,
                cost: null,
                courseName: null,
                internet: null,
                id: null
            };
        };
    });
