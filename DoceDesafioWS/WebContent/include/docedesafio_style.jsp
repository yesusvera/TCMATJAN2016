
<!-- <link rel="stylesheet" href="./css/jquery-ui.css">
<script src="./js/jquery-2.1.4.min.js"></script>
<script src="./js/jquery-ui.js"></script>
<link rel="stylesheet" href="/resources/demos/style.css"> -->

<link rel="stylesheet"
	href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
<link rel="stylesheet" href="/resources/demos/style.css">


<script src="./js/__jquery.tablesorter.min.js" type="text/javascript"></script>

<script>
	$(function() {
		$("#menu").menu({
			items : "> :not(.ui-widget-header)"
		});
	});

	$(function() {
		$('.fechar').click(function() {
			$('.div_erro').hide();
		});
	});
	
	$(function() {

	    $('.sonums').keypress(function(event) {
	        var tecla = (window.event) ? event.keyCode : event.which;
	        if ((tecla > 47 && tecla < 58)) return true;
	        else {
	            if (tecla != 8) return false;
	            else return true;
	        }
	    });

	});

</script>
<style>
th {
	cursor: pointer;
	font-size: medium;
}

/*JQUERY UI*/
.ui-menu {
	width: 200px;
	font-size: medium;
}

.ui-widget-header {
	padding: 0.2em;
	color: graytext;
}
/*****************/
.boxLogin {
	position: absolute;
	margin-left: 50%;
	width: 400px;
	height: 300px;
	text-align: center;
	border: solid 1px Gainsboro;
	margin-top: 23px;
	/* background-image: linear-gradient(45deg, white, #EEF7FA, #EFF5FB); */
	background-color: white;
	-moz-border-radius: 12px;
	-webkit-border-radius: 12px;
	border-radius: 12px;
}

.boxConteudoDoceDesafio {
	position: absolute;
	margin-left: 230px;
	width: 75%;
	/* height: 90%; */
	text-align: center;
	border: solid 1px Gainsboro;
	margin-top: 23px;
	/* background-image: linear-gradient(45deg, white, Snow1); */
	background-image: linear-gradient(45deg, white, #F4FDFF);
	-moz-border-radius: 2px;
	-webkit-border-radius: 2px;
	border-radius: 2px;
}

.tituloBoxConteudo {
	width: 100%;
	height: 32px;
	text-align: center;
	font-size: x-large;
	color: gray;
	border: solid 1px Gainsboro;
	margin-bottom: 10px;
	/* background-image: linear-gradient(45deg, white, Gold); */
	/* background-color: #fffd73; */
	-moz-border-radius: 2px;
	-webkit-border-radius: 2px;
	border-radius: 2px;
}

#header {
	/* background-image: linear-gradient(to right, white, gold, yellow); */
	background-color: #fffd73;
	color: black;
	text-align: center;
	padding: 5px;
	-moz-border-radius: 7px;
	-webkit-border-radius: 7px;
	border-radius: 7px;
}
/*
#menu ul {
	padding: 0px;
	margin: 0px;
	background-color: #bdbdbd;
	list-style: none;	
	text-align: center;
}

#menu ul li {
	display: inline;
}

#menu ul li a {
	padding: 7px 20px;
	display: inline-block;
	background-color: #bdbdbd;
	color: #333;
	font-weight: bold;
	text-decoration: none;
	border-bottom: 0px solid #bdbdbd;
}

#menu ul li a:hover {
	background-color: gold;
}*/
body {
	font: normal medium sans-serif;
	background-image: linear-gradient(45deg, white, #D4E2E8);
}

table {
	border-collapse: collapse;
	/* width: 90%; */
	margin-bottom: 10px;
	margin: 0px auto;
	border-color: white;
	border: 1px solid Gainsboro;
}

input {
	font-size: 10pt;
}

select {
	font-size: 10pt;
}

textarea{
	background-color: white;
}

/* input[type=text] {
	border-radius: 4px;
	-moz-border-radius: 4px;
	-webkit-border-radius: 4px;
	box-shadow: 1px 1px 2px #333333;
	-moz-box-shadow: 1px 1px 2px #333333;
	-webkit-box-shadow: 1px 1px 2px #333333;
	background: white;
	border: 1px solid #000000;
	width: 150px;
	height: 20px;
} */
input[type=text] {
	background-color: white;
	color: black;
	border: 1px solid #c5c5c5;
}

input[type=password] {
	background-color: white;
	color: black;
	border: 1px solid #c5c5c5;
}

input[type=button] {
	background-color: white;
	color: graytext;
	border: 1px solid;
}
/* input[type=password] {
	border-radius: 4px;
	-moz-border-radius: 4px;
	-webkit-border-radius: 4px;
	box-shadow: 1px 1px 2px #333333;
	-moz-box-shadow: 1px 1px 2px #333333;
	-webkit-box-shadow: 1px 1px 2px #333333;
	background: white;
	border: 1px solid #000000;
	width: 150px;
	height: 20px;
} */
textarea {
	border: 1px solid #000000;
	background: #cccccc;
	width: 150px;
	height: 100px;
	border-radius: 4px;
	-moz-border-radius: 4px;
	-webkit-border-radius: 4px;
	box-shadow: 1px 1px 2px #333333;
	-moz-box-shadow: 1px 1px 2px #333333;
	-webkit-box-shadow: 1px 1px 2px #333333;
}

input[type=text]:hover, textarea:hover {
	background-color: white;
	color: graytext;
	border: 1px solid;
}

input[type=submit] {
	background-color: white;
	color: graytext;
	border: 1px solid;
}

input[type=reset] {
	background-color: white;
	color: red;
	border: 1px solid;
	/* background: red;
	color: #ffffff; */
}

/* input[type=button] {
	background: #006699;
	color: #ffffff;
} */

th, td {
	padding: 0.25rem;
	text-align: left;
	border: none;
}

hr{
	color: graytext;
	width: 70%;
	border-style: solid 1px;
}

tbody tr:nth-child(odd) {
	background: #eee;
}

/* tbody tr:first-child {
	background: #bdbdbd;
	font-weight: bold;
} */

/*Mesagem de erro*/
.div_erro {
	border: 1px solid #EBCCD1;
	background-color: #F2DEDE;
	font-family: verdana;
	font-size: 13px;
	color: #A94442;
	width: 98%;
	height: 23px;
	padding: 10px 10px;
}

.fechar {
	float: right;
	cursor: pointer;
	font-weight: bold;
}

.msgRegistros {
	font-size: small;
	text-align: right;
	color: graytext;
	margin-bottom: 20px;
}
</style>

