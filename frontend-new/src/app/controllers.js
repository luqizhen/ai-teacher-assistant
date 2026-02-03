// Controllers Module
angular.module('pianoTeacherApp')

.controller('DashboardController', ['$scope', '$rootScope', 'StudentService', 'ScheduleService', 'LessonService', 
    function($scope, $rootScope, StudentService, ScheduleService, LessonService) {
    var vm = this;
    vm.loading = true;
    vm.stats = {
        totalStudents: 0,
        upcomingLessons: 0,
        pendingHomework: 0,
        todaySchedule: 0
    };

    // Load dashboard data
    vm.loadDashboardData = function() {
        vm.loading = true;
        
        // Load students count
        StudentService.getStudents().then(function(response) {
            vm.stats.totalStudents = response.data.length || 0;
        }).catch(function(error) {
            console.error('Error loading students:', error);
        });

        // Load today's schedule
        ScheduleService.getTodaySchedule().then(function(response) {
            vm.stats.todaySchedule = response.data.length || 0;
        }).catch(function(error) {
            console.error('Error loading schedule:', error);
        });

        // Load recent lessons
        LessonService.getRecentLessons().then(function(response) {
            vm.stats.upcomingLessons = response.data.length || 0;
        }).catch(function(error) {
            console.error('Error loading lessons:', error);
        });

        vm.loading = false;
    };

    // Initialize
    vm.loadDashboardData();
}])

.controller('StudentsController', ['$scope', '$rootScope', 'StudentService', 
    function($scope, $rootScope, StudentService) {
    var vm = this;
    vm.loading = true;
    vm.students = [];
    vm.searchTerm = '';
    vm.showAddModal = false;
    vm.editingStudent = null;

    // Load students
    vm.loadStudents = function() {
        vm.loading = true;
        StudentService.getStudents().then(function(response) {
            vm.students = response.data || [];
            vm.loading = false;
        }).catch(function(error) {
            console.error('Error loading students:', error);
            vm.loading = false;
        });
    };

    // Search students
    vm.searchStudents = function() {
        if (!vm.searchTerm) {
            return vm.students;
        }
        return vm.students.filter(student => 
            student.name.toLowerCase().includes(vm.searchTerm.toLowerCase()) ||
            student.email.toLowerCase().includes(vm.searchTerm.toLowerCase())
        );
    };

    // Add student
    vm.addStudent = function() {
        vm.editingStudent = {
            name: '',
            age: '',
            grade: '',
            email: '',
            phone: '',
            notes: ''
        };
        vm.showAddModal = true;
    };

    // Edit student
    vm.editStudent = function(student) {
        vm.editingStudent = angular.copy(student);
        vm.showAddModal = true;
    };

    // Save student
    vm.saveStudent = function() {
        if (!vm.editingStudent.name) return;

        if (vm.editingStudent.id) {
            // Update existing student
            StudentService.updateStudent(vm.editingStudent.id, vm.editingStudent).then(function(response) {
                vm.loadStudents();
                vm.showAddModal = false;
                vm.editingStudent = null;
            }).catch(function(error) {
                console.error('Error updating student:', error);
            });
        } else {
            // Add new student
            StudentService.createStudent(vm.editingStudent).then(function(response) {
                vm.loadStudents();
                vm.showAddModal = false;
                vm.editingStudent = null;
            }).catch(function(error) {
                console.error('Error adding student:', error);
            });
        }
    };

    // Delete student
    vm.deleteStudent = function(student) {
        if (confirm('Are you sure you want to delete ' + student.name + '?')) {
            StudentService.deleteStudent(student.id).then(function(response) {
                vm.loadStudents();
            }).catch(function(error) {
                console.error('Error deleting student:', error);
            });
        }
    };

    // Cancel modal
    vm.cancelModal = function() {
        vm.showAddModal = false;
        vm.editingStudent = null;
    };

    // Initialize
    vm.loadStudents();
}])

.controller('ScheduleController', ['$scope', '$rootScope', 'ScheduleService', 
    function($scope, $rootScope, ScheduleService) {
    var vm = this;
    vm.loading = true;
    vm.schedule = [];
    vm.currentDate = new Date();
    vm.showAddModal = false;
    vm.editingSchedule = null;

    // Load schedule
    vm.loadSchedule = function() {
        vm.loading = true;
        ScheduleService.getSchedule().then(function(response) {
            vm.schedule = response.data || [];
            vm.loading = false;
        }).catch(function(error) {
            console.error('Error loading schedule:', error);
            vm.loading = false;
        });
    };

    // Add schedule
    vm.addSchedule = function() {
        vm.editingSchedule = {
            studentId: '',
            startTime: '',
            endTime: '',
            recurring: false
        };
        vm.showAddModal = true;
    };

    // Save schedule
    vm.saveSchedule = function() {
        if (!vm.editingSchedule.studentId || !vm.editingSchedule.startTime) return;

        if (vm.editingSchedule.id) {
            // Update existing schedule
            ScheduleService.updateSchedule(vm.editingSchedule.id, vm.editingSchedule).then(function(response) {
                vm.loadSchedule();
                vm.showAddModal = false;
                vm.editingSchedule = null;
            }).catch(function(error) {
                console.error('Error updating schedule:', error);
            });
        } else {
            // Add new schedule
            ScheduleService.createSchedule(vm.editingSchedule).then(function(response) {
                vm.loadSchedule();
                vm.showAddModal = false;
                vm.editingSchedule = null;
            }).catch(function(error) {
                console.error('Error adding schedule:', error);
            });
        }
    };

    // Delete schedule
    vm.deleteSchedule = function(schedule) {
        if (confirm('Are you sure you want to delete this schedule?')) {
            ScheduleService.deleteSchedule(schedule.id).then(function(response) {
                vm.loadSchedule();
            }).catch(function(error) {
                console.error('Error deleting schedule:', error);
            });
        }
    };

    // Cancel modal
    vm.cancelModal = function() {
        vm.showAddModal = false;
        vm.editingSchedule = null;
    };

    // Initialize
    vm.loadSchedule();
}])

.controller('LessonsController', ['$scope', '$rootScope', 'LessonService', 
    function($scope, $rootScope, LessonService) {
    var vm = this;
    vm.loading = true;
    vm.lessons = [];
    vm.showAddModal = false;
    vm.editingLesson = null;

    // Load lessons
    vm.loadLessons = function() {
        vm.loading = true;
        LessonService.getLessons().then(function(response) {
            vm.lessons = response.data || [];
            vm.loading = false;
        }).catch(function(error) {
            console.error('Error loading lessons:', error);
            vm.loading = false;
        });
    };

    // Add lesson
    vm.addLesson = function() {
        vm.editingLesson = {
            studentId: '',
            date: new Date(),
            duration: 60,
            notes: '',
            difficulty: 'beginner'
        };
        vm.showAddModal = true;
    };

    // Save lesson
    vm.saveLesson = function() {
        if (!vm.editingLesson.studentId || !vm.editingLesson.date) return;

        if (vm.editingLesson.id) {
            // Update existing lesson
            LessonService.updateLesson(vm.editingLesson.id, vm.editingLesson).then(function(response) {
                vm.loadLessons();
                vm.showAddModal = false;
                vm.editingLesson = null;
            }).catch(function(error) {
                console.error('Error updating lesson:', error);
            });
        } else {
            // Add new lesson
            LessonService.createLesson(vm.editingLesson).then(function(response) {
                vm.loadLessons();
                vm.showAddModal = false;
                vm.editingLesson = null;
            }).catch(function(error) {
                console.error('Error adding lesson:', error);
            });
        }
    };

    // Delete lesson
    vm.deleteLesson = function(lesson) {
        if (confirm('Are you sure you want to delete this lesson?')) {
            LessonService.deleteLesson(lesson.id).then(function(response) {
                vm.loadLessons();
            }).catch(function(error) {
                console.error('Error deleting lesson:', error);
            });
        }
    };

    // Cancel modal
    vm.cancelModal = function() {
        vm.showAddModal = false;
        vm.editingLesson = null;
    };

    // Initialize
    vm.loadLessons();
}])

.controller('HomeworkController', ['$scope', '$rootScope', 'HomeworkService', 
    function($scope, $rootScope, HomeworkService) {
    var vm = this;
    vm.loading = true;
    vm.homework = [];
    vm.showAddModal = false;
    vm.editingHomework = null;

    // Load homework
    vm.loadHomework = function() {
        vm.loading = true;
        HomeworkService.getHomework().then(function(response) {
            vm.homework = response.data || [];
            vm.loading = false;
        }).catch(function(error) {
            console.error('Error loading homework:', error);
            vm.loading = false;
        });
    };

    // Add homework
    vm.addHomework = function() {
        vm.editingHomework = {
            studentId: '',
            title: '',
            description: '',
            dueDate: new Date(),
            status: 'pending'
        };
        vm.showAddModal = true;
    };

    // Save homework
    vm.saveHomework = function() {
        if (!vm.editingHomework.studentId || !vm.editingHomework.title) return;

        if (vm.editingHomework.id) {
            // Update existing homework
            HomeworkService.updateHomework(vm.editingHomework.id, vm.editingHomework).then(function(response) {
                vm.loadHomework();
                vm.showAddModal = false;
                vm.editingHomework = null;
            }).catch(function(error) {
                console.error('Error updating homework:', error);
            });
        } else {
            // Add new homework
            HomeworkService.createHomework(vm.editingHomework).then(function(response) {
                vm.loadHomework();
                vm.showAddModal = false;
                vm.editingHomework = null;
            }).catch(function(error) {
                console.error('Error adding homework:', error);
            });
        }
    };

    // Delete homework
    vm.deleteHomework = function(homework) {
        if (confirm('Are you sure you want to delete this homework?')) {
            HomeworkService.deleteHomework(homework.id).then(function(response) {
                vm.loadHomework();
            }).catch(function(error) {
                console.error('Error deleting homework:', error);
            });
        }
    };

    // Cancel modal
    vm.cancelModal = function() {
        vm.showAddModal = false;
        vm.editingHomework = null;
    };

    // Initialize
    vm.loadHomework();
}]);
