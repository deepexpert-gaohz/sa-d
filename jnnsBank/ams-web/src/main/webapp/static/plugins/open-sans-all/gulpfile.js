var gulp = require("gulp");
var sass = require("gulp-sass");
var less = require("gulp-less");
var rename = require("gulp-rename");
var cssmin = require("gulp-cssmin");

gulp.task("scss", function () {
    return gulp.src("./scss/open-sans.scss")
        .pipe(sass().on("error", sass.logError))
        .pipe(gulp.dest("./css"));
});

gulp.task("less", function () {
    return gulp.src("./less/open-sans.less")
        .pipe(less())
        .pipe(gulp.dest("./css"));
});

gulp.task("minify", ["scss"], function () {
    return gulp.src("./css/open-sans.css")
        .pipe(cssmin())
        .pipe(rename({suffix: ".min"}))
        .pipe(gulp.dest("./css"));
});

gulp.task("default", ["minify"]);