var express = require("express");
var router = express.Router();
var mysql = require("mysql");
var config = require("../db/db_info").local;
var dbconfig = require("../db/db_con")();
var pool = mysql.createPool(config);
//var connection = dbconfig.init();
//dbconfig.test_open(connection);

/* GET home page. */

router.get("/", function (req, res) {
  pool.getConnection(function (err, connection) {
    var q = "select * from walle.persons";
    var query = connection.query(q, function (err, rows) {
      if (err) {
        connection.release();

        throw err;
      }

      //console.log(rows);

      //res.json(rows);

      var a = getRandomInt(37, 45);
      console.log(a);
      var b = a.toString();
      console.log(b);
      res.send(b);

      connection.release();
    });

    //console.log(query);
  });
});
router.post("/", function (req, res) {
  var dstep = req.body.dstep;
  var madd = req.body.madd;
  var stime = req.body.stime;
  var mid = 1;

  pool.getConnection(function (err, connection) {
    //var q = "INSERT INTO `walle`.`persons` (`name`, `age`) VALUES ('" + madd +"', '"+ dstep +"')";
    var q =
      "INSERT INTO `walle`.`momentum` (`memId`, `stepCount`, `macAdd`) VALUES ('" +
      mid +
      "', '" +
      dstep +
      "', '" +
      madd +
      "')";
    console.log(q);

    var query = connection.query(q, function (err, rows) {
      if (err) {
        connection.release();

        throw err;
      }
      console.log(dstep + " " + madd + " " + stime);
      //console.log(rows);

      res.json(rows);

      connection.release();
    });

    //console.log(query);
  });
});

function getRandomInt(min, max) {
  //min ~ max 사이의 임의의 정수 반환
  return Math.floor(Math.random() * (max - min)) + min;
}

// configuration ===============================================================

/*
router.get('/', function(req, res){
  var q = 'SELECT * from Persons';
  connection.query(q, function (err, result) {

        console.log('The solution is: ', result);
        res.send(result);

  });
});
*/

module.exports = router;
