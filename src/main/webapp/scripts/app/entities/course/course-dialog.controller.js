'use strict';

angular.module('newspringangularApp').controller('CourseDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Course', 'Student',
        function($scope, $stateParams, $uibModalInstance, entity, Course, Student) {

        $scope.course = entity;
        $scope.students = Student.query();
        $scope.load = function(id) {
            Course.get({id : id}, function(result) {
                $scope.course = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('newspringangularApp:courseUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.course.id != null) {
                Course.update($scope.course, onSaveSuccess, onSaveError);
            } else {
                Course.save($scope.course, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
