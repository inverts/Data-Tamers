/**
 * hypothetical-future.js
 */

$(document).ready(function() {
	
	GetWidget('testWidget');
	
	//GetWidget('testWidget2');
	
	//GetWidget('testWidget3');
	
	//GetWidget('testWidget4');

});


function GetWidget(id, source, change) {
	
	var $element = $('#' + id);
	
	$.post("HypotheticalFuture", { source: source, change: change }, 
		function(response) {
			$element.empty().append(response);
			//new Processing.loadSketchFromSources('hypotheticalFutureData', ['cache/pde/Grapher.pde']);
			var sketch = new Processing.Sketch();
			
			sketch.attachFunction = function(processing) {
				  var tex;
				  var rotx = Math.PI/4;
				  var roty = Math.PI/4;

				  processing.setup = function() {
				    processing.size(640, 360, processing.P3D);
				    tex = processing.loadImage("pjs.png");
				    processing.textureMode(processing.NORMALIZED);
				    processing.fill(255);
				    processing.stroke(processing.color(44,48,32));
				    $('')
				  };

				  processing.draw = function() {
				    processing.background(0);
				    processing.noStroke();
				    processing.translate(processing.width/2.0, 
				                         processing.height/2.0, -100);
				    processing.rotateX(rotx);
				    processing.rotateY(roty);
				    processing.scale(90);
				    texturedCube(tex);
				  }

				  function texturedCube(tex) {
				    processing.beginShape(processing.QUADS);
				    processing.texture(tex);

				    
				    // +Z "front" face
				    processing.vertex(-1, -1,  1, 0, 0);
				    processing.vertex( 1, -1,  1, 1, 0);
				    processing.vertex( 1,  1,  1, 1, 1);
				    processing.vertex(-1,  1,  1, 0, 1);

				    // -Z "back" face
				    processing.vertex( 1, -1, -1, 0, 0);
				    processing.vertex(-1, -1, -1, 1, 0);
				    processing.vertex(-1,  1, -1, 1, 1);
				    processing.vertex( 1,  1, -1, 0, 1);

				    // +Y "bottom" face
				    processing.vertex(-1,  1,  1, 0, 0);
				    processing.vertex( 1,  1,  1, 1, 0);
				    processing.vertex( 1,  1, -1, 1, 1);
				    processing.vertex(-1,  1, -1, 0, 1);

				    // -Y "top" face
				    processing.vertex(-1, -1, -1, 0, 0);
				    processing.vertex( 1, -1, -1, 1, 0);
				    processing.vertex( 1, -1,  1, 1, 1);
				    processing.vertex(-1, -1,  1, 0, 1);

				    // +X "right" face
				    processing.vertex( 1, -1,  1, 0, 0);
				    processing.vertex( 1, -1, -1, 1, 0);
				    processing.vertex( 1,  1, -1, 1, 1);
				    processing.vertex( 1,  1,  1, 0, 1);

				    // -X "left" face
				    processing.vertex(-1, -1, -1, 0, 0);
				    processing.vertex(-1, -1,  1, 1, 0);
				    processing.vertex(-1,  1,  1, 1, 1);
				    processing.vertex(-1,  1, -1, 0, 1);

				    processing.endShape();
				  }

				  // mouse event
				  processing.mouseDragged = function() {
				    var rate = 0.01;
				    rotx += (processing.pmouseY-processing.mouseY) * rate;
				    roty += (processing.mouseX-processing.pmouseX) * rate;
				  };
				};
			
			var canvas = $('#hypotheticalFutureData').get(0);
			var p = new Processing(canvas, sketch);
			
			// Setup change event
			$('select').on('change', function() {
				GetWidget(id, $('#traffic_source').val(), $('#change_pct').val());
			});
	});
	
}