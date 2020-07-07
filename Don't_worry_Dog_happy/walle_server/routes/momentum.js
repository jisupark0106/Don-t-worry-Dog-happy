/*만보기로부터 운동정보 받아 momentum에 저장*/
var express = require("express");
var router = express.Router();
var mysql = require("mysql");
var config = require("../db/db_info").local;
var dbconfig = require("../db/db_con")();
var pool = mysql.createPool(config);
//var connection = dbconfig.init();
//dbconfig.test_open(connection);

router.post("/", function (req, res) {
  var dstep = req.body.dstep;
  var madd = req.body.madd;
  var stime = req.body.stime;
  var dId = 27;

  pool.getConnection(function (err, connection) {
    //var q = "INSERT INTO `walle`.`persons` (`name`, `age`) VALUES ('" + madd +"', '"+ dstep +"')";
    var q =
      "INSERT INTO `walle`.`momentum` (`dId`, `stepCount`, `macAdd`) VALUES ('" +
      dId +
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

/* 모니터링 그래프 제공 : 몸무게, 운동량 그래프로 최근 5개까지 확인 */
router.get("/wgraph", function (req, res) {
  var member_dogid = req.headers.did;
  var wgraph_list;
  var mgraph_list;
  var max_weight;
  var min_weight;
  /*var mgraph_empty={
          mgId : 1,
          dId  : member_dogid,
          momentum  : 0,
          date : " "
    };*/
  var graph_message;
  console.log(member_dogid);
  pool.getConnection(function (err, connection) {
    var sql_wgraph =
      "select * from wgraph where dId = '" +
      member_dogid +
      "' order by wgraph.date desc limit 5";

    var sql_show_wgraph = connection.query(sql_wgraph, function (err, rows) {
      //console.log(rows);
      if (err) {
        connection.release();
        throw err;
      }

      wgraph_list = rows;

      var sql_minmax =
        "select minWeight, maxWeight from stweight,mydog where mydog.dId='" +
        member_dogid +
        "' and stweight.stId=mydog.stId";

      var sql_show_minmax = connection.query(sql_minmax, function (err, rows) {
        if (err) {
          connection.release();
          throw err;
        } else {
          // if(rows.length==0){
          //   mgraph_list[0].mgId = 1;
          //   mgraph_list[0].dId = member_dogid;
          //   mgraph_list[0].momentum = 0;
          //   mgraph_list[0].date = " ";
          // }

          min_weight = rows[0].minWeight;
          max_weight = rows[0].maxWeight;

          var sql_mgraph =
            "select * from mgraph where dId = '" +
            member_dogid +
            "' order by mgraph.date desc limit 30";

          var sql_show_mgraph = connection.query(sql_mgraph, function (
            err,
            rows
          ) {
            if (err) {
              connection.release();
              throw err;
            } else {
              if (rows.length == 0) {
                graph_message = "mgraph is empty";
                res.status(201).send({
                  status: true,
                  message: graph_message,
                  dogid: member_dogid,
                  max: max_weight,
                  min: min_weight,
                  wgraphlist: wgraph_list,
                });
              } else {
                graph_message = "get graph success";
                mgraph_list = rows;
                res.status(201).send({
                  status: true,
                  message: graph_message,
                  dogid: member_dogid,
                  max: max_weight,
                  min: min_weight,
                  wgraphlist: wgraph_list,
                  mgraphlist: mgraph_list,
                });
              }
            }
            console.log(rows);
          });
        }
      });
    });

    connection.release();
  });
});
/* GET home page. */
module.exports = router;
