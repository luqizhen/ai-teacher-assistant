// Main AngularJS Application
console.log('Loading AngularJS app...');

// Check if Angular is available
if (typeof angular === 'undefined') {
    console.error('AngularJS is not loaded!');
} else {
    console.log('AngularJS is loaded, version:', angular.version.full);
}

angular.module('pianoTeacherApp', ['ngRoute', 'ngSanitize'])

.config(['$routeProvider', '$httpProvider', '$locationProvider', '$compileProvider', 
    function($routeProvider, $httpProvider, $locationProvider, $compileProvider) {
    console.log('Configuring AngularJS app...');
    
    // Configure HTML5 mode
    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });
    $locationProvider.hashPrefix('!');
    
    // Configure compilation
    $compileProvider.debugInfoEnabled(true);
    
    // Configure routes
    $routeProvider
        .when('/', {
            templateUrl: 'views/dashboard.html',
            controller: 'DashboardController',
            controllerAs: 'vm'
        })
        .when('/students', {
            templateUrl: 'views/students.html',
            controller: 'StudentsController',
            controllerAs: 'vm'
        })
        .when('/schedule', {
            templateUrl: 'views/schedule.html',
            controller: 'ScheduleController',
            controllerAs: 'vm'
        })
        .when('/lessons', {
            templateUrl: 'views/lessons.html',
            controller: 'LessonsController',
            controllerAs: 'vm'
        })
        .when('/homework', {
            templateUrl: 'views/homework.html',
            controller: 'HomeworkController',
            controllerAs: 'vm'
        })
        .otherwise({
            redirectTo: '/'
        });
    
    // Add routing debug
    $rootScope.$on('$routeChangeStart', function(event, next, current) {
        console.log('Route change start:', next);
    });
    
    $rootScope.$on('$routeChangeSuccess', function(event, current, previous) {
        console.log('Route change success:', current);
    });
    
    $rootScope.$on('$routeChangeError', function(event, current, previous, rejection) {
        console.error('Route change error:', rejection);
    });

    // Configure HTTP provider
    $httpProvider.defaults.headers.common['Content-Type'] = 'application/json';
    $httpProvider.interceptors.push(['$q', function($q) {
        return {
            'request': function(config) {
                // Add authentication header to all API requests
                if (config.url && (config.url.includes('/api/') || config.url.startsWith('http'))) {
                    var authHeader = sessionStorage.getItem('authHeader');
                    if (authHeader) {
                        config.headers.Authorization = authHeader;
                        console.log('Adding auth header to:', config.url);
                    } else {
                        console.warn('No auth header available for:', config.url);
                    }
                }
                return config;
            },
            'responseError': function(rejection) {
                console.error('HTTP Error:', rejection);
                if (rejection.status === 401) {
                    console.log('Authentication required for:', rejection.config.url);
                    console.log('Auth header was:', rejection.config.headers.Authorization);
                }
                return $q.reject(rejection);
            }
        };
    }]);
    
    console.log('AngularJS app configured');
}])

.run(['$rootScope', '$location', '$timeout', function($rootScope, $location, $timeout) {
    console.log('AngularJS app is running!');
    
    // Global application configuration
    $rootScope.app = {
        name: 'Piano Teacher Management System',
        version: '1.0.0',
        apiBaseUrl: 'http://localhost:8080/api'
    };

    // Initialize authentication immediately (synchronously)
    function initAuth() {
        console.log('Initializing authentication...');
        try {
            var authString = 'admin:admin123';
            var encodedAuth = window.btoa ? window.btoa(authString) : 'YWRtaW46YWRtaW4xMjM=';
            var authHeader = 'Basic ' + encodedAuth;
            sessionStorage.setItem('authHeader', authHeader);
            sessionStorage.setItem('currentUser', 'admin');
            $rootScope.currentUser = 'admin';
            $rootScope.isAuthenticated = true;
            console.log('Authentication initialized successfully');
            console.log('Auth header:', authHeader);
        } catch (error) {
            console.error('Auth initialization failed:', error);
            // Fallback: manually set auth header
            var authHeader = 'Basic YWRtaW46YWRtaW4xMjM=';
            sessionStorage.setItem('authHeader', authHeader);
            sessionStorage.setItem('currentUser', 'admin');
            $rootScope.currentUser = 'admin';
            $rootScope.isAuthenticated = true;
        }
    }
    
    // Initialize auth immediately
    initAuth();
    
    console.log('Initial auth state:', {
        isAuthenticated: $rootScope.isAuthenticated,
        currentUser: $rootScope.currentUser,
        authHeader: sessionStorage.getItem('authHeader')
    });

    // Language management
    $rootScope.currentLanguage = localStorage.getItem('preferredLanguage') || 'en';
    $rootScope.translations = {
        en: {
            app: {
                title: 'Piano Teacher Management System',
                welcome: 'Welcome to Piano Teacher Management System',
                description: 'Manage your students, schedule lessons, track homework, and monitor progress.'
            },
            nav: {
                dashboard: 'Dashboard',
                students: 'Students',
                schedule: 'Schedule',
                lessons: 'Lessons',
                homework: 'Homework',
                settings: 'Settings',
                logout: 'Logout',
                back: 'Back to Dashboard'
            },
            students: {
                title: 'Student Management',
                name: 'Name',
                age: 'Age',
                grade: 'Grade',
                email: 'Email',
                phone: 'Phone',
                notes: 'Notes',
                add: 'Add Student',
                edit: 'Edit Student',
                delete: 'Delete Student',
                save: 'Save',
                cancel: 'Cancel',
                search: 'Search students...',
                manage: 'Manage Students',
                description: 'Manage student information and pricing'
            },
            schedule: {
                title: 'Schedule Management',
                view: 'View Schedule',
                description: 'View and manage teaching schedule',
                add: 'Add Schedule',
                startTime: 'Start Time',
                endTime: 'End Time',
                recurring: 'Recurring'
            },
            lessons: {
                title: 'Lesson Management',
                manage: 'Manage Lessons',
                description: 'Track lesson history and performance',
                add: 'Add Lesson',
                date: 'Date',
                duration: 'Duration (minutes)',
                notes: 'Notes',
                difficulty: 'Difficulty'
            },
            homework: {
                title: 'Homework Management',
                manage: 'Manage Homework',
                description: 'Assign and track homework assignments',
                add: 'Add Homework',
                assign: 'Assign Homework',
                dueDate: 'Due Date',
                description: 'Description'
            },
            common: {
                loading: 'Loading...',
                error: 'Error',
                success: 'Success',
                save: 'Save',
                cancel: 'Cancel',
                delete: 'Delete',
                edit: 'Edit',
                add: 'Add',
                search: 'Search',
                filter: 'Filter',
                back: 'Back',
                next: 'Next',
                previous: 'Previous',
                actions: 'Actions',
                noResults: 'No results found',
                quickActions: 'Quick Actions'
            }
        },
        zh: {
            app: {
                title: '钢琴教师管理系统',
                welcome: '欢迎使用钢琴教师管理系统',
                description: '管理您的学生、安排课程、跟踪作业和监控进度。'
            },
            nav: {
                dashboard: '仪表板',
                students: '学生',
                schedule: '日程',
                lessons: '课程',
                homework: '作业',
                settings: '设置',
                logout: '退出登录',
                back: '返回仪表板'
            },
            students: {
                title: '学生管理',
                name: '姓名',
                age: '年龄',
                grade: '年级',
                email: '邮箱',
                phone: '电话',
                notes: '备注',
                add: '添加学生',
                edit: '编辑学生',
                delete: '删除学生',
                save: '保存',
                cancel: '取消',
                search: '搜索学生...',
                manage: '管理学生',
                description: '管理学生信息和定价'
            },
            schedule: {
                title: '日程管理',
                view: '查看日程',
                description: '查看和管理教学日程',
                add: '添加日程',
                startTime: '开始时间',
                endTime: '结束时间',
                recurring: '重复'
            },
            lessons: {
                title: '课程管理',
                manage: '管理课程',
                description: '跟踪课程历史和表现',
                add: '添加课程',
                date: '日期',
                duration: '时长（分钟）',
                notes: '备注',
                difficulty: '难度'
            },
            homework: {
                title: '作业管理',
                manage: '管理作业',
                description: '分配和跟踪作业任务',
                add: '添加作业',
                assign: '分配作业',
                dueDate: '截止日期',
                description: '描述'
            },
            common: {
                loading: '加载中...',
                error: '错误',
                success: '成功',
                save: '保存',
                cancel: '取消',
                delete: '删除',
                edit: '编辑',
                add: '添加',
                search: '搜索',
                filter: '筛选',
                back: '返回',
                next: '下一个',
                previous: '上一个',
                actions: '操作',
                noResults: '未找到结果',
                quickActions: '快速操作'
            }
        }
    };

    // Translation function
    $rootScope.t = function(key) {
        const keys = key.split('.');
        let value = $rootScope.translations[$rootScope.currentLanguage];
        
        for (const k of keys) {
            value = value?.[k];
        }
        
        return value || key;
    };

    // Change language
    $rootScope.changeLanguage = function(lang) {
        $rootScope.currentLanguage = lang;
        localStorage.setItem('preferredLanguage', lang);
        $rootScope.$applyAsync();
    };

    // Initialize with a timeout to ensure DOM is ready
    $timeout(function() {
        console.log('AngularJS app initialized successfully');
        console.log('Current language:', $rootScope.currentLanguage);
        console.log('Translation test:', $rootScope.t('app.title'));
    }, 100);
    
    // Authentication functions
    $rootScope.login = function(username, password) {
        try {
            var authString = username + ':' + password;
            var encodedAuth = window.btoa ? window.btoa(authString) : 'YWRtaW46YWRtaW4xMjM=';
            var authHeader = 'Basic ' + encodedAuth;
            sessionStorage.setItem('authHeader', authHeader);
            sessionStorage.setItem('currentUser', username);
            $rootScope.currentUser = username;
            $rootScope.isAuthenticated = true;
            console.log('User logged in:', username);
            console.log('Auth header:', authHeader);
        } catch (error) {
            console.error('Login error:', error);
        }
    };
    
    $rootScope.logout = function() {
        sessionStorage.removeItem('authHeader');
        sessionStorage.removeItem('currentUser');
        $rootScope.currentUser = null;
        $rootScope.isAuthenticated = false;
        console.log('User logged out');
    };
}]);
