/* momentum -> id별로 mgraph 저장후 -> momentum데이터 삭제*/
exports.sort = function () {
  var mysql = require("mysql");
  var config = require("./db/db_info").local;
  var dbconfig = require("./db/db_con")();
  var pool = mysql.createPool(config);

  pool.getConnection(function (err, connection) {
    //var q = "INSERT INTO `walle`.`persons` (`name`, `age`) VALUES ('" + madd +"', '"+ dstep +"')";
    var q1 =
      "SELECT dId, sum(stepCount)as stepCount FROM  walle.momentum GROUP BY dId";

    var query = connection.query(q1, function (err, rows) {
      if (err) {
        connection.release();

        throw err;
      }
      /*ok
      for(var i=0; i < rows.length; i++){
          console.log(rows[i].dId);
          console.log(rows[i].stepCount);
      }
      */
      for (var i = 0; i < rows.length; i++) {
        var q2 =
          "INSERT INTO walle.mgraph (dId, momentum) VALUES (" +
          rows[i].dId +
          " , " +
          rows[i].stepCount +
          ")";
        var query = connection.query(q2, function (err, rows) {
          if (err) {
            connection.release();

            throw err;
          }

          var q3 =
            "DELETE FROM `walle`.`momentum` WHERE momentum.curTime between date(current_date())-1 and date(current_timestamp())+1";
          var query3 = connection.query(q3, function (err, rows) {
            if (err) {
              connection.release();

              throw err;
            }

            console.log(rows);

            res.status(201).send({
              status: true,
              message: "successful momentum",
            });
          });
        });
      }

      connection.release();
      console.log("success mgraphdb");
    });
  });
};
