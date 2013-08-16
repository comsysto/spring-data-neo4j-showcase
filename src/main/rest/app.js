$(document).ready(function () {

	var getDataFromServer = function (cypher, cb) {
		cb = cb || $.noop;
		var requestData = {
			"query": cypher,
			"params": {}
		};
		$.ajax({
			type: "POST",
			url: 'http://localhost:7474/db/data/cypher',
			data: requestData,
			success: function (data) { cb(null, data); },
			error: function (err) { cb(err); },
			dataType: "json"
		});
	};

    var cypher = 'start r=rel(*) match (n)-[r:VIEWED]->(m) return ID(n),n.name,n.type,ID(m),m.name,m.type,ID(r),type(r),r.count;';
    														// 		0 		1 	2 		3 	   4	5 		6 		7 		8

	getDataFromServer(cypher, function (err, res) {
		if (err) {
			console.log(err);
			return;
		}

		// translate into array of links and nodes
		var links = [];
		var nodes = {};
		var maxCount = 0;
		for (var i=0; i<res.data.length;i++) {
			links.push({ //name : res.data[i][6],
						source : res.data[i][0], 
						target : res.data[i][3],
						type : res.data[i][7],
						count : res.data[i][8]});

			// get max count
			if (res.data[i][8] > maxCount) {
				maxCount = res.data[i][8];
			}

			// if nodes[links[i].source] is undefined create it
			links[i].source = nodes[links[i].source] || (nodes[links[i].source] = {	
																					name: res.data[i][1], //name
																					type: res.data[i][2]  //product type 
																					});
		  	links[i].target = nodes[links[i].target] || (nodes[links[i].target] = {
		  																			name: res.data[i][4], //name
		  																			type: res.data[i][5]  //product type
		  																			});
			
		}

		
		// var nodes = {};
		// // Compute the distinct nodes from the links.
		// // Make endpoints of links objects instead of numbers (representing ids)
		// links.forEach(function(link) {
		//   link.source = nodes[link.source] || (nodes[link.source] = {name: link.source});
		//   link.target = nodes[link.target] || (nodes[link.target] = {name: link.target});
		// });

		function tick() {
		  link
		      .attr("x1", function(d) { return d.source.x; })
		      .attr("y1", function(d) { return d.source.y; })
		      .attr("x2", function(d) { return d.target.x; })
		      .attr("y2", function(d) { return d.target.y; });

		  node
		      .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
		}

		var width = 500,
			height = 500,
			radius = 5,
			dist = 120;

		var maxWidth = 3;

		// var color = d3.scale.ordinal()
  //   				.domain(["PIZZA", "SALAD", "BEVERAGE", "PASTA"])
  //   				.range(["#111", "#000", "#333", "#777"]);

  		var color = d3.scale.category10()

  		var weight = function(x, maxCount, maxWidth) { 
  			// return x.map(function(z) {return (z / maxCount) * maxWidth; });
  			return x / maxCount * maxWidth
  		}

		var force = d3.layout.force()
		.nodes(d3.values(nodes))
		.links(links)
		.size([width, height])
		.linkDistance(dist)
		.charge(-200)
		.on("tick", tick)
		.start();

		var svg = d3.select("#container").append("svg")
		.attr("width", width)
		.attr("height", height);

		var link = svg.selectAll(".link")
		.data(force.links())
		.enter().append("line")
		.attr("class", "link")
		.attr("opacity", 0.4)
		.style("stroke-width", function(d) { return weight(d.count, maxCount, maxWidth)});

		var node = svg.selectAll(".node")
		.data(force.nodes())
		.enter().append("g")
		.attr("class", "node")
		.call(force.drag);

		node.append("circle")
		.attr("r", radius)
		.style("fill", function(d) { return color(d.type); });

		node.append("text")
		.attr("x", 12)
		.attr("dy", ".35em")
		.text(function(d) { return d.name; });
	
	});

});


