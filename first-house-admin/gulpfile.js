// Generated on 2015-09-28 using generator-jhipster 2.21.1
/* jshint camelcase: false */
'use strict';

var gulp = require('gulp'),
    gutil = require('gulp-util'),
    prefix = require('gulp-autoprefixer'),
    minifyCss = require('gulp-minify-css'),
    usemin = require('gulp-usemin'),
    uglify = require('gulp-uglify'),
    sass = require('gulp-sass'),
    htmlmin = require('gulp-htmlmin'),
    imagemin = require('gulp-imagemin'),
    ngAnnotate = require('gulp-ng-annotate'),
    ngConstant = require('gulp-ng-constant-fork'),
    jshint = require('gulp-jshint'),
    rev = require('gulp-rev'),
    proxy = require('proxy-middleware'),
    es = require('event-stream'),
    flatten = require('gulp-flatten'),
    del = require('del'),
    url = require('url'),
    wiredep = require('wiredep').stream,
    fs = require('fs'),
    runSequence = require('run-sequence'),
    browserSync = require('browser-sync');

var karma = require('gulp-karma')({configFile: 'src/test/javascript/karma.conf.js'});

var yeoman = {
    app: 'www/',
    dist: 'dist/',
    test: 'test/spec/',
    tmp: '.tmp/',
    importPath: 'www/lib',
    scss: 'scss/',
    port: 9000,
    apiPort: 8080,
    liveReloadPort: 35729
};

var endsWith = function (str, suffix) {
    return str.indexOf('/', str.length - suffix.length) !== -1;
};

gulp.task('clean', function (cb) {
  del([yeoman.dist], cb);
});

gulp.task('clean:tmp', function (cb) {
  del([yeoman.tmp], cb);
});

gulp.task('test', ['wiredep:test', 'ngconstant:dev'], function() {
    karma.once();
});

gulp.task('copy', function() {
    return es.merge( 
              gulp.src(yeoman.app + 'fonts/*.{woff,svg,ttf,eot}').
              pipe(flatten()).
              pipe(gulp.dest(yeoman.dist + 'fonts/')));
});
gulp.task('copy-other', function() {
    gulp.src(yeoman.app + 'lib/bootstrap-css/fonts/**/*.*').
        pipe(gulp.dest(yeoman.dist + 'lib/bootstrap-css/fonts/'));
    gulp.src(yeoman.app + 'lib/swagger-ui/**/*.*').
        pipe(gulp.dest(yeoman.dist + 'lib/swagger-ui/'));
    gulp.src(yeoman.app + 'lib-ext/**/*.*').
        pipe(gulp.dest(yeoman.dist + 'lib-ext/'));
});

gulp.task('images', function() {
    return gulp.src(yeoman.app + 'img/**').
        pipe(imagemin({optimizationLevel: 5})).
        pipe(gulp.dest(yeoman.dist + 'img')).
        pipe(browserSync.reload({stream: true}));
});

gulp.task('sass', function () {
    // return gulp.src(yeoman.scss + '**/*.scss')
    //     .pipe(sass({includePaths:yeoman.importPath}).on('error', sass.logError))
    //     .pipe(gulp.dest(yeoman.app + 'css'));
});

gulp.task('styles', ['sass'], function() {
    return gulp.src(yeoman.app + 'css/**/*.css').
        pipe(gulp.dest(yeoman.tmp)).
        pipe(browserSync.reload({stream: true}));
});

gulp.task('serve', function() {
    runSequence('wiredep:test', 'wiredep:app', 'ngconstant:dev', 'sass', function () {
        var baseUri = 'http://localhost:' + yeoman.apiPort;
        // Routes to proxy to the backend. Routes ending with a / will setup
        // a redirect so that if accessed without a trailing slash, will
        // redirect. This is required for some endpoints for proxy-middleware
        // to correctly handle them.
        var proxyRoutes = [
            '/api',
            '/health',
            '/configprops',
            '/v2/api-docs',
            // '/swagger-ui',
            '/configuration/security',
            '/configuration/ui',
            '/swagger-resources',
            '/metrics',
            '/websocket/tracker',
            '/dump'
        ];

        var requireTrailingSlash = proxyRoutes.filter(function (r) {
            return endsWith(r, '/');
        }).map(function (r) {
            // Strip trailing slash so we can use the route to match requests
            // with non trailing slash
            return r.substr(0, r.length - 1);
        });

        var proxies = [
            // Ensure trailing slash in routes that require it
            function (req, res, next) {
                requireTrailingSlash.forEach(function(route){
                    if (url.parse(req.url).path === route) {
                        res.statusCode = 301;
                        res.setHeader('Location', route + '/');
                        res.end();
                    }
                });

                next();
            }
        ].concat(
            // Build a list of proxies for routes: [route1_proxy, route2_proxy, ...]
            proxyRoutes.map(function (r) {
                var options = url.parse(baseUri + r);
                options.route = r;
                return proxy(options);
            }));

        browserSync({
            open: false,
            port: yeoman.port,
            server: {
                baseDir: yeoman.app,
                middleware: proxies
            }
        });

        gulp.run('watch');
    });
});

gulp.task('watch', function() {
    gulp.watch('bower.json', ['wiredep:test', 'wiredep:app']);
    gulp.watch(['gulpfile.js', 'pom.xml'], ['ngconstant:dev']);
    gulp.watch(yeoman.scss + '**/*.scss', ['styles']);
    gulp.watch(yeoman.app + 'img/**', ['images']);
    gulp.watch([yeoman.app + '*.html', yeoman.app + 'js/**', yeoman.app + 'templates/**']).on('change', browserSync.reload);
});

gulp.task('wiredep', ['wiredep:test', 'wiredep:app']);

gulp.task('wiredep:app', function () {
    var s = gulp.src('www/index.html')
        .pipe(wiredep())
        .pipe(gulp.dest('www'));
    return s;
});

gulp.task('wiredep:test', function () {
    return gulp.src('src/test/javascript/karma.conf.js')
        .pipe(wiredep({
            exclude: [/angular-i18n/, /angular-scenario/],
            ignorePath: /\.\.\/\.\.\//, // remove ../../ from paths of injected javascripts
            devDependencies: true,
            fileTypes: {
                js: {
                    block: /(([\s\t]*)\/\/\s*bower:*(\S*))(\n|\r|.)*?(\/\/\s*endbower)/gi,
                    detect: {
                        js: /'(.*\.js)'/gi
                    },
                    replace: {
                        js: '\'{{filePath}}\','
                    }
                }
            }
        }))
        .pipe(gulp.dest('src/test/javascript'));
});

gulp.task('build', function () {
    runSequence('clean', 'copy', 'wiredep:app', 'ngconstant:prod', 'usemin', 'copy-other');
});

gulp.task('usemin', function() {
    runSequence('images', 'styles', function () {
        return gulp.src([yeoman.app + '**/*.html', '!' + yeoman.app + 'lib/**/*.html',
                         '!' + yeoman.app + 'lib-ext/**/*.html']).
            pipe(usemin({
                css: [
                    prefix.apply(),
                    minifyCss({root: 'www'}),  // Replace relative paths for static resources with absolute path with root
                    'concat', // Needs to be present for minifyCss root option to work
                    rev()
                ],
                html: [
                    htmlmin({collapseWhitespace: true})
                ],
                js: [
                    ngAnnotate(),
                    uglify(),
                    'concat',
                    rev()
                ]
            })).
            pipe(gulp.dest(yeoman.dist));
    });
});

gulp.task('ngconstant:dev', function() {
    return ngConstant({
        dest: 'app.constants.js',
        name: 'firstHouseAdmin',
        deps:   false,
        noFile: true,
        interpolate: /\{%=(.+?)%\}/g,
        wrap: '/* jshint quotmark: false */\n"use strict";\n// DO NOT EDIT THIS FILE, EDIT THE GULP TASK NGCONSTANT SETTINGS INSTEAD WHICH GENERATES THIS FILE\n{%= __ngModule %}',
        constants: {
            ENV: 'dev',
            VERSION: 1.0
        }
    })
    .pipe(gulp.dest(yeoman.app + 'js/'));
});

gulp.task('ngconstant:prod', function() {
    return ngConstant({
        dest: 'app.constants.js',
        name: 'firstHouseAdmin',
        deps:   false,
        noFile: true,
        interpolate: /\{%=(.+?)%\}/g,
        wrap: '/* jshint quotmark: false */\n"use strict";\n// DO NOT EDIT THIS FILE, EDIT THE GULP TASK NGCONSTANT SETTINGS INSTEAD WHICH GENERATES THIS FILE\n{%= __ngModule %}',
        constants: {
            ENV: 'prod',
            VERSION: 1.0
        }
    })
    .pipe(gulp.dest(yeoman.tmp + 'js/'));
});

gulp.task('jshint', function() {
    return gulp.src(['gulpfile.js', yeoman.app + 'js/**/*.js'])
        .pipe(jshint())
        .pipe(jshint.reporter('jshint-stylish'));
});

gulp.task('server', ['serve'], function () {
    gutil.log('The `server` task has been deprecated. Use `gulp serve` to start a server');
});
gulp.task('dist-server', function() {
    var baseUri = 'http://localhost:' + yeoman.apiPort;
    // Routes to proxy to the backend. Routes ending with a / will setup
    // a redirect so that if accessed without a trailing slash, will
    // redirect. This is required for some endpoints for proxy-middleware
    // to correctly handle them.
    var proxyRoutes = [
        '/api',
        '/health',
        '/configprops',
        '/v2/api-docs',
        '/swagger-ui',
        '/configuration/security',
        '/configuration/ui',
        '/swagger-resources',
        '/metrics',
        '/websocket/tracker',
        '/dump'
    ];

    var requireTrailingSlash = proxyRoutes.filter(function (r) {
        return endsWith(r, '/');
    }).map(function (r) {
        // Strip trailing slash so we can use the route to match requests
        // with non trailing slash
        return r.substr(0, r.length - 1);
    });

    var proxies = [
        // Ensure trailing slash in routes that require it
        function (req, res, next) {
            requireTrailingSlash.forEach(function(route){
                if (url.parse(req.url).path === route) {
                    res.statusCode = 301;
                    res.setHeader('Location', route + '/');
                    res.end();
                }
            });

            next();
        }
    ].concat(
        // Build a list of proxies for routes: [route1_proxy, route2_proxy, ...]
        proxyRoutes.map(function (r) {
            var options = url.parse(baseUri + r);
            options.route = r;
            return proxy(options);
        }));

    browserSync({
        open: false,
        port: yeoman.port,
        server: {
            baseDir: yeoman.dist,
            middleware: proxies
        }
    });
    gulp.run('watch');
});

gulp.task('default', function() {
    runSequence('serve');
});
