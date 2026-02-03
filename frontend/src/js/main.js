// Main JavaScript file for Piano Teacher Management System

// Global configuration
window.APP_CONFIG = {
    API_BASE_URL: 'http://localhost:8080/api',
    DEFAULT_LANGUAGE: 'en',
    SUPPORTED_LANGUAGES: ['en', 'zh']
};

// Language management
class LanguageManager {
    constructor() {
        this.currentLanguage = localStorage.getItem('preferredLanguage') || window.APP_CONFIG.DEFAULT_LANGUAGE;
        this.messages = {};
        // Load messages synchronously for the current language
        this.loadMessages(this.currentLanguage);
        console.log('Language manager initialized with language:', this.currentLanguage);
    }

    async loadMessages(language) {
        // Always use fallback messages - no API calls
        this.messages[language] = this.getFallbackMessages(language);
        console.log(`Loaded fallback messages for language: ${language}`);
    }

    getFallbackMessages(language) {
        const messages = {
            en: {
                'app.title': 'Piano Teacher Management System',
                'app.welcome': 'Welcome to Piano Teacher Management System',
                'app.description': 'Manage your students, schedule lessons, track homework, and monitor progress.',
                'student.title': 'Student Management',
                'student.name': 'Name',
                'student.age': 'Age',
                'student.grade': 'Grade',
                'student.email': 'Email',
                'student.phone': 'Phone',
                'student.notes': 'Notes',
                'student.add': 'Add Student',
                'student.edit': 'Edit Student',
                'student.delete': 'Delete Student',
                'student.save': 'Save Student',
                'student.cancel': 'Cancel',
                'schedule.title': 'Schedule Management',
                'schedule.availability': 'Availability',
                'schedule.startTime': 'Start Time',
                'schedule.endTime': 'End Time',
                'schedule.recurring': 'Recurring',
                'schedule.addLesson': 'Add Lesson',
                'schedule.conflict': 'Time Conflict',
                'common.save': 'Save',
                'common.cancel': 'Cancel',
                'common.delete': 'Delete',
                'common.edit': 'Edit',
                'common.add': 'Add',
                'common.search': 'Search',
                'common.filter': 'Filter',
                'common.loading': 'Loading...',
                'common.error': 'Error',
                'common.success': 'Success',
                'common.confirm': 'Confirm',
                'nav.dashboard': 'Dashboard',
                'nav.students': 'Students',
                'nav.schedule': 'Schedule',
                'nav.lessons': 'Lessons',
                'nav.homework': 'Homework',
                'nav.settings': 'Settings',
                'nav.logout': 'Logout',
                'nav.back': 'Back to Dashboard',
                'student.list': 'Student List',
                'student.manage': 'Manage Students',
                'student.description': 'Manage student information and pricing',
                'student.search': 'Search students...',
                'schedule.view': 'View Schedule',
                'schedule.description': 'View and manage teaching schedule',
                'schedule.add': 'Add Schedule',
                'schedule.suggestions': 'Scheduling Suggestions',
                'schedule.suggestions.get': 'Get Suggestions',
                'lesson.manage': 'Manage Lessons',
                'lesson.add': 'Add Lesson',
                'lesson.list': 'Lesson History',
                'lesson.date': 'Date',
                'lesson.duration': 'Duration (minutes)',
                'lesson.notes': 'Notes',
                'lesson.difficulty': 'Difficulty',
                'homework.manage': 'Manage Homework',
                'homework.add': 'Add Homework',
                'homework.list': 'Homework Assignments',
                'homework.title': 'Title',
                'homework.description': 'Description',
                'homework.dueDate': 'Due Date',
                'homework.difficulty': 'Difficulty',
                'homework.toggle': 'Toggle Status',
                'btn.save': 'Save',
                'btn.cancel': 'Cancel',
                'btn.edit': 'Edit',
                'btn.delete': 'Delete',
                'btn.add': 'Add',
                'calendar.sun': 'Sun',
                'calendar.mon': 'Mon',
                'calendar.tue': 'Tue',
                'calendar.wed': 'Wed',
                'calendar.thu': 'Thu',
                'calendar.fri': 'Fri',
                'calendar.sat': 'Sat',
                'calendar.previous': 'Previous',
                'calendar.next': 'Next'
            },
            zh: {
                'app.title': '钢琴教师管理系统',
                'app.welcome': '欢迎使用钢琴教师管理系统',
                'app.description': '管理您的学生、安排课程、跟踪作业和监控进度。',
                'student.title': '学生管理',
                'student.name': '姓名',
                'student.age': '年龄',
                'student.grade': '年级',
                'student.email': '邮箱',
                'student.phone': '电话',
                'student.notes': '备注',
                'student.add': '添加学生',
                'student.edit': '编辑学生',
                'student.delete': '删除学生',
                'student.save': '保存学生',
                'student.cancel': '取消',
                'schedule.title': '日程管理',
                'schedule.availability': '可用时间',
                'schedule.startTime': '开始时间',
                'schedule.endTime': '结束时间',
                'schedule.recurring': '重复',
                'schedule.addLesson': '添加课程',
                'schedule.conflict': '时间冲突',
                'common.save': '保存',
                'common.cancel': '取消',
                'common.delete': '删除',
                'common.edit': '编辑',
                'common.add': '添加',
                'common.search': '搜索',
                'common.filter': '筛选',
                'common.loading': '加载中...',
                'common.error': '错误',
                'common.success': '成功',
                'common.confirm': '确认',
                'nav.dashboard': '仪表板',
                'nav.students': '学生',
                'nav.schedule': '日程',
                'nav.lessons': '课程',
                'nav.homework': '作业',
                'nav.settings': '设置',
                'nav.logout': '退出登录',
                'nav.back': '返回仪表板',
                'student.list': '学生列表',
                'student.manage': '管理学生',
                'student.description': '管理学生信息和定价',
                'student.search': '搜索学生...',
                'schedule.view': '查看日程',
                'schedule.description': '查看和管理教学日程',
                'schedule.add': '添加日程',
                'schedule.suggestions': '排课建议',
                'schedule.suggestions.get': '获取建议',
                'lesson.manage': '管理课程',
                'lesson.add': '添加课程',
                'lesson.list': '课程历史',
                'lesson.date': '日期',
                'lesson.duration': '时长（分钟）',
                'lesson.notes': '备注',
                'lesson.difficulty': '难度',
                'homework.manage': '管理作业',
                'homework.add': '添加作业',
                'homework.list': '作业列表',
                'homework.title': '标题',
                'homework.description': '描述',
                'homework.dueDate': '截止日期',
                'homework.difficulty': '难度',
                'homework.toggle': '切换状态',
                'btn.save': '保存',
                'btn.cancel': '取消',
                'btn.edit': '编辑',
                'btn.delete': '删除',
                'btn.add': '添加',
                'calendar.sun': '日',
                'calendar.mon': '一',
                'calendar.tue': '二',
                'calendar.wed': '三',
                'calendar.thu': '四',
                'calendar.fri': '五',
                'calendar.sat': '六',
                'calendar.previous': '上一月',
                'calendar.next': '下一月'
            }
        };
        return messages[language] || messages.en;
    }

    getMessage(key) {
        const message = this.messages[this.currentLanguage]?.[key] || key;
        console.log(`Getting translation for "${key}" in "${this.currentLanguage}": "${message}"`);
        return message;
    }

    async setLanguage(language) {
        console.log('setLanguage called with:', language);
        
        if (!window.APP_CONFIG.SUPPORTED_LANGUAGES.includes(language)) {
            console.warn(`Unsupported language: ${language}`);
            return;
        }

        // Load messages if not already loaded
        if (!this.messages[language]) {
            console.log('Loading messages for language:', language);
            await this.loadMessages(language);
        }

        this.currentLanguage = language;
        localStorage.setItem('preferredLanguage', language);
        console.log('Current language set to:', this.currentLanguage);
        
        // Update document attributes
        document.documentElement.lang = language;
        document.documentElement.dir = this.getDirection(language);
        
        // Update all translatable elements
        console.log('Updating page translations...');
        this.updatePageTranslations();
        console.log('Page translations updated');
        
        // Update any language-specific styling
        updateLanguageStyling(language);
        
        // Re-initialize any components that need language refresh
        reinitializeComponents();
        
        console.log('Language change completed for:', language);
    }

    getDirection(language) {
        // Add RTL languages here if needed in the future
        const rtlLanguages = ['ar', 'he', 'fa'];
        return rtlLanguages.includes(language) ? 'rtl' : 'ltr';
    }

    updatePageTranslations() {
        console.log('updatePageTranslations called, current language:', this.currentLanguage);
        console.log('Available messages for current language:', Object.keys(this.messages[this.currentLanguage] || {}));
        
        // Update elements with data-i18n attribute
        const elements = document.querySelectorAll('[data-i18n]');
        console.log('Found elements with data-i18n:', elements.length);
        
        elements.forEach((element, index) => {
            const key = element.getAttribute('data-i18n');
            const message = this.getMessage(key);
            console.log(`Element ${index}: key="${key}" -> "${message}"`);
            
            if (element.tagName === 'INPUT' || element.tagName === 'TEXTAREA') {
                element.placeholder = message;
            } else {
                element.textContent = message;
            }
        });

        // Update page title
        const titleElement = document.querySelector('title');
        if (titleElement) {
            const titleKey = titleElement.getAttribute('data-i18n') || 'app.title';
            titleElement.textContent = this.getMessage(titleKey);
            console.log('Updated title to:', this.getMessage(titleKey));
        }
        
        console.log('Page translation update completed');
    }

    getCurrentLanguage() {
        return this.currentLanguage;
    }
}

// Initialize language manager
window.languageManager = new LanguageManager();

// Apply initial translations when DOM is ready
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM loaded, applying initial translations...');
    if (window.languageManager) {
        window.languageManager.updatePageTranslations();
    }
});

// Also apply translations after a short delay as backup
setTimeout(() => {
    if (window.languageManager) {
        console.log('Applying initial translations (backup)...');
        window.languageManager.updatePageTranslations();
    }
}, 500);

// Utility functions
function t(key) {
    return window.languageManager.getMessage(key);
}

// DOM content loaded handler
document.addEventListener('DOMContentLoaded', function() {
    // Initialize language selector if present
    const languageSelector = document.querySelector('.language-selector');
    if (languageSelector) {
        // Language selector is handled by the component itself
    }

    // Initialize performance optimizations
    window.performanceOptimizer.lazyLoadImages();
    
    // Set up periodic cache cleanup
    setInterval(() => {
        window.performanceOptimizer.cleanupCache();
    }, 60000); // Clean up every minute

    // Initial page translation
    window.languageManager.updatePageTranslations();
});

// Update language-specific styling
function updateLanguageStyling(language) {
    const body = document.body;
    
    // Remove language-specific classes
    body.classList.remove('lang-en', 'lang-zh');
    
    // Add current language class
    body.classList.add(`lang-${language}`);
    
    // Update font family for Chinese
    if (language === 'zh') {
        body.style.fontFamily = '"PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "WenQuanYi Micro Hei", sans-serif';
    } else {
        body.style.fontFamily = ''; // Reset to default
    }
}

// Reinitialize components after language change
function reinitializeComponents() {
    // Re-initialize tooltips, popovers, etc.
    if (window.bootstrap) {
        // Re-initialize Bootstrap tooltips
        const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new window.bootstrap.Tooltip(tooltipTriggerEl);
        });
    }
}

// Performance optimizations
class PerformanceOptimizer {
    constructor() {
        this.cache = new Map();
        this.cacheTimeout = 5 * 60 * 1000; // 5 minutes
        this.initPerformanceMonitoring();
    }

    // Simple in-memory cache with TTL
    setCache(key, data, ttl = this.cacheTimeout) {
        this.cache.set(key, {
            data,
            timestamp: Date.now(),
            ttl
        });
    }

    getCache(key) {
        const cached = this.cache.get(key);
        if (!cached) return null;
        
        if (Date.now() - cached.timestamp > cached.ttl) {
            this.cache.delete(key);
            return null;
        }
        
        return cached.data;
    }

    // Debounce function for API calls
    debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }

    // Throttle function for scroll events
    throttle(func, limit) {
        let inThrottle;
        return function() {
            const args = arguments;
            const context = this;
            if (!inThrottle) {
                func.apply(context, args);
                inThrottle = true;
                setTimeout(() => inThrottle = false, limit);
            }
        };
    }

    // Lazy loading for images
    lazyLoadImages() {
        const images = document.querySelectorAll('img[data-src]');
        const imageObserver = new IntersectionObserver((entries, observer) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const img = entry.target;
                    img.src = img.dataset.src;
                    img.removeAttribute('data-src');
                    observer.unobserve(img);
                }
            });
        });

        images.forEach(img => imageObserver.observe(img));
    }

    // Optimize DOM updates
    batchDOMUpdates(updates) {
        requestAnimationFrame(() => {
            updates.forEach(update => update());
        });
    }

    // Initialize performance monitoring
    initPerformanceMonitoring() {
        // Monitor page load performance
        window.addEventListener('load', () => {
            const perfData = performance.getEntriesByType('navigation')[0];
            console.log('Page Load Performance:', {
                domContentLoaded: perfData.domContentLoadedEventEnd - perfData.domContentLoadedEventStart,
                loadComplete: perfData.loadEventEnd - perfData.loadEventStart,
                totalTime: perfData.loadEventEnd - perfData.fetchStart
            });
        });

        // Monitor long tasks
        if ('PerformanceObserver' in window) {
            const observer = new PerformanceObserver((list) => {
                list.getEntries().forEach((entry) => {
                    if (entry.duration > 50) {
                        console.warn('Long task detected:', {
                            name: entry.name,
                            duration: entry.duration,
                            startTime: entry.startTime
                        });
                    }
                });
            });
            observer.observe({ entryTypes: ['longtask'] });
        }
    }

    // Clear expired cache entries
    cleanupCache() {
        const now = Date.now();
        for (const [key, value] of this.cache.entries()) {
            if (now - value.timestamp > value.ttl) {
                this.cache.delete(key);
            }
        }
    }
}

// Initialize performance optimizer
window.performanceOptimizer = new PerformanceOptimizer();

// API helper functions with caching
class ApiHelper {
    static async request(endpoint, options = {}) {
        const url = `${window.APP_CONFIG.API_BASE_URL}${endpoint}`;
        const cacheKey = `${endpoint}_${JSON.stringify(options)}`;
        
        // Check cache for GET requests
        if (!options.method || options.method === 'GET') {
            const cached = window.performanceOptimizer.getCache(cacheKey);
            if (cached) {
                console.log('Cache hit for:', endpoint);
                return cached;
            }
        }

        const defaultOptions = {
            headers: {
                'Content-Type': 'application/json',
                'Accept-Language': window.languageManager.getCurrentLanguage()
            }
        };

        const finalOptions = { ...defaultOptions, ...options };

        try {
            const response = await fetch(url, finalOptions);
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            const data = await response.json();
            
            // Cache successful GET requests
            if (!options.method || options.method === 'GET') {
                window.performanceOptimizer.setCache(cacheKey, data);
            }
            
            return data;
        } catch (error) {
            console.error('API request failed:', error);
            throw error;
        }
    }

    static async get(endpoint) {
        return this.request(endpoint);
    }

    static async post(endpoint, data) {
        return this.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    }

    static async put(endpoint, data) {
        return this.request(endpoint, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    }

    static async delete(endpoint) {
        return this.request(endpoint, {
            method: 'DELETE'
        });
    }

    // Debounced API calls for search functionality
    static debouncedGet(endpoint, delay = 300) {
        return window.performanceOptimizer.debounce(() => this.get(endpoint), delay);
    }
}

// Export for global use
window.ApiHelper = ApiHelper;
window.t = t;

// Error handling
window.addEventListener('error', function(e) {
    console.error('Global error:', e.error);
});

// Unhandled promise rejection handling
window.addEventListener('unhandledrejection', function(e) {
    console.error('Unhandled promise rejection:', e.reason);
});

console.log('Piano Teacher Management System - Main.js loaded');
