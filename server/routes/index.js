module.exports = function(app, Models,fs,multer){
	//Check Whether Server is Alive
	app.get('/',function(req,res){
			console.log("request to /");
			res.send("Server Connected");	
	})
	//get all contact list
	app.get('/api/contactlist',function(req,res){
			console.log("request to /api/contactlist")
			Models.Contact.find(function(err,contacts){
					if (err) return res.status(500).send({error: 'database failure'});

					res.json(contacts);
			
			
			})
	})
	//get all gallery list
	app.get('/api/gallerylist',function(req,res){
			console.log("request to /api/gallerylist")
			Models.Gallery.find(function(err, galleries){
					if (err) return res.status(500).send({error: 'database failure'});
					res.json(galleries);
			})
	})

	//Show Password of Specific User
	app.get('/api/:email/password',function(req,res){
			console.log("request to /api/"+req.params.email+"/password")
			Models.User.find({email: req.params.email},{_id:0, password: 1}, function(err,user){
					if (err) return res.status(500).json({error: err});
					if (!user) return res.status(404).json({error: 'no such user exists'});
					res.json(user);
			})
	})


	//Show Data of Specific User's Contacts
	app.get('/api/:email/contact',function(req, res){
			console.log("request to /api/"+req.params.email+"/contact")
			Models.User.findOne({email: req.params.email},{_id:0, contact:1}, function(err, user){
					if (err) return res.status(500).json({error: err});
					if (!user) return res.status(404).json({error: 'no such user exists'});
					Models.Contact.find({_id: {$in: user.contact}}, function(err, contacts){
							if (err) return res.status(500).json({error:err});
							res.json(contacts);
					})
			})
	})

	//Show Data of Specifif User's Gallery
	app.get('/api/:email/gallery', function(req, res){
			console.log("request to /api/"+req.params.email+"/gallery")
			Models.User.findOne({email: req.params.email},{_id:0, gallery:1}, function(err, user){
					if (err) return res.status(500).json({error: err});
					if (!user) return res.status(404).json({error: 'no such user exists'});
					Models.Gallery.find({_id: {$in: use.gallery}}, function(err, galleries){
							if (err) return res.status(500).json({error:err});
							res.json(galleries);
					})
			})
	})

	//Add New User
	app.get('/api/NewUser/:email/:password',function(req, res){
			console.log("request to add new user")
			if (req.params.password ==  null){
				return res.status(400).json({error: 'please write valid password'});
			}

			Models.User.findOne({email: req.params.email}, function(err, user){
					if (err) return res.status(500).json({error: err});
					if (user) return res.status(406).json({error: 'user already exists'});

					var newuser = new Models.User();
					newuser.email = req.params.email
					newuser.password = req.params.password
					newuser.contact = []
					newuser.gallery = []

					newuser.save(function(err){
							if (err) {
								console.log(err)
								res.json({result: 0})
								return
							}
							res.json({result:1})
					})

					fs.mkdir("images/"+req.params.email, function(err){
							if (err) console.log(err)
					})
					
					fs.mkdir("images/"+req.params.email+"/gallery", function(err){
							if (err) console.log(err)
					})
			})
	})

	//Add Contact to User
	app.put('/api/addcontact/:email/:name/:number', function(req, res){
			console.log("request to /api/addcontact/"+req.params.email)
			email = req.params.email
			var storage = multer.diskSt
			Models.User.findOne({email: email}, function(err, user){
					if (err) return res.status(500).send({error: err});
					if(req.params.number == ""){
						return res.send({error: 'number not found in request'});
					}	
					Models.Contact.findOne({_id: {$in: user.contact}, number: req.params.number}, function(err, contact){
							if (err) return res.status(500).send({error: err});
							if (!contact) {
								console.log("Add New User Contact")
								newcontact = new Models.Contact()
								newcontact.name = req.params.name
								newcontact.number = req.params.number
								newcontact.save(function(err){
										if (err) {
											console.log(err)
											res.json({result: 0})
											return
										}
										res.json({result: 1})
								})
								user.contact.push(newcontact._id)
								user.save()
							}else{
								console.log("Edit Existing User Contact")
								contact.name = req.params.name
								contact.number = req.parms.number
								contact.save(function(err){
										if (err) {
											console.log(err)
											res.json({result: 0})
											return
										}
										res.json({result: 1})
								})
							}
					})
					
			})
	})

	//Add Image to User's Gallery
	app.post('/api/:email/addimage',function(req, res){
			console.log("request to /api/"+req.params.email+"/addimage")

			email = req.params.email

			var storage = multer.diskStorage({
				destination: function(req, file, cb){
					cb(null, 'images/'+email+"/gallery")
				},
				filename: function(req, file, cb){
					file.fixedname = file.originalname.replace(/\s+/g, '');
					cb(null, file.fixedname)
				}
			})

			var upload = multer({storage: storage}).single('image')

			upload(req, res, function(err){
					if(req.file == null){
						console.log(req.file)
						return res.send({error: 'no image attached'});
					}

					console.log(req.file)
					if (err) return res.status(500).send({error:err});

					Models.User.findOne({email: email}, function(err, user){
							if (err) return res.status(500).send({error: err});
							if (!user){
								return res.send({error: 'no such user exists'});
							}
							newimage = new Models.Gallery()
							newimage.image = 'images/'+email+'/gallery/'+req.file.fixedname
							newimage.save(function(err){
									if (err) {
										console.log(err)
										res.json({result:0})
										return
									}
									res.json({result: 1})
							})
							user.gallery.push(newimage._id)
							user.save()
					})
			})
	})

	app.get('/api/Newpost/:email/:title/:content', function(req, res){
			console.log("request to /api/NewPost/")
			email = req.params.email
			Models.Post.findOne({title: req.params.title}, function(err, post){
					if (err) return res.status(500).send({error: err});
					if (!post){
						console.log("Add New Post")
						newpost = new Models.Post()
						newpost.email = email
						newpost.title = req.params.title
						newpost.content = req.params.content
						newpost.save(function(err){
								if (err) {
									console.log(err)
									res.json({result: 0})
									return
								}
								res.json({result: 1})
						})
					}
			})
	})

	app.get('/api/post', function(req, res){
			console.log("request to /api/post")
			Models.Post.find(function(err, posts){
					if (err) return res.status(500).send({error: 'database failure'});

					res.json(posts);
			})
	})

	app.get('/api/post/:title', function(req, res){
			console.log("/api/post/"+req.params.title)
			Models.Post.find({title: req.params.title},function(err,post){
					if (err) return res.status(500).json({error: err});
					if (!post) return res.status(404).json({error: 'no such user exists'});
					res.json(post);
			})
	})

}









			
