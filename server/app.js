var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var mongoose = require('mongoose');
var fs = require('fs')
var multer = require('multer')

app.use(bodyParser.urlencoded({extended: true}));

app.use(bodyParser.json());

var port = process.env.PORT || 8080;

//var router = require('./routes')(app, Contact);

//app.set('views',__dirname+'/views');
//app.set('view engine','ejs');
//app.engine('html',require('ejs').renderfile);


var db = mongoose.connection;
db.on('error', console.error);
db.once('open',function(){
		console.log("Connected to mongod server");
	});

mongoose.connect('mongodb://localhost:27017/project2');

var Models  = require('./models/user');

var router = require('./routes')(app, Models, fs, multer);

var server = app.listen(port,function(){
		console.log("Express has started on port" + port)
		});



