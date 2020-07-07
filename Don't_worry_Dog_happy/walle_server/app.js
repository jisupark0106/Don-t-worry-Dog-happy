var createError = require("http-errors");
var express = require("express");
var path = require("path");
var logger = require("morgan");
/*eeeee*/
var schedule = require("node-schedule");

var indexRouter = require("./routes/index");
var usersRouter = require("./routes/users");
var serverRouter = require("./routes/serverserver");
var dbTestRouter = require("./routes/feed");
var memberRouter = require("./routes/userinfo");
var momentumRouter = require("./routes/momentum");

/*eeeee*/
var mgraphdb = require("./mgraph");

var app = express();

/*eeeee time set need*/
schedule.scheduleJob(" 30 * * * * *", function () {
  mgraphdb.sort();
});

app.use("/", indexRouter);
app.use("/users", usersRouter);
app.use("/server", serverRouter);
app.use("/feed", dbTestRouter);
app.use("/userinfo", memberRouter);
app.use("/momentum", momentumRouter);

// catch 404 and forward to error handler
app.use(function (req, res, next) {
  next(createError(404));
});

// error handler
app.use(function (err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get("env") === "development" ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render("error");
});

module.exports = app;
