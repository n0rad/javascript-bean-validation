<html>
<head>
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>

<script type="text/javascript" src="Validator.js"></script>
<script type="text/javascript" src="form2object.js"></script>
<script type="text/javascript" src="jquery.toObject.js"></script>
<script type="text/javascript">

	personConstraints = ${contraints};

	$(function() {
		
		$("form").submit(function() {
			try {
				var validator = new Validator();
				var dataObject = $(this).toObject();
				var violations = validator.validate(dataObject, personConstraints);
				
				
				var dataJson = JSON.stringify(dataObject);
				alert("salut");
			} catch (e) {
				alert(e);
			} finally {
				return false;
			}
		});
	});
</script>

<style type="text/css">
LABEL,INPUT { /* 			display: block; */
	
}
</style>
</head>
<body>
	<form id="form">

		<label>firstname</label> <input id="firstname" name="firstname">

		<label>lastname</label> <input name="lastname"> <label>email</label>
		<input name="email">

		<fieldset>
			<legend>addresses</legend>

			<div>
				<label>street</label><input name="addresses[0].street">
				<label>city</label><input name="addresses[0].city">
				<label>code</label><input name="addresses[0].code">
			</div>
			<div>
				<label>street</label><input name="addresses[1].street" value="">
				<label>city</label><input name="addresses[1].city" value="Malville">
				<label>code</label><input name="addresses[1].code" value="44260">
			</div>
		</fieldset>

First Name:<input type="text" name="Fname" maxlength="12" size="12"/> <br/>
Last Name:<input type="text" name="Lname" maxlength="36" size="12"/> <br/>
Gender:<br/>
Male:<input type="radio" name="gender" value="Male"/><br/>
Female:<input type="radio" name="gender" value="Female" checked="checked"/><br/>
Favorite Food:<br/>
Steak:<input type="checkbox" name="food[]" value="Steak" checked="checked"/><br/>
Pizza:<input type="checkbox" name="food[]" value="Pizza"/><br/>
Chicken:<input type="checkbox" name="food[]" value="Chicken" checked="checked"/><br/>
<textarea wrap="physical" cols="20" name="quote" rows="5">Enter your favorite quote!</textarea><br/>
Select a Level of Education:<br/>
<select name="education">
<option value="Jr.High">Jr.High</option>
<option value="HighSchool">HighSchool</option>
<option value="College">College</option></select><br/>
Select your favorite time of day:<br/>
<select size="3" name="TofD" multiple="multiple">
<option value="MorningId" selected="selected">Morning</option>
<option value="DayId">Day</option>
<option value="NightId" selected="selected">Night</option></select>

		<input type="submit">
	</form>
</body>
</html>