/*test*/
var express = require("express");
var router = express.Router();

/* GET home page. */

router.post("/", function (req, res) {
  var dstep = req.body.dstep;
  var madd = req.body.madd;
  var stime = req.body.stime;
  console.log();
  console.log(dstep + " " + madd + " " + stime);
  //res.render('server', { title: title });
  res.status(201).send({
    status: true,
    message: "successful sign in",
    dstep: dstep,
    madd: madd,
    stime: stime,
  });
});

module.exports = router;
