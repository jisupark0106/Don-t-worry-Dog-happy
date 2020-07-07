/* 배식 기능 */
var express = require("express");
var router = express.Router();
var mysql = require("mysql");
var config = require("../db/db_info").local;
var dbconfig = require("../db/db_con")();
var pool = mysql.createPool(config);

//var connection = dbconfig.init();
//dbconfig.test_open(connection);

/* GET home page. */
//  var first_check=false;

router.get("/", function (req, res) {
  var user_id = "ha";
  var dog_id = 27;
  var person_mac;
  var dog_mac;

  var last_dog_time;
  var m_recent_time;

  //10시-8시

  //  var curdate=new Date();
  //  var curHours=curdate.getHours();
  //  console.log(curHours);

  //if(curHours==17||curHours==20){
  pool.getConnection(function (err, connection) {
    //   feed_check=true;
    var get_person_mac =
      "select person.macAdd from person where userId='" + user_id + "'";
    var get_dog_mac =
      "select momentum.macAdd,momentum.curTime from momentum where dId='" +
      dog_id +
      "' order by curTime desc";
    var get_cfm =
      "SELECT mydog.cfm,kcal from mydog where dId = '" + dog_id + "'";
    var get_person_mac_query = connection.query(get_person_mac, function (
      err,
      rows
    ) {
      if (err) {
        connection.release();

        throw err;
      }
      res.json("20");
      connection.release();
      //  perosn_mac=rows[0].macAdd;

      /*  var get_dog_mac_query = connection.query(get_dog_mac, function (err, rows) {

                if(err){

                    connection.release();

                    throw err;

                }
                dog_mac=rows[0].macAdd;
                m_recent_time=rows[0].curTime;

    //             //범위안에 들어오면 m_time 그 현재시간이랑 얼마나 차이 나는지? 집에있는지
    //             //같으면  mac주소 확인하기

                console.log(m_recent_time.getHours());

                res.json("500");
              /*  if(person_mac==dog_mac){

                      if(curHours==m_recent_time.getHours()){
                        var get_cfm_query = connection.query(get_cfm, function (err, rows) {

                            if(err){

                                connection.release();

                                throw err;

                            }
                            if(feed_check){

                                res.json(rows[0].cfm);
                        }
                          connection.release();
                        });

                }
              }
              else{
                res.json("dog out...");
              }*/

      //
      //
      //
      //
      //         //feed_check true -> cfm 확인해서 밥주기
      //       //  var a = getRandomInt(37,45);
      //         //console.log(a);
      //         //var b = a.toString();
      //       //  console.log(b);
      //       //  res.send(b);
      //
      //
      //
    });
    //
    //     //console.log(query);
    //   });
    // });
  });
  //}
  // else{
  //   res.json("nonononononono....you can't eat");
  // }
});

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
