/*사용자 입력 처리*/
var express = require("express");
var router = express.Router();
var mysql = require("mysql");
var config = require("../db/db_info").local;
var dbconfig = require("../db/db_con")();
var pool = mysql.createPool(config);

//회원 와이파이 mac주소 변경
router.post("/MacUpdate", function (req, res) {
  var macInfo = {
    userid: req.body.userid,
    mac: req.body.mac,
  };

  //var weight = req.body.weight;
  //var dId = 15;

  pool.getConnection(function (err, connection) {
    var update_mac_query =
      "UPDATE `walle`.`person` SET `macAdd`='" +
      macInfo.mac +
      "' WHERE `userId`='" +
      macInfo.userid +
      "'";
    var update_mac = connection.query(update_mac_query, function (err, rows) {
      if (err) {
        connection.release();

        throw err;
      }

      //console.log(rows);
      res.status(201).send({
        status: true,
        message: "successful Mac Update",
      });

      connection.release();
    });
  });
});
// 사료 칼로리 계산
router.post("/FeedUpdate", function (req, res) {
  var feedInfo = {
    did: req.body.dId,
    kcal: req.body.kcal,
  };

  //var weight = req.body.weight;
  //var dId = 15;

  pool.getConnection(function (err, connection) {
    var update_feed_query =
      "UPDATE `walle`.`mydog` SET `kcal`='" +
      feedInfo.kcal +
      "' WHERE `dId`='" +
      feedInfo.did +
      "'";
    var update_feed = connection.query(update_feed_query, function (err, rows) {
      if (err) {
        connection.release();

        throw err;
      }

      //console.log(rows);
      res.status(201).send({
        status: true,
        message: "successful gram Update",
      });

      connection.release();
    });
  });
});

// 체중 업데이트
router.post("/weightUpdate", function (req, res) {
  var weightInfo = {
    weight: req.body.weight,
    dId: req.body.dId,
  };

  var before_weight = 0.0;
  var before_cfm = 0.0;
  var member_status = 0;
  var stId = 0;
  //var weight = req.body.weight;
  //var dId = 15;

  pool.getConnection(function (err, connection) {
    var query_get_w =
      "SELECT mydog.curWeight,mydog.cfm, mydog.status, mydog.stId from mydog where dId = '" +
      weightInfo.dId +
      "'";

    //console.log(q);

    var query = connection.query(query_get_w, function (err, rows) {
      if (err) {
        connection.release();

        throw err;
      }

      before_weight = rows[0].curWeight;
      before_cfm = rows[0].cfm;
      member_status = rows[0].status;
      stId = rows[0].stId;
      //console.log(rows);
      var stWeight_query =
        "SELECT minWeight,maxWeight from stweight where stId ='" +
        rows[0].stId +
        "'";

      var query2 = connection.query(stWeight_query, function (err, rows) {
        if (err) {
          connection.release();

          throw err;
        }

        if (weightInfo.weight < rows[0].minWeight) {
          member_status = 0;
        } else if (weightInfo.weight > rows[0].maxWeight) {
          member_status = 2;
        } else {
          member_status = 1;
        }

        if (member_status == 2) {
          if (before_weight <= weightInfo.weight) {
            before_cfm = before_cfm - 7;
          }
        }
        if (member_status == 0) {
          if (before_weight >= weightInfo.weight) {
            before_cfm = before_cfm + 7;
          }
        }

        var q =
          "UPDATE `walle`.`mydog` SET `curWeight`='" +
          weightInfo.weight +
          "', cfm = '" +
          before_cfm +
          "', status = '" +
          member_status +
          "' WHERE `dId`='" +
          weightInfo.dId +
          "'";
        var query1 = connection.query(q, function (err, rows) {
          if (err) {
            connection.release();

            throw err;
          }

          var q2 =
            "INSERT INTO `walle`.`wgraph` (`dId`, `weight`) VALUES ('" +
            weightInfo.dId +
            "', '" +
            weightInfo.weight +
            "')";

          var query2 = connection.query(q2, function (err, rows) {
            if (err) {
              connection.release();

              throw err;
            }

            //console.log(rows);
            res.status(201).send({
              status: true,
              message: "successful weight Update",
            });
          });
          //res.json(rows);
        });
        connection.release();
      });
    });

    //console.log(query);
  });
});
module.exports = router;
