var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var userSchema = new Schema({
	email : String, //primary key
	password : String, 
	contact : Array,
	gallery : Array
},{versionKey: false});

var contactSchema = new Schema({
	name : String,
	number : String
},{versionKey: false})

var gallerySchema = new Schema({
	image: String
},{versionKey:false})

var postSchema = new Schema({
	email : String,
	title : String,
	content : String
},{versionKey: false})

module.exports = {
	User: mongoose.model('user', userSchema),
	Contact: mongoose.model('contact', contactSchema),
	Gallery: mongoose.model('garrery', gallerySchema),
	Post: mongoose.model('post', postSchema)
}
