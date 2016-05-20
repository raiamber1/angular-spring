'use strict';

angular.module('newspringangularApp')
    .controller('StudentController', function ($scope, $state, Student) {

        $scope.students = [];
        $scope.loadAll = function() {
            Student.query(function(result) {
               $scope.students = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.student = {
                studentId: null,
                age: null,
                emailAdd: null,
                phoneNumber: null,
                name: null,
                id: null
            };
        };
    });
