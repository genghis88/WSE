function init()
		{
                        var ca = document.cookie.split(';');
			var elems = document.getElementsByClassName('outerelement');
			var query = document.getElementById('search-list-div').getAttribute("queryattribute");
			console.log(query);
			for( var elem in elems)
			{
				
				elems[elem].onclick = function(){
					var me = this;
					
					var sessionId = ca[0];
					var url = "/log/abc";
					//var params = "?docid=" + me.getAttribute('docid') + "&sessionid=" + sessionId + "&query=" + query;
                                        var params = "?docid=" + me.getAttribute('docid') + "&query=" + query;
					
					var http;
					if (window.XMLHttpRequest)
					{// code for IE7+, Firefox, Chrome, Opera, Safari
					  http=new XMLHttpRequest();
					}
					else
					{// code for IE6, IE5
					  http=new ActiveXObject("Microsoft.XMLHTTP");
					}
					http.open("GET", url+params, true);

					//Send the proper header information along with the 			
					http.onreadystatechange = function() {//Call a function when the state changes.
						if(http.readyState == 4 && http.status == 200) {
							alert(http.responseText);
						}
					}					
					http.send();
				};
			}
		}
