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
	
		validator = new Validator();
		SecurityCheck = function(obj, attributes) {
			if (obj == null) {
				return true;
			}
			
			if (obj.personalNumber == undefined) {
				return false;
			}
			
			return "000000-0000" != obj.personalNumber;
		}
		validator.registerConstraint('org.hibernate.jsr303.tck.tests.constraints.application.SecurityCheck', SecurityCheck)
		cv = validator.validate(JSON.parse('{"gender":"FEMALE","firstName":"Sarah","lastName":"Jones","personalNumber":"000000-0000"}'), JSON.parse('{"properties":{"lastName":{"constraints":[{"type":"javax.validation.constraints.NotNull","attributes":{"message":"{javax.validation.constraints.NotNull.message}"},"reportAsSingle":false}]},"firstName":{"constraints":[{"type":"javax.validation.constraints.NotNull","attributes":{"message":"{javax.validation.constraints.NotNull.message}"},"reportAsSingle":false}]},"personalNumber":{"constraints":[{"type":"javax.validation.constraints.Pattern","attributes":{"message":"Personal number must be 10 digits with the last 4 separated by a dash.","flags":[],"regexp":"^[0-9][0-9][0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]$"},"reportAsSingle":false}]}},"constraints":[{"type":"org.hibernate.jsr303.tck.tests.constraints.application.SecurityCheck","attributes":{"message":"Security check failed.","groups":["javax.validation.groups.Default","org.hibernate.jsr303.tck.tests.constraints.application.TightSecurity"]},"reportAsSingle":false},{"type":"org.hibernate.jsr303.tck.tests.constraints.application.SecurityCheck","attributes":{"message":"Security check failed.","groups":["javax.validation.groups.Default"]},"reportAsSingle":false}]}'))


		
		
		var myform = $("form");
		
		var MyNotEmpty = function(obj, attributes) {
            if (obj == undefined) {
                return true;
            }
            return $.trim(obj).length > 0;
		};
		
		validator.registerConstraint("net.awired.validation.MyNotEmpty", MyNotEmpty);
				
		var inlineValidate = function(e) {
			var f = myform;
			var propertyDescriptor = validator.getPropertyDescriptorFromPath(personConstraints, this.name);
			var listElem = $('[name="' + this.name + '"]', myform);
			var violations = validator.validate(listElem.toObject({mode : 'combine', skipEmpty : false}), propertyDescriptor);
		};
		
		var elements = $("input, textarea, select", myform);
		elements.keyup(inlineValidate);
		elements.change(inlineValidate);
		
		myform.submit(function() {
			try {
				var formElements = $("input, textarea, select", this);
				formElements.removeClass('red');

				var dataObject = $(this).toObject({skipEmpty : false});
				var violations = validator.validate(dataObject, personConstraints);
				
				
				var dataJson = JSON.stringify(dataObject);
				
				for (var i = 0; i < violations.length; i++) {
					$('[name="' + violations[i].propertyPath + '"]').addClass('red');
				}
				
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

.red {
	border: 1px solid red;
}
</style>
</head>
<body>
	<form id="form">

<!-- First Name:<input type="text" name="Fname" maxlength="12" size="12"/> <br/> -->
<!-- Last Name:<input type="text" name="Lname" maxlength="36" size="12"/> <br/> -->
		<label>firstname</label><input name="firstname">
		<label>lastname</label><input name="lastname">
		<label>email</label><input name="email"><br/>
		Male:<input type="radio" name="gender" value="Male"/>Female:<input type="radio" name="gender" value="Female"/><br/>

		<fieldset>
			<legend>Competences</legend>
		
			<label>java rating</label><input name="languageSkill[java].rating">
			<label>js rating</label><input name="languageSkill[js].rating">
		</fieldset>

		<fieldset>
			<legend>Addresses</legend>

			<div>
				<label>street</label><input name="addresses[1].street">
				<label>city</label><input name="addresses[1].city">
				<label>code</label><input name="addresses[1].code">
			</div>
			<div>
				<label>street</label><input name="addresses[3].street">
				<label>city</label><input name="addresses[3].city">
				<label>code</label><input name="addresses[3].code">
			</div>
		</fieldset>



Food:<br/>Steak:<input type="checkbox" name="food[]" value="Steak"/><br/>
Pizza:<input type="checkbox" name="food[]" value="Pizza"/><br/>
Chicken:<input type="checkbox" name="food[]" value="Chicken"/><br/>
<textarea cols="20" name="quote" rows="5">Enter your favorite quote!</textarea><br/>
Select a Level of Education:<br/>
<select name="education">
<option value="Jr.High">Jr.High</option>
<option value="HighSchool">HighSchool</option>
<option value="College">College</option></select><br/>
Select your favorite time of day:<br/>
<select size="3" name="favoriteDayTime" multiple="multiple">
<option value="MorningId">Morning</option>
<option value="DayId">Day</option>
<option value="NightId">Night</option></select>

		<input type="submit">
	</form>
</body>
</html>