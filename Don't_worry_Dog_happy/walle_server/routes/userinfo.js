/*회원가입 -> person, mydog table :  stweight rer -> cfm 변경*/
var express = require("express");
var router = express.Router();
var mysql = require("mysql");
var config = require("../db/db_info").local;
var dbconfig = require("../db/db_con")();
var pool = mysql.createPool(config);
//var connection = dbconfig.init();
//dbconfig.test_open(connection);

/* GET home page. */

// 로그인 '/' post 방식 요청 처리
router.post("/login", function (req, res, next) {
  var logInInfo = {
    member_id: req.body.userid,
    member_pwd: req.body.pwd,
  };
  logIn(logInInfo, function (error, results) {
    if (error) {
      console.log("Connection error " + error);
      return res.send(error);
    } else if (results.message == "check information") {
      // res.status(201).render('login', {urls :url});
      //res.redirect('login');
      res.send("check information");
    } else {
      if (results.member_info) {
        //var sess = req.session;
        //console.log(req.session);
        // sess.member_id = results.member_info.member_email;
        //sess = results.member_info.member_email;
        //var result_value = {message : results.message};
        //res.status(201).render('main', {urls : url});
        res.status(201).send({
          status: true,
          message: "login success",
        });
      }
    }
  });
});

//회원등록 - id, 비밀번호, 공유기 MAC주소
router.post("/signin", function (req, res) {
  var member_userid = req.body.userid;
  var member_pwd = req.body.pwd;
  var member_macadd = req.body.mac;

  var usercheck =
    "select userId from walle.person where person.userId ='" +
    member_userid +
    "'";
  var userjoin =
    "INSERT INTO walle.person (userId, pwd, macAdd) VALUES ('" +
    member_userid +
    "', '" +
    member_pwd +
    "', '" +
    member_macadd +
    "')";

  pool.getConnection(function (err, connection) {
    connection.query(usercheck, function (error, rows) {
      if (error) {
        connection.release();
        throw error;
      }
      //userid 중복확인
      else if (rows.length > 0) {
        connection.release();
        res.status(201).send({
          status: true,
          message: "select different id",
        });
      } else {
        connection.query(userjoin, function (error, rows) {
          if (error) {
            connection.release();
            throw error;
          }
          //uerid join
          res.status(201).send({
            status: true,
            message: "signin success",
          });
          connection.release();
        });
      }
    });
  });
});

//반려견 견종 정보 조회

router.get("/sttype", function (req, res) {
  var typelist = [];
  pool.getConnection(function (err, connection) {
    var sql_type = "select type from walle.stweight";

    var showdogtype = connection.query(sql_type, function (err, rows) {
      console.log(rows);
      if (err) {
        connection.release();
        throw err;
      }
      console.log(rows[0].type);

      //typelist=rows
      for (var i = 0; i < rows.length; i++) {
        typelist[i] = rows[i].type;
        console.log(typelist[i]);
      }

      res.status(201).send({
        status: true,
        message: "dogtype send success",
        typelist: typelist,
      });
    });

    connection.release();
  });
});

//반려견 등록 몸무게와 견종을 바탕으로 cfm 지수, 반려견의 상태 mydog 테이블 저장, wgraph저장
router.post("/doginfo", function (req, res) {
  var doginfo = {
    member_dname: req.body.dname,
    member_userid: req.body.userid,
    member_dage: req.body.dage,
    member_stid: req.body.stid,
    member_curweight: req.body.curweight,
  };

  var member_cfm = 0.0;
  var member_status = 0;
  var finduser =
    "select * from walle.person where person.userid ='" +
    doginfo.member_userid +
    "'";
  var findrer =
    "select * from walle.stweight where stweight.stId ='" +
    doginfo.member_stid +
    "'";
  var dnamecheck =
    "select * from walle.mydog where mydog.userId ='" +
    doginfo.member_userid +
    "'and dName='" +
    doginfo.member_dname +
    "'";

  pool.getConnection(function (err, connection) {
    connection.query(finduser, function (error, rows) {
      if (error) {
        connection.release();
        throw error;
      }
      //user 존재
      else if (rows.length == 0) {
        connection.release();
        res.status(201).send({
          status: true,
          message: "please signin",
        });
      } else {
        connection.query(dnamecheck, function (error, rows) {
          if (error) {
            connection.release();
            throw error;
          }
          //dname 중복확인
          else if (rows.length > 0) {
            connection.release();
            res.status(201).send({
              status: true,
              message: "select different name",
            });
          } else {
            connection.query(findrer, function (error, rows) {
              if (error) {
                connection.release();
                throw error;
              }
              //rer cfm
              member_cfm = rows[0].rer;
              if (doginfo.member_curweight < rows[0].minWeight) {
                member_status = 0;
              } else if (doginfo.member_curweight > rows[0].maxWeight) {
                member_status = 2;
              } else {
                member_status = 1;
              }
              console.log(member_cfm);
              // 과체중 2 표준 1 저체중 0

              var dogjoin =
                "INSERT INTO walle.mydog (dName, userId, dAge,stId,curWeight,cfm,status,kcal) VALUES ('" +
                doginfo.member_dname +
                "', '" +
                doginfo.member_userid +
                "', '" +
                doginfo.member_dage +
                "', '" +
                doginfo.member_stid +
                "','" +
                doginfo.member_curweight +
                "','" +
                member_cfm +
                "','" +
                member_status +
                "',0.0)";

              //강아지 체중정보 wgrpah 에 insert

              connection.query(dogjoin, function (error, rows) {
                if (error) {
                  connection.release();
                  throw error;
                }

                //uerid join
                res.status(201).send({
                  status: true,
                  message: "dog join",
                });
                connection.release();
                firstWeight(doginfo);
              });
            });
          }
        });
      }
    });
  });
});
function firstWeight(doginfo) {
  var finddid =
    "select dId from walle.mydog where mydog.userId ='" +
    doginfo.member_userid +
    "'and dName='" +
    doginfo.member_dname +
    "'";

  var member_did = 0;

  pool.getConnection(function (err, connection) {
    connection.query(finddid, function (error, rows) {
      if (error) {
        connection.release();
        throw error;
      }
      member_did = rows[0].dId;
      var weightinsert =
        "INSERT INTO walle.wgraph (weight, dId) VALUES ('" +
        doginfo.member_curweight +
        "', '" +
        member_did +
        "')";

      connection.query(weightinsert, function (error, rows) {
        if (error) {
          connection.release();
          throw error;
        }
        connection.release();
      });
    });
  });
}

//강아지 정보조회 견종, 체중, 최종 체중 업데이트 날짜, 이름, 나이, 사료 칼로리
router.get("/showdoginfo", function (req, res) {
  var member_did = req.headers.did;
  var member_stype;
  var member_updatedate;
  var member_dname;
  var member_dogage;
  var member_dogweight;
  var member_dogkcal;
  console.log(member_did);
  pool.getConnection(function (err, connection) {
    var sql_show_doginfo =
      "select * from walle.mydog where mydog.did ='" + member_did + "'";

    var show_doginfo = connection.query(sql_show_doginfo, function (err, rows) {
      console.log(rows[0]);
      if (err) {
        connection.release();
        throw err;
      }
      member_dname = rows[0].dName;
      member_dogage = rows[0].dAge;
      member_dogweight = rows[0].curWeight;
      member_dogkcal = rows[0].kcal;

      var sql_dogtype =
        "select type from walle.stweight where stweight.stId ='" +
        rows[0].stId +
        "'";

      var dogtype = connection.query(sql_dogtype, function (err, rows) {
        console.log(rows[0]);
        if (err) {
          connection.release();
          throw err;
        }
        member_stype = rows[0].type;

        var sql_recentupdate =
          "select date from walle.wgraph where wgraph.dId ='" +
          member_did +
          "' order by wgId desc";

        var recentupdate = connection.query(sql_recentupdate, function (
          err,
          rows
        ) {
          console.log(rows[0]);
          if (err) {
            connection.release();
            throw err;
          } else if (rows.length == 0) {
            member_updatedate = "X";
          } else {
            member_updatedate = rows[0].date;
          }

          res.status(201).send({
            message: "dogsearch success",
            status: true,
            dogname: member_dname,
            dogtype: member_stype,
            dogage: member_dogage,
            dogweight: member_dogweight,
            update_date: member_updatedate,
            kcal: member_dogkcal,
          });
        });
      });

      connection.release();
    });

    //console.log(query);
  });
});
//회원 정보조회 공유기 Mac주소 정보를 포함해 회원의 정보에 대한 페이지를 제공
router.get("/showuserinfo", function (req, res) {
  var member_userid = req.headers.userid;
  var member_userpwd;
  var member_mac;
  console.log(member_userid);
  pool.getConnection(function (err, connection) {
    var sql_show_userinfo =
      "select * from walle.person where person.userId ='" + member_userid + "'";

    var show_userinfo = connection.query(sql_show_userinfo, function (
      err,
      rows
    ) {
      console.log(rows[0]);
      if (err) {
        connection.release();
        throw err;
      }
      member_userpwd = rows[0].pwd;
      member_mac = rows[0].macAdd;
      res.status(201).send({
        status: true,
        message: "usersearch success",
        userid: member_userid,
        userpwd: member_userpwd,
        usermac: member_mac,
      });
    });

    connection.release();
  });
});

//반려견 정보 삭제
router.get("/deletedog", function (req, res) {
  var member_did = req.headers.did;
  var deletedog =
    "DELETE FROM walle.mydog WHERE mydog.did='" + member_did + "'";

  pool.getConnection(function (err, connection) {
    connection.query(deletedog, function (error, rows) {
      if (error) {
        connection.release();
        throw error;
      }
      //uerid join
      res.status(201).send({
        status: true,
        message: "delete dog",
      });
      connection.release();
    });
  });
});
//회원탈퇴
router.get("/deleteperson", function (req, res) {
  var member_userid = req.headers.userid;
  var deletealldog =
    "DELETE FROM walle.mydog WHERE mydog.userId ='" + member_userid + "'";
  var deleteperson =
    "DELETE FROM walle.person WHERE person.userId ='" + member_userid + "'";
  pool.getConnection(function (err, connection) {
    connection.query(deleteperson, function (error, rows) {
      if (error) {
        connection.release();
        throw error;
      }
      //uerid join
      //res.send('dog delete!!');
      connection.query(deleteperson, function (error, rows) {
        if (error) {
          connection.release();
          throw error;
        }
        //uerid join
        res.status(201).send({
          status: true,
          message: "person delete",
        });
        connection.release();
      });
    });
  });
});

// 로그인 처리 함수
function logIn(logInInfo, callback) {
  var sql_login_check = "select * from person where userId = ? and pwd = ?";
  pool.getConnection(function (error, dbConn) {
    if (error) {
      return callback(error);
    }

    var logInMessage = {};
    dbConn.query(
      sql_login_check,
      [logInInfo.member_id, logInInfo.member_pwd],
      function (error, rows) {
        if (error) {
          dbConn.release();
          return callback(error);
        }
        // 로그인 성공
        if (rows.length >= 1) {
          dbConn.release();
          logInMessage = {
            message: "login success",
            member_info: rows[0],
          };
          return callback(null, logInMessage);
        }
        // 아이디 혹은 비밀번호가 잘못됨 혹은 탈퇴된 회원
        else {
          dbConn.release();
          logInMessage = { message: "check information" };
          return callback(null, logInMessage);
        }
      }
    );
  });
}

// 메인페이지 userid 받기
router.get("/maindog", function (req, res) {
  var member_userid = req.headers.userid;
  var mydog_list;
  console.log(member_userid);
  pool.getConnection(function (err, connection) {
    var sql_show_userinfo =
      "select * from walle.mydog where mydog.userId = '" + member_userid + "'";

    var sql_maindog = connection.query(sql_show_userinfo, function (err, rows) {
      console.log(rows);
      if (err) {
        connection.release();
        throw err;
      }
      mydog_list = rows;
      res.status(201).send({
        status: true,
        message: "usersearch success",
        userid: member_userid,
        doglist: mydog_list,
      });
    });

    connection.release();
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
