'use strict';

angular.module('newspringangularApp')
    .controller('CourseDetailController', function ($scope, $rootScope, $stateParams, entity, Course, Student) {
        $scope.course = entity;
        $scope.load = function (id) {
            Course.get({id: id}, function(result) {
                $scope.course = result;
            });
        };
        var unsubscribe = $rootScope.$on('newspringangularApp:courseUpdate', function(event, result) {
            $scope.course = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
