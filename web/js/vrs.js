
/*
 * VRS JavaScript API, using JSON.
 * Theoretically supports crappy browsers *cough* IE6/7 *cough* but that's
 * untested.
 */

/*
 * Really, this doesn't exist already?!
 */
Array.prototype.remove = function(s) {
	for (var i = 0; i <  this.length; i++) {
		if(s == this[i]) {
			this.splice(i, 1);
		}
	}
}

var VRS = {
	
	getJSON: function(url) {
		var request = new XMLHttpRequest();
		request.open("GET", url, false);
		
		var text = null;
		
		request.onreadystatechange = function() {
			if (request.readyState == 4 && request.status == 200) {
				text = request.responseText;
			}
		}
		request.send(null);
		
		return JSON.parse(text);
	},
	
	doPost: function(data, callback) {
		var postString = JSON.stringify(data);
		document.write("posting: <pre>" + postString + "</pre><br>");
		
		var request = new XMLHttpRequest();
		request.open("POST", "/submit", true);
		request.setRequestHeader("Content-type", "application/json");
		request.setRequestHeader("Content-length", postString.length);
		request.setRequestHeader("Connection", "close");
		
		request.onreadystatechange = function() {
			if (request.readyState == 4 && request.status == 200) {
				if (typeof callback != "undefined") {
					callback(JSON.parse(request.responseText));
				}
			} else {
				
			}
		}
		
		request.send(postString);
	},
	
	getAlliances: function(callback) {
		$.getJSON("/alliances/", function(data) {
			callback(data);
		});
	},
	
	getProperties: function(callback) {
		$.getJSON("/properties/", function(data) {
			callback(data);
		});
	},
	
	/*
	 * Gets the team with the given number.
	 */
	getTeams: function(callback) {
		//return VRS.getJSON("/teams/");
		$.getJSON("/teams/", function(data) {
			callback(data);
		});
	},
	
	getSchedule: function(callback) {
		$.getJSON("/schedule/", function(data) {
			callback(data);
		});
	},
	
	addTeam: function(number, name, callback) {
		postData = new Object();
		postData.action = "team-add";
		postData.number = number;
		postData.name = name;
		
		$.post("/submit/", JSON.stringify(postData), function(data) {
			if (data.status == "error") {
				alert("Error: " + data.message);
				return;
			}
			
			alert("Success: " + data.message);
			
			if (typeof callback != "undefined") {
				callback(data);
			}
		});
	},
	
	deleteTeam: function(number, callback) {
		var postData = {
			'action': "team-delete",
			'number': number
		}
		
		$.post("/submit/", JSON.stringify(postData), function(data) {
			if (data.status == "error") {
				alert("Error: " + data.message);
				return;
			}
			
			alert("Success: " + data.message);
			
			if (typeof callback != "undefined") {
				callback(data);
			}
		});
	},
	
	matchInit: function(alliances, callback) {
		var postData = {
			'action': "match-init",
			'alliances': alliances
		}
		
		$.post("/submit/", JSON.stringify(postData), function(data) {
			if (data.status == "error") {
				alert("Error: " + data.message);
				return;
			}
			
			if (typeof callback != "undefined") {
				callback(data);
			}
		});
	},
	
	matchStart: function(callback) {
		var postData = {
			'action': "match-start"
		}
		
		$.post("/submit/", JSON.stringify(postData), function(data) {
			if (data.status == "error") {
				alert("Error: " + data.message);
				return;
			}
			
			if (typeof callback != "undefined") {
				callback(data);
			}
		});
	},
	
	matchStop: function(callback) {
		var postData = {
			'action': "match-stop"
		}
		
		$.post("/submit/", JSON.stringify(postData), function(data) {
			if (data.status == "error") {
				alert("Error: " + data.message);
				return;
			}
			
			if (typeof callback != "undefined") {
				callback(data);
			}
		});
	},
	
	matchPause: function(callback) {
		var postData = {
			'action': "match-pause"
		}
		
		$.post("/submit/", JSON.stringify(postData), function(data) {
			if (data.status == "error") {
				alert("Error: " + data.message);
				return;
			}
			
			if (typeof callback != "undefined") {
				callback(data);
			}
		});
	},
	
	matchResume: function(callback) {
		var postData = {
			'action': "match-resume"
		}
		
		$.post("/submit/", JSON.stringify(postData), function(data) {
			if (data.status == "error") {
				alert("Error: " + data.message);
				return;
			}
			
			if (typeof callback != "undefined") {
				callback(data);
			}
		});
	},
	
	matchFinish: function(callback) {
		var postData = {
			'action': "match-finish"
		}
		
		$.post("/submit/", JSON.stringify(postData), function(data) {
			if (data.status == "error") {
				alert("Error: " + data.message);
				return;
			}
			
			if (typeof callback != "undefined") {
				callback(data);
			}
		});
	},
	
	matchCancel: function(callback) {
		var postData = {
			'action': "match-cancel"
		}
		
		$.post("/submit/", JSON.stringify(postData), function(data) {
			if (data.status == "error") {
				alert("Error: " + data.message);
				return;
			}
			
			if (typeof callback != "undefined") {
				callback(data);
			}
		});
	},
	
	addPoint: function(alliance, property, callback) {
		var postData = {
			'action': "point-add",
			'alliance': alliance,
			'property': property
		}
		
		$.post("/submit/", JSON.stringify(postData), function(data) {
			if (data.status == "error") {
				alert("Error: " + data.message);
				return;
			}
			
			if (typeof callback != "undefined") {
				callback(data);
			}
		});
	},
	
	subtractPoint: function(alliance, property, callback) {
		var postData = {
			'action': "point-subtract",
			'alliance': alliance,
			'property': property
		};
		
		$.post("/submit/", JSON.stringify(postData), function(data) {
			if (data.status == "error") {
				alert("Error: " + data.message);
				return;
			}
			
			if (typeof callback != "undefined") {
				callback(data);
			}
		});
	},
	
	getPoint: function(alliance, property, callback) {
		var postData = {
			'action': "get-point",
			"alliance": alliance,
			'property': property
		};
		
		$.post("/submit/", JSON.stringify(postData), function(data) {
			if (data.status == "error") {
				alert("Error: " + data.message);
				return;
			}
			
			if (typeof callback != "undefined") {
				callback(data);
			}
		});
	},
	
	getPoints: function(alliance, callback) {
		var postData = {
			'action': 'get-points',
			'alliance': alliance
		}
		
		$.post("/submit/", JSON.stringify(postData), function(data) {
			if (data.status == "error") {
				alert("Error: " + data.message);
				return;
			}
			
			if (typeof callback != "undefined") {
				callback(data.points);
			}
		});
	},
	
	getStatus: function(callback) {
		var postData = {
			'action': 'get-status'
		}
		
		$.post("/submit/", JSON.stringify(postData), function(data) {
			if (data.status == "error") {
				alert("Error: " + data.message);
				return;
			}
			
			if (typeof callback != "undefined") {
				callback(data.statusText, data.timeLeft);
			}
		});
	}
	
}
