// Services Module
angular.module('pianoTeacherApp')

.service('StudentService', ['$http', '$rootScope', function($http, $rootScope) {
    this.getStudents = function() {
        return $http.get($rootScope.app.apiBaseUrl + '/students');
    };

    this.getStudent = function(id) {
        return $http.get($rootScope.app.apiBaseUrl + '/students/' + id);
    };

    this.createStudent = function(student) {
        return $http.post($rootScope.app.apiBaseUrl + '/students', student);
    };

    this.updateStudent = function(id, student) {
        return $http.put($rootScope.app.apiBaseUrl + '/students/' + id, student);
    };

    this.deleteStudent = function(id) {
        return $http.delete($rootScope.app.apiBaseUrl + '/students/' + id);
    };
}])

.service('ScheduleService', ['$http', '$rootScope', function($http, $rootScope) {
    this.getSchedule = function() {
        return $http.get($rootScope.app.apiBaseUrl + '/schedules');
    };

    this.getTodaySchedule = function() {
        const today = new Date().toISOString().split('T')[0];
        return $http.get($rootScope.app.apiBaseUrl + '/schedules?date=' + today);
    };

    this.createSchedule = function(schedule) {
        return $http.post($rootScope.app.apiBaseUrl + '/schedules', schedule);
    };

    this.updateSchedule = function(id, schedule) {
        return $http.put($rootScope.app.apiBaseUrl + '/schedules/' + id, schedule);
    };

    this.deleteSchedule = function(id) {
        return $http.delete($rootScope.app.apiBaseUrl + '/schedules/' + id);
    };

    this.getSchedulingSuggestions = function(studentId, startDate, endDate, duration) {
        return $http.get($rootScope.app.apiBaseUrl + '/schedules/suggestions', {
            params: {
                studentId: studentId,
                startDate: startDate,
                endDate: endDate,
                duration: duration
            }
        });
    };
}])

.service('LessonService', ['$http', '$rootScope', function($http, $rootScope) {
    this.getLessons = function() {
        return $http.get($rootScope.app.apiBaseUrl + '/lessons');
    };

    this.getRecentLessons = function() {
        return $http.get($rootScope.app.apiBaseUrl + '/lessons?recent=true');
    };

    this.getLesson = function(id) {
        return $http.get($rootScope.app.apiBaseUrl + '/lessons/' + id);
    };

    this.createLesson = function(lesson) {
        return $http.post($rootScope.app.apiBaseUrl + '/lessons', lesson);
    };

    this.updateLesson = function(id, lesson) {
        return $http.put($rootScope.app.apiBaseUrl + '/lessons/' + id, lesson);
    };

    this.deleteLesson = function(id) {
        return $http.delete($rootScope.app.apiBaseUrl + '/lessons/' + id);
    };

    this.getLessonsByStudent = function(studentId) {
        return $http.get($rootScope.app.apiBaseUrl + '/lessons?studentId=' + studentId);
    };
}])

.service('HomeworkService', ['$http', '$rootScope', function($http, $rootScope) {
    this.getHomework = function() {
        return $http.get($rootScope.app.apiBaseUrl + '/homework');
    };

    this.getHomework = function(id) {
        return $http.get($rootScope.app.apiBaseUrl + '/homework/' + id);
    };

    this.createHomework = function(homework) {
        return $http.post($rootScope.app.apiBaseUrl + '/homework', homework);
    };

    this.updateHomework = function(id, homework) {
        return $http.put($rootScope.app.apiBaseUrl + '/homework/' + id, homework);
    };

    this.deleteHomework = function(id) {
        return $http.delete($rootScope.app.apiBaseUrl + '/homework/' + id);
    };

    this.getHomeworkByStudent = function(studentId) {
        return $http.get($rootScope.app.apiBaseUrl + '/homework?studentId=' + studentId);
    };
}]);
