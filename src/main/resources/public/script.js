$(document).ready(function(){
	/* This code is executed after the DOM has been completely loaded */
	
	// Increase the font size	
	$('a.increase').click(function(){					
		var size = $('div.articleBody').css('font-size').split('px')[0];
		$('div.articleBody').css({'font-size' : (parseInt(size) + 1) + "px"});
	});

	// Decrease the font size
	$('a.decrease').click(function(){					
		var size = $('div.articleBody').css('font-size').split('px')[0];
		$('div.articleBody').css({'font-size' : (parseInt(size) - 1) + "px"});
	});

	var doPollForResponse = function(url, element, instance){
		$.ajax({
			type : "GET",  
			url: url + "?instance=" + instance + "&blocking=" +  $("#blocking").is(":checked"),
			data: "",
			success: function(data) {
	        	if (!data.endsWith("Done")) {
	        		if (!data.startsWith("Wait")) {
	        			$(element).append(data+ '<br>'); // We can get a wait when there is no data to return yet.
	        		}
	        		setTimeout(function() {
	        			doPollForResponse(url, element, instance);
	        		}, 150);
	    		} else {
	    			clear(url, instance); // Once we get done, clear the map on the server.
	    		}
			},
			async:true,
			dataType: "text"
		});
	}
	
	var clear = function(url, instance) {
		$.ajax({
			type:"GET",
			url:"/clear?instance=" + instance + "&url=" + url,
			data:"",
			success: function(data) {
				
			},
			async:true,
			dataType:"text"
		});
	}

	var initiate = function(url, element){
		// $(element).html('Initated<br>');
		$.ajax({
			type : "GET",  
			url: url,
			data: "",
			success: function(data) {
				$(element).html(data+'<br>');
			},
			async:true,
			dataType: "text"
		});
	}

	$('a.compositeButton').click(function(){
		clear();
		var timestamp = Date.now(); // Use this to create unique instances on the server
		initiate("/traditionalExecutor?instance=" + timestamp + "&size=" + $("#dataSize").val(), "#traditionalResponse");
		doPollForResponse("/traditionalResponse", "#traditionalResponse", timestamp);
		initiate("/compositeExecutor?instance=" + timestamp + "&size=" + $("#dataSize").val(), "#compositeResponse");
		doPollForResponse("/compositeResponse", "#compositeResponse", timestamp);
	});
	
	$('a.transactionalityButton').click(function(){
		// clear();
		var timestamp = Date.now(); // Use this to create unique instances on the server
		initiate('/transactionalityTraditional?instance=' + timestamp + '&accountName=' + $('#accountName').val() + '&contact1Name=' + $('#contact1LastName').val() + '&contact2Name=' + $('#contact2LastName').val() + '&contact3Name=' + $('#contact3LastName').val(), "#traditionalResponse");
		doPollForResponse("/traditionalResponse", "#traditionalResponse", timestamp);
		initiate("/transactionalityComposite?instance=" + timestamp + '&accountName=' + $('#accountName').val() + '&contact1Name=' + $('#contact1LastName').val() + '&contact2Name=' + $('#contact2LastName').val() + '&contact3Name=' + $('#contact3LastName').val(), "#compositeResponse");
		doPollForResponse("/compositeResponse", "#compositeResponse", timestamp);
	});
	
});