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

      var a = getRandomInt(204, 208);
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
  var logInInfo = {
    member_age: req.body.age,
    member_weight: req.body.weight,
    member_size: req.body.size,
    member_type: req.body.type,
  };

  pool.getConnection(function (err, connection) {
    //var q = "INSERT INTO `walle`.`persons` (`name`, `age`) VALUES ('" + madd +"', '"+ dstep +"')";
    //var q = "INSERT INTO `walle`.`stweight` (`memId`, `stepCount`, `macAdd`) VALUES ('" + mid +"', '"+ dstep +"', '"+ madd +"')";
    var q =
      "SELECT * FROM walle.stweight where type='" + logInInfo.member_type + "'";
    var rer = 0.0;
    console.log(q);

    var query = connection.query(q, function (err, rows) {
      if (err) {
        connection.release();

        throw err;
      }
      //console.log(dstep +" "+ madd +" "+ stime);
      //console.log(rows);

      res.json(rows);
      rer = rows[0].rer;
      console.log(rows);
      console.log(rows[0].rer);
      console.log("rer = " + rer);
      var cal = rer * 1.4;
      console.log("cal = " + cal);
      connection.release();

      // res.status(201).send({
      //   status : true,
      //   message : "successful calculate",
      //   cal : cal
      //   });
    });

    //console.log(query);
  });
});

// router.post('/', function(req,res){
//   var dstep = req.body.dstep;
//   var madd = req.body.madd;
//   var stime = req.body.stime;
//   var mid = 1;
//
//     pool.getConnection(function(err,connection){
//       //var q = "INSERT INTO `walle`.`persons` (`name`, `age`) VALUES ('" + madd +"', '"+ dstep +"')";
//       var q = "INSERT INTO `walle`.`momentum` (`memId`, `stepCount`, `macAdd`) VALUES ('" + mid +"', '"+ dstep +"', '"+ madd +"')";
//       console.log(q);
//
//         var query = connection.query(q, function (err, rows) {
//
//             if(err){
//
//                 connection.release();
//
//                 throw err;
//
//             }
//             console.log(dstep +" "+ madd +" "+ stime);
//             //console.log(rows);
//
//             res.json(rows);
//
//             connection.release();
//
//         });
//         //console.log(query);
//     });
// });

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
